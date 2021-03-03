package org.jxtech.propertytrade.platform.property.service.impl

import org.jxtech.propertytrade.platform.property.persistence.entity.Property
import org.jxtech.propertytrade.platform.property.persistence.repository.PropertyRepository
import org.jxtech.propertytrade.platform.property.service.BuildingService
import org.jxtech.propertytrade.platform.property.service.PropertyService
import org.jxtech.propertytrade.platform.property.service.data.PropertyDto
import org.jxtech.propertytrade.platform.property.service.data.PropertySaveRequest

class DefaultPropertyService(
    val propertyRepository: PropertyRepository,
    val buildingService: BuildingService
): PropertyService {
    private val propertyDtoPopulator = PropertyDtoPopulator(buildingService)

    override fun batchSave(saveRequests: List<PropertySaveRequest>, savedByUser: String): List<Long> {
        return emptyList()
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
