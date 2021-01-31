package org.jxtech.propertytrade.platform.listing.controller.seller

import com.fasterxml.jackson.databind.ObjectMapper
import com.neovisionaries.i18n.CurrencyCode
import org.jxtech.propertytrade.platform.common.persistence.entity.Price
import org.jxtech.propertytrade.platform.common.util.excel.ExcelParsingUtil
import org.jxtech.propertytrade.platform.common.util.file.FileParseLineError
import org.jxtech.propertytrade.platform.common.util.file.FileParseLineResult
import org.jxtech.propertytrade.platform.common.util.file.FileParseLineResultStatus
import org.jxtech.propertytrade.platform.common.util.file.FileParseResult
import org.jxtech.propertytrade.platform.listing.controller.ListingCreatedEvent
import org.jxtech.propertytrade.platform.listing.persistence.entity.Listing
import org.jxtech.propertytrade.platform.listing.service.ListingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.http.ResponseEntity
import org.springframework.messaging.SubscribableChannel
import org.springframework.messaging.support.MessageBuilder
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.socket.TextMessage
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.pow

@RestController
@RequestMapping("/seller/listing")
class ListingFileUploadController {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ListingFileUploadController::class.java)
    }

    @Autowired
    lateinit var listingService: ListingService

    @Autowired
    lateinit var listingCreateEventChannel: SubscribableChannel
    private val listingFileUploadValidator = ListingFileUploadValidator()
    private val listingFileUploadConverter = ListingFileUploadConverter()
    private val listingCreatedEventConverter = ListingCreatedEventConverter()
    private val objectMapper = ObjectMapper()


    @PostMapping("/upload")
    fun performUpload(@RequestParam("file") file: MultipartFile): ResponseEntity<FileParseResult> {
        val fileName = file.name
        logger.info("Perform listing file upload with file name: {}", fileName)
        val inputStream = file.inputStream
        val columns = listOf("propertyId", "agentId", "description", "price", "startDate")
        val fileParseResult = FileParseResult()
        ExcelParsingUtil.parseExcelStream(inputStream, columns, listingFileUploadValidator,
            { lineNumber, successResult ->
                val listing = listingFileUploadConverter.convert(successResult)
                val savedListingId = listingService.createListing(listing)
                fireMessageEvent(listing)
                fileParseResult.addFileParseLineResult(FileParseLineResult(lineNumber.toLong(),
                    FileParseLineResultStatus.CREATED).apply {
                    this.lineDataId = savedListingId
                })
            },
            { lineNumber, error ->
                fileParseResult.addFileParseLineResult(FileParseLineResult(lineNumber.toLong(),
                    FileParseLineResultStatus.ERROR).apply {
                    this.lineErrors = error.allErrors.map {objectError ->
                        FileParseLineError(objectError.code, objectError.defaultMessage)
                    }
                })
            })
        return ResponseEntity.ok(fileParseResult)
    }

    fun fireMessageEvent(listing: Listing) {
        val listingCreatedEvent = listingCreatedEventConverter.convert(listing)
        val listingCreatedMessage = MessageBuilder
            .withPayload(objectMapper.writeValueAsString(listingCreatedEvent))
            .build()
        listingCreateEventChannel.send(listingCreatedMessage)
    }
}

class ListingFileUploadValidator: Validator {
    override fun validate(target: Any, errors: Errors) {
    }

    override fun supports(clazz: Class<*>): Boolean {
        return true
    }
}

class ListingFileUploadConverter: Converter<List<Pair<String, Any?>>, Listing> {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(ZoneOffset.UTC)
    override fun convert(source: List<Pair<String, Any?>>): Listing {
        val propertyId = (source[0].second as Double).toLong()
        val agentId = (source[1].second as Double).toLong()
        val description = source[2].second.toString()
        val price = source[3].second as Double
        val startDate = source[4].second as String
        return Listing(propertyId, agentId).apply {
            this.description = description
            this.price = Price().apply {
                this.currency = CurrencyCode.USD
                val priceVal = (price * (10.0.pow(this.currency.minorUnit))).toLong()
                this.amount = BigInteger.valueOf(priceVal)
            }
            this.effectiveDateStart = LocalDate.parse(startDate, dateFormatter)
            this.createdOn = LocalDateTime.now()
            this.lastUpdatedOn = LocalDateTime.now()
            this.createdBy = "admin"
            this.lastUpdatedBy = "admin"
        }
    }
}

class ListingCreatedEventConverter: Converter<Listing, ListingCreatedEvent> {
    override fun convert(source: Listing): ListingCreatedEvent {
        return ListingCreatedEvent(source.id, source.description.orEmpty(),
            (source.price?.amount?:BigInteger.valueOf(0)).toString())
    }
}
