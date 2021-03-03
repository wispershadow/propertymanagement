package org.jxtech.propertytrade.platform.listing.service.impl

import com.neovisionaries.i18n.CurrencyCode
import org.jxtech.propertytrade.platform.common.persistence.BatchMergeContext
import org.jxtech.propertytrade.platform.common.persistence.BatchMergeHelper
import org.jxtech.propertytrade.platform.common.persistence.entity.Price
import org.jxtech.propertytrade.platform.common.persistence.setCommonFieldsBeforeCreate
import org.jxtech.propertytrade.platform.listing.persistence.entity.Listing
import org.jxtech.propertytrade.platform.listing.persistence.repository.ListingRepository
import org.jxtech.propertytrade.platform.listing.service.AgentService
import org.jxtech.propertytrade.platform.listing.service.ListingService
import org.jxtech.propertytrade.platform.listing.service.data.ListingSaveRequest
import org.jxtech.propertytrade.platform.property.service.PropertyService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DefaultListingService(private val listingRepository: ListingRepository,
    private val agentService: AgentService,
    private val propertyService: PropertyService): ListingService {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DefaultListingService::class.java)
        private val listingDateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    }

    override fun batchSave(saveRequests: List<ListingSaveRequest>, savedByUser: String): List<Long> {
        populateListingSaveReqWithAgentIdPropId(saveRequests, savedByUser)
        return BatchMergeHelper.performBatchMerge<ListingSaveRequest, String, Listing, Long>(
            saveRequests = saveRequests, savedByUser = savedByUser,
            searchFun = this::searchListings,
            searchKeyExtractor = this::extractListingSearchKey,
            entityTransformer = this::convertListingSaveReqToListing,
            entityKeyExtractor = this::extractIdFromListing,
            entitySearchKeyExtractor = this::extractNameFromListing,
            entitySaveFun = this::saveListing
        )
    }

    private fun populateListingSaveReqWithAgentIdPropId(saveRequests: List<ListingSaveRequest>, savedByUser: String) {
        val propertySaveRequests = saveRequests.map {saveReq ->
            saveReq.propertySaveRequest
        }


        val propertyIds = propertyService.batchSave(propertySaveRequests, savedByUser)
        propertyIds.forEachIndexed { index, propertyId ->
            saveRequests[index].propertyId = propertyId
        }

        val agentSaveRequests = saveRequests.map {saveReq ->
            saveReq.agentSaveRequest
        }
        val agentIds = agentService.batchSave(agentSaveRequests, savedByUser)
        agentIds.forEachIndexed { index, agentId ->
            saveRequests[index].agentId = agentId
        }
    }


    private fun searchListings(listingNamesToSearch: List<String>,
        batchMergeContext: BatchMergeContext<ListingSaveRequest, String, Listing, Long>): List<Listing> {
        //search listing by composite key  with agentId + propertyId
        val resultList = mutableListOf<Listing>()
        listingNamesToSearch.map {listingName ->
            val listingNameParts = listingName.split("|")
            val agentId = listingNameParts[0].toLong()
            val propertyId = listingNameParts[1].toLong()
            val listingOptional = listingRepository.findByAgentIdAndPropertyId(agentId, propertyId)
            if (listingOptional.isPresent) {
                resultList.add(listingOptional.get())
            }
        }
        return resultList
    }

    private fun extractListingSearchKey(listingSaveRequest: ListingSaveRequest,
        batchMergeContext: BatchMergeContext<ListingSaveRequest, String, Listing, Long>): String {
        return listingSaveRequest.getSearchKey()
    }

    private fun extractNameFromListing(listing: Listing,
        batchMergeContext: BatchMergeContext<ListingSaveRequest, String, Listing, Long>): String {
        return "${listing.agentId}|${listing.propertyId}"
    }

    private fun extractIdFromListing(listing: Listing,
        batchMergeContext: BatchMergeContext<ListingSaveRequest, String, Listing, Long>): Long {
        return listing.id
    }

    private fun convertListingSaveReqToListing(listingKey: String, listingSaveRequest: ListingSaveRequest,
        batchMergeContext: BatchMergeContext<ListingSaveRequest, String, Listing, Long>, savedByUser: String): Listing {
        return Listing(propertyId = listingSaveRequest.propertyId, agentId = listingSaveRequest.agentId).apply {
            this.listingType = listingSaveRequest.listingType
            this.listingOwnerType = listingSaveRequest.listingOwnerType
            this.price = Price().apply {
                this.amount = BigDecimal(listingSaveRequest.price).unscaledValue()
                this.currency = CurrencyCode.USD
            }
            this.effectiveDateStart = LocalDate.from(listingDateFormat.parse(listingSaveRequest.startDate))
            this.setCommonFieldsBeforeCreate(savedByUser)
        }
    }

    private fun saveListing(listingList: Iterable<Listing>,
        batchMergeContext: BatchMergeContext<ListingSaveRequest, String, Listing, Long>): Iterable<Listing> {
        return listingRepository.saveAll(listingList)
    }
}

