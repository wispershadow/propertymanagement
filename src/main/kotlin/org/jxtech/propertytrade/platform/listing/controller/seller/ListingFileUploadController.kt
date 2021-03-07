package org.jxtech.propertytrade.platform.listing.controller.seller

import com.fasterxml.jackson.databind.ObjectMapper
import com.neovisionaries.i18n.CurrencyCode
import org.jxtech.propertytrade.platform.common.persistence.entity.Price
import org.jxtech.propertytrade.platform.common.service.data.AddressSaveRequest
import org.jxtech.propertytrade.platform.common.service.data.USAddressSaveRequest
import org.jxtech.propertytrade.platform.common.util.excel.ExcelParsingUtil
import org.jxtech.propertytrade.platform.common.util.file.FileParseLineError
import org.jxtech.propertytrade.platform.common.util.file.FileParseLineResult
import org.jxtech.propertytrade.platform.common.util.file.FileParseLineResultStatus
import org.jxtech.propertytrade.platform.common.util.file.FileParseResult
import org.jxtech.propertytrade.platform.listing.controller.ListingCreatedEvent
import org.jxtech.propertytrade.platform.listing.persistence.entity.Listing
import org.jxtech.propertytrade.platform.listing.persistence.entity.ListingOwnerType
import org.jxtech.propertytrade.platform.listing.persistence.entity.ListingType
import org.jxtech.propertytrade.platform.listing.service.ListingService
import org.jxtech.propertytrade.platform.listing.service.data.AgentSaveRequest
import org.jxtech.propertytrade.platform.listing.service.data.ListingSaveRequest
import org.jxtech.propertytrade.platform.property.persistence.entity.BuildingType
import org.jxtech.propertytrade.platform.property.service.data.BuildingSaveRequest
import org.jxtech.propertytrade.platform.property.service.data.PropertySaveRequest
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
    private val batchThreshold = 10


    @PostMapping("/upload")
    fun performUpload(@RequestParam("file") file: MultipartFile): ResponseEntity<FileParseResult> {
        val fileName = file.name
        logger.info("Perform listing file upload with file name: {}", fileName)
        val inputStream = file.inputStream
        val columns = listOf("locationNames", "street1", "street2", "street3", "postalCode", "buildingType",
            "buildingDescription", "totalUnits", "totalStories", "totalFamilyNum", "builtYear", "propertyNo",
            "propertyDescription", "propertySize", "bedRoomNo", "bathRoomNo", "totalRoomNo", "tags", "agentFirstName",
            "agentMiddleName", "agentLastName", "agentPhoneNo", "agentCompany", "companyStreet1", "companyStreet2",
            "companyStreet3", "companyLocation", "listingType", "listingOwnerType", "price", "startDate")
        val fileParseResult = FileParseResult()
        val listingSaveRequests = mutableListOf<ListingSaveRequest>()
        val userName = "james"
        ExcelParsingUtil.parseExcelStream(inputStream, columns, listingFileUploadValidator,
            { lineNumber, successResult ->
                val listingSaveReq = listingFileUploadConverter.convert(successResult)
                listingSaveRequests.add(listingSaveReq)
                if (listingSaveRequests.size == batchThreshold) {
                    listingService.batchSave(listingSaveRequests, userName)
                    listingSaveRequests.clear()
                }
            },
            { lineNumber, error ->
                fileParseResult.addFileParseLineResult(FileParseLineResult(lineNumber.toLong(),
                    FileParseLineResultStatus.ERROR).apply {
                    this.lineErrors = error.allErrors.map {objectError ->
                        FileParseLineError(objectError.code, objectError.defaultMessage)
                    }
                })
            })
        if (listingSaveRequests.size > 0) {
            listingService.batchSave(listingSaveRequests, userName)
            listingSaveRequests.clear()
        }
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

class ListingFileUploadConverter: Converter<List<Pair<String, Any?>>, ListingSaveRequest> {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(ZoneOffset.UTC)

    fun formatDoubleAsString(value: Any?): String {
        return (value as Double).toInt().toString()
    }

    fun formatDoubleAsInt(value: Any?): Int {
        return (value as Double).toInt()
    }

    override fun convert(source: List<Pair<String, Any?>>): ListingSaveRequest {
        val locationNames = source[0].second as String
        val buildingStreet1 = source[1].second as String
        val buildingStreet2 = source[2].second as String?
        val buildingStreet3 = source[3].second as String?
        val buildingPostalCode = formatDoubleAsString(source[4].second)
        val buildingType = source[5].second as String
        val buildingDesc = source[6].second as String?
        val totalUnit = formatDoubleAsInt(source[7].second)
        val totalStories = formatDoubleAsInt(source[8].second)
        val totalFamily = formatDoubleAsInt(source[9].second)
        val builtYear = formatDoubleAsInt(source[10].second)
        val propertyNo = source[11].second.toString()
        val propertyDesc = source[12].second as String?
        val propertySize = source[13].second as String
        val bedRoomNo = formatDoubleAsInt(source[14].second)
        val bathRoomNo = formatDoubleAsInt(source[15].second)
        val totalRoomNo = formatDoubleAsInt(source[16].second)
        val tags = source[17].second as String?
        val agentFirstName = source[18].second as String
        val agentMidName = source[19].second as String?
        val agentLastName = source[20].second as String
        val agentPhoneNo = formatDoubleAsString(source[21].second)
        val agentCompany = source[22].second as String
        val companyStreet1 = source[23].second as String
        val companyStreet2 = source[24].second as String?
        val companyStreet3 = source[25].second as String?
        val companyLocations = source[26].second as String
        val listingType: String = source[27].second as String
        val listingOwnerType = source[28].second as String
        val listingPrice = (source[29].second as Double).toString()
        val listingStartDate = source[30].second as String

        val buildingAddressSaveRequest = USAddressSaveRequest(locationNames, buildingPostalCode).apply {
            this.street1 = buildingStreet1
            this.street2 = buildingStreet2.orEmpty()
            this.street3 = buildingStreet3.orEmpty()
        }

        val buildingSaveRequest = BuildingSaveRequest().apply {
            this.addressSaveRequest = buildingAddressSaveRequest
            this.buildingType = BuildingType.valueOf(buildingType)
            this.builtYear = builtYear
            this.totalFamilyNum = totalFamily
            this.totalStories = totalStories
            this.totalUnit = totalUnit
            this.buildingDescription = buildingDesc.orEmpty()
        }

        val propertySaveRequest = PropertySaveRequest().apply {
            this.buildingSaveRequest = buildingSaveRequest
            this.propertyNo = propertyNo
            this.propertyDescription = propertyDesc.orEmpty()
            this.propertySize = propertySize
            this.bathRoomNo = bathRoomNo
            this.bedRoomNo = bedRoomNo
            this.totalRoomNo = totalRoomNo
            this.tags = tags.orEmpty()
        }

        val agentAddressSaveRequest = USAddressSaveRequest(companyLocations, null).apply {
            this.street1 = companyStreet1
            this.street2 = companyStreet2.orEmpty()
            this.street3 = companyStreet3.orEmpty()
        }

        val agentSaveRequest = AgentSaveRequest().apply {
            this.agentCompanyName = agentCompany
            this.firstName = agentFirstName
            this.middleName = agentMidName
            this.lastName = agentLastName
            this.phone = agentPhoneNo
            this.addressSaveRequest = agentAddressSaveRequest
        }

        return ListingSaveRequest().apply {
            this.listingOwnerType = ListingOwnerType.valueOf(listingOwnerType)
            this.listingType = ListingType.valueOf(listingType)
            this.price = listingPrice
            this.startDate = listingStartDate
            this.agentSaveRequest = agentSaveRequest
            this.propertySaveRequest = propertySaveRequest
        }
    }
}



class ListingCreatedEventConverter: Converter<Listing, ListingCreatedEvent> {
    override fun convert(source: Listing): ListingCreatedEvent {
        return ListingCreatedEvent(source.id, source.description.orEmpty(),
            (source.price?.amount?:BigInteger.valueOf(0)).toString())
    }
}
