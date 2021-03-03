package org.jxtech.propertytrade.platform.property.service.impl

import org.jxtech.propertytrade.platform.common.persistence.BatchMergeContext
import org.jxtech.propertytrade.platform.common.persistence.BatchMergeHelper
import org.jxtech.propertytrade.platform.common.persistence.setCommonFieldsBeforeCreate
import org.jxtech.propertytrade.platform.property.persistence.entity.Property
import org.jxtech.propertytrade.platform.property.persistence.repository.PropertyRepository
import org.jxtech.propertytrade.platform.property.service.BuildingService
import org.jxtech.propertytrade.platform.property.service.PropertyService
import org.jxtech.propertytrade.platform.property.service.data.BuildingSaveRequest
import org.jxtech.propertytrade.platform.property.service.data.PropertyDto
import org.jxtech.propertytrade.platform.property.service.data.PropertySaveRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DefaultPropertyService(
    val propertyRepository: PropertyRepository,
    val buildingService: BuildingService
): PropertyService {
    private val propertyDtoPopulator = PropertyDtoPopulator(buildingService)

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DefaultPropertyService::class.java)
        private val CONTEXT_ATTRIBUTE_BUILDINGMAP = "buildingMap"
    }

    override fun batchSave(saveRequests: List<PropertySaveRequest>, savedByUser: String): List<Long> {
        return BatchMergeHelper.performBatchMerge<PropertySaveRequest, String, Property, Long>(
            saveRequests = saveRequests, savedByUser = savedByUser,
            preMergeHook = this::saveBuildings,
            searchFun = this::searchProperties,
            searchKeyExtractor = this::extractPropertySearchKey,
            entityTransformer = this::convertPropertySaveReqToProperty,
            entityKeyExtractor = this::extractIdFromProperty,
            entitySearchKeyExtractor = this::extractNameFromProperty,
            entitySaveFun = this::saveProperty
        )
    }

    override fun loadPropertyById(propertyId: Long): PropertyDto {
        val propertyOptional = propertyRepository.findById(propertyId)
        return propertyOptional.map {property ->
            val propertyDto = PropertyDto()
            propertyDtoPopulator.populate(property, propertyDto)
            propertyDto
        }.orElseThrow {
            IllegalArgumentException("No matching found for property: $propertyId")
        }
    }

    private fun saveBuildings(batchMergeContext: BatchMergeContext<PropertySaveRequest, String, Property, Long>, savedByUser: String) {
        val savedRequestsBySearchKey = batchMergeContext.getSaveRequestsBySearchKey()
        val buildingSaveRequests = savedRequestsBySearchKey.map {(key, saveReq) ->
            saveReq.buildingSaveRequest
        }
        val buildingIds = buildingService.batchSave(buildingSaveRequests, savedByUser)
        val buildingMapWithId = buildingIds.mapIndexed { index, buildingId ->
            buildingSaveRequests[index] to buildingId
        }.toMap()
        batchMergeContext.addContextAttribute(CONTEXT_ATTRIBUTE_BUILDINGMAP, buildingMapWithId)
    }


    private fun searchProperties(propertyNamesToSearch: List<String>,
        batchMergeContext: BatchMergeContext<PropertySaveRequest, String, Property, Long>): List<Property> {
        return propertyRepository.findByNameIn(propertyNamesToSearch)
    }

    private fun extractPropertySearchKey(propertySaveRequest: PropertySaveRequest,
        batchMergeContext: BatchMergeContext<PropertySaveRequest, String, Property, Long>): String {
        return propertySaveRequest.getFullName()
    }

    private fun extractNameFromProperty(property: Property,
        batchMergeContext: BatchMergeContext<PropertySaveRequest, String, Property, Long>): String {
        return property.name
    }

    private fun extractIdFromProperty(property: Property,
        batchMergeContext: BatchMergeContext<PropertySaveRequest, String, Property, Long>): Long {
        return property.id
    }

    private fun convertPropertySaveReqToProperty(propertyName: String, propertySaveRequest: PropertySaveRequest,
        batchMergeContext: BatchMergeContext<PropertySaveRequest, String, Property, Long>, savedByUser: String): Property {
        val buildingMapWithId = batchMergeContext.getContextAttribute(CONTEXT_ATTRIBUTE_BUILDINGMAP) as Map<BuildingSaveRequest, Long>
        return Property(buildingId = buildingMapWithId.getValue(propertySaveRequest.buildingSaveRequest)).apply {
            this.name = propertyName
            this.description = propertySaveRequest.propertyDescription
            this.size = propertySaveRequest.propertySize
            this.bedRoomNumber = propertySaveRequest.bedRoomNo
            this.bathRoomNumber = propertySaveRequest.bathRoomNo
            this.totalRoomNumber = propertySaveRequest.totalRoomNo
            this.tags = propertySaveRequest.tags
            this.setCommonFieldsBeforeCreate(savedByUser)
        }
    }

    private fun saveProperty(propertyList: Iterable<Property>,
        batchMergeContext: BatchMergeContext<PropertySaveRequest, String, Property, Long>): Iterable<Property> {
        return propertyRepository.saveAll(propertyList)
    }
}

class PropertyDtoPopulator(val buildingService: BuildingService) {
    fun populate(source: Property, target: PropertyDto) {
        target.id = source.id
        target.name = source.name
        target.description = source.description
        target.size = source.size
        target.totalRoomNumber = source.totalRoomNumber
        target.bedRoomNumber = source.bedRoomNumber
        target.bathRoomNumber = source.bathRoomNumber
        target.building = buildingService.loadBuildingById(source.buildingId)
    }
}
