package org.jxtech.propertytrade.platform.common.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.jxtech.propertytrade.platform.common.persistence.BatchMergeContext
import org.jxtech.propertytrade.platform.common.persistence.BatchMergeHelper
import org.jxtech.propertytrade.platform.common.persistence.entity.AddressComponent
import org.jxtech.propertytrade.platform.common.persistence.repository.AddressComponentRepository
import org.jxtech.propertytrade.platform.common.service.AddressFormatterFactory
import org.jxtech.propertytrade.platform.common.service.AddressService
import org.jxtech.propertytrade.platform.common.service.LocationService
import org.jxtech.propertytrade.platform.common.service.data.AddressComponentDto
import org.jxtech.propertytrade.platform.common.service.data.AddressSaveRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

class DefaultAddressService(val addressComponentRepository: AddressComponentRepository,
    val locationService: LocationService,
    val addressFormatterFactory: AddressFormatterFactory): AddressService {

    private val addressComponentDtoPopulator = AddressComponentDtoPopulator(locationService)

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DefaultAddressService::class.java)
        private val CONTEXT_ATTRIBUTE_LOCATIONLIST = "locationList"
    }

    override fun batchSave(saveRequests: List<AddressSaveRequest>, savedByUser: String): List<Long> {
        return BatchMergeHelper.performBatchMerge<AddressSaveRequest, String, AddressComponent, Long>(
            saveRequests = saveRequests, savedByUser = savedByUser,
            preMergeHook = this::saveLocations,
            searchFun = this::searchAddresses,
            searchKeyExtractor = this::extractAddressSearchKey,
            entityTransformer = this::convertAddressSaveReqToAddressComponent,
            entityKeyExtractor = this::extractIdFromAddressComponent,
            entitySearchKeyExtractor = this::extractAddressFromAddressComponent,
            entitySaveFun = this::saveAddressComponent
        )
    }

    override fun loadAddressById(addressId: Long): AddressComponentDto {
        val addressComponentOptional = addressComponentRepository.findById(addressId)
        return addressComponentOptional.map { addressComponent ->
            val addressComponentDto = AddressComponentDto()
            addressComponentDtoPopulator.populate(addressComponent, addressComponentDto)
            addressComponentDto
        }.orElseThrow {
            IllegalArgumentException("No matching found for address: $addressId")
        }
    }

    private fun saveLocations(batchMergeContext: BatchMergeContext<AddressSaveRequest, String, AddressComponent, Long>, savedByUser: String) {
        val locationNames = batchMergeContext.getSaveRequestsBySearchKey().map {
            it.value.locations
        }
        val locations = locationService.batchSave(locationNames, savedByUser)
        batchMergeContext.addContextAttribute(CONTEXT_ATTRIBUTE_LOCATIONLIST, locations)
    }

    private fun searchAddresses(formattedAddressesToSearch: List<String>, batchMergeContext: BatchMergeContext<AddressSaveRequest, String, AddressComponent, Long>): List<AddressComponent> {
        return addressComponentRepository.findByAddressDetailsIn(formattedAddressesToSearch)
    }

    private fun extractAddressSearchKey(addressSaveRequest: AddressSaveRequest, batchMergeContext: BatchMergeContext<AddressSaveRequest, String, AddressComponent, Long>): String {
        return addressFormatterFactory.createAddressFormatter(addressSaveRequest).formatAddress(addressSaveRequest)
    }

    private fun extractAddressFromAddressComponent(addressComponent: AddressComponent,
        batchMergeContext: BatchMergeContext<AddressSaveRequest, String, AddressComponent, Long>): String {
        return addressComponent.addressDetails
    }

    private fun extractIdFromAddressComponent(addressComponent: AddressComponent,
        batchMergeContext: BatchMergeContext<AddressSaveRequest, String, AddressComponent, Long>): Long {
        return addressComponent.id
    }

    private fun convertAddressSaveReqToAddressComponent(formattedAddress: String, addressSaveRequest: AddressSaveRequest,
        batchMergeContext: BatchMergeContext<AddressSaveRequest, String, AddressComponent, Long>, savedByUser: String): AddressComponent {
        val locationList = batchMergeContext.getContextAttribute(CONTEXT_ATTRIBUTE_LOCATIONLIST) as List<List<Long>>
        val locationNameIndex = batchMergeContext.getSearchKeysInSequence().indexOf(formattedAddress)
        val locationIds = locationList[locationNameIndex]
        logger.debug("Creating new address component: address = {}, locationIds = {}", formattedAddress, locationIds)
        return AddressComponent().apply {
            this.addressDetails = formattedAddress
            this.region = "US"
            this.addressSchema = addressFormatterFactory.getAddressSchema(addressSaveRequest)
            this.postalCode = addressSaveRequest.postalCode
            this.locationId  = locationIds.last()
            this.fullLocationPath = locationIds.joinToString(separator = ",")
        }
    }

    private fun saveAddressComponent(addressComponentList: Iterable<AddressComponent>,
        batchMergeContext: BatchMergeContext<AddressSaveRequest, String, AddressComponent, Long>): Iterable<AddressComponent> {
        return addressComponentRepository.saveAll(addressComponentList)
    }
}

class AddressComponentDtoPopulator(val locationService: LocationService) {
    private val objectMapper = ObjectMapper()
    fun populate(source: AddressComponent, target: AddressComponentDto) {
        target.apply {
            this.id = source.id
            this.postalCode = source.postalCode
            this.addressComponents = objectMapper.readValue(source.addressDetails, Map::class.java) as Map<String, Any>
            source.fullLocationPath?.let { locationPath ->
                val locationIdList = locationPath.split(",").map {
                    it.toLong()
                }
                this.locations = locationService.loadLocations(locationIdList)
            }
        }
    }
}
