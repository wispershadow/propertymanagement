package org.jxtech.propertytrade.platform.property.service.impl

import org.jxtech.propertytrade.platform.common.persistence.BatchMergeContext
import org.jxtech.propertytrade.platform.common.persistence.BatchMergeHelper
import org.jxtech.propertytrade.platform.common.persistence.entity.AddressComponent
import org.jxtech.propertytrade.platform.common.persistence.setCommonFieldsBeforeCreate
import org.jxtech.propertytrade.platform.common.service.AddressService
import org.jxtech.propertytrade.platform.common.service.data.AddressComponentDto
import org.jxtech.propertytrade.platform.common.service.data.AddressSaveRequest
import org.jxtech.propertytrade.platform.common.service.impl.AddressComponentDtoPopulator
import org.jxtech.propertytrade.platform.property.persistence.entity.Building
import org.jxtech.propertytrade.platform.property.persistence.repository.BuildingRepository
import org.jxtech.propertytrade.platform.property.service.BuildingService
import org.jxtech.propertytrade.platform.property.service.data.BuildingDto
import org.jxtech.propertytrade.platform.property.service.data.BuildingSaveRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

class DefaultBuildingService(
    val buildingRepository: BuildingRepository,
    val addressService: AddressService
): BuildingService {
    private val buildingDtoPopulator = BuildingDtoPopulator(addressService)

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DefaultBuildingService::class.java)
        private val CONTEXT_ATTRIBUTE_ADDRESSMAP = "addressMap"
    }


    override fun batchSave(saveRequests: List<BuildingSaveRequest>, savedByUser: String): List<Long> {
        return BatchMergeHelper.performBatchMerge<BuildingSaveRequest, String, Building, Long>(
            saveRequests = saveRequests, savedByUser = savedByUser,
            preMergeHook = this::saveAddresses,
            searchFun = this::searchBuildings,
            searchKeyExtractor = this::extractBuildingSearchKey,
            entityTransformer = this::convertBuildingSaveReqToBuilding,
            entityKeyExtractor = this::extractIdFromBuilding,
            entitySearchKeyExtractor = this::extractNameFromBuilding,
            entitySaveFun = this::saveBuilding
        )
    }

    override fun loadBuildingById(buildingId: Long): BuildingDto {
        val buildingOptional = buildingRepository.findById(buildingId)
        return buildingOptional.map {building ->
            val buildingDto = BuildingDto()
            buildingDtoPopulator.populate(building, buildingDto)
            buildingDto
        }.orElseThrow {
            IllegalArgumentException("No matching found for building: $buildingId")
        }
    }

    private fun saveAddresses(batchMergeContext: BatchMergeContext<BuildingSaveRequest, String, Building, Long>, savedByUser: String) {
        val savedRequestsBySearchKey = batchMergeContext.getSaveRequestsBySearchKey()
        val addressSaveRequests = savedRequestsBySearchKey.map {(key, saveReq) ->
            saveReq.addressSaveRequest
        }
        val addressIds = addressService.batchSave(addressSaveRequests, savedByUser)
        val addressMapWithId = addressIds.mapIndexed { index, addressId ->
            addressSaveRequests[index] to addressId
        }.toMap()
        batchMergeContext.addContextAttribute(CONTEXT_ATTRIBUTE_ADDRESSMAP, addressMapWithId)
    }

    private fun searchBuildings(buildingNamesToSearch: List<String>,
        batchMergeContext: BatchMergeContext<BuildingSaveRequest, String, Building, Long>): List<Building> {
        return buildingRepository.findByNameIn(buildingNamesToSearch)
    }

    private fun extractBuildingSearchKey(buildingSaveRequest: BuildingSaveRequest,
        batchMergeContext: BatchMergeContext<BuildingSaveRequest, String, Building, Long>): String {
        return buildingSaveRequest.addressSaveRequest.getConcatAddressString()
    }

    private fun extractNameFromBuilding(building: Building,
        batchMergeContext: BatchMergeContext<BuildingSaveRequest, String, Building, Long>): String {
        return building.name
    }

    private fun extractIdFromBuilding(building: Building,
        batchMergeContext: BatchMergeContext<BuildingSaveRequest, String, Building, Long>): Long {
        return building.id
    }

    private fun convertBuildingSaveReqToBuilding(buildingName: String, buildingSaveRequest: BuildingSaveRequest,
        batchMergeContext: BatchMergeContext<BuildingSaveRequest, String, Building, Long>, savedByUser: String): Building {
        val addressMapWithId = batchMergeContext.getContextAttribute(CONTEXT_ATTRIBUTE_ADDRESSMAP) as Map<AddressSaveRequest, Long>
        return Building().apply {
            this.name = buildingName
            this.buildingType = buildingSaveRequest.buildingType
            this.description = buildingSaveRequest.buildingDescription
            this.addressId = addressMapWithId.getValue(buildingSaveRequest.addressSaveRequest)
            this.builtYear = buildingSaveRequest.builtYear
            this.totalFamilyNum = buildingSaveRequest.totalFamilyNum
            this.totalStories = buildingSaveRequest.totalStories
            this.totalUnits = buildingSaveRequest.totalUnit
            this.setCommonFieldsBeforeCreate(savedByUser)
        }
    }

    private fun saveBuilding(buildingList: Iterable<Building>,
        batchMergeContext: BatchMergeContext<BuildingSaveRequest, String, Building, Long>): Iterable<Building> {
        return buildingRepository.saveAll(buildingList)
    }
}

class BuildingDtoPopulator(val addressService: AddressService) {
    fun populate(source: Building, target: BuildingDto) {
        target.apply {
            this.id = source.id
            this.name = source.name
            this.description = source.description
            this.buildingType = source.buildingType
            this.totalFamilyNum = source.totalFamilyNum
            this.totalStories = source.totalStories
            this.totalUnits = source.totalUnits
            this.builtYear = source.builtYear
            this.latitude = source.latitude
            this.longitude = source.longitude
            this.address = addressService.loadAddressById(source.addressId)
        }
    }
}
