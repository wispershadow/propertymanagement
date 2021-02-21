package org.jxtech.propertytrade.platform.common.service.impl

import org.jxtech.propertytrade.platform.common.persistence.entity.Location
import org.jxtech.propertytrade.platform.common.persistence.entity.LocationType
import org.jxtech.propertytrade.platform.common.persistence.repository.LocationRepository
import org.jxtech.propertytrade.platform.common.persistence.setCommonFieldsBeforeCreate
import org.jxtech.propertytrade.platform.common.service.LocationService
import org.jxtech.propertytrade.platform.common.service.data.LocationDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Optional

class DefaultLocationService(
    val locationRepository: LocationRepository
): LocationService {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DefaultLocationService::class.java)
    }

    override fun batchSave(locationNames: List<String>, savedByUser: String): List<List<Long>> {
        val locationTempCache = mutableMapOf<String, Location>()
        val nameToIdList = mutableMapOf<String, List<Long>>()
        // remove duplicate first
        locationNames.toSet().forEach {locationName ->
            val locationSegments = locationName.split(",").map {
                it.trim()
            }
            val locationSegmentsNotFound = locationSegments.filter {locationSegment ->
                locationTempCache.containsKey(locationSegment)
            }
            val existingLocationsInDb = locationRepository.findByNameIn(locationSegmentsNotFound)
            val toAddToCache = existingLocationsInDb.map { "${it.name}_${it.type}"  to it }.toMap()
            locationTempCache.putAll(toAddToCache)
            var parentLocationId: Long = -1
            val savedIdList = mutableListOf<Long>()
            locationSegments.forEachIndexed { index, locationSegment ->
                val locationType = mapLocationType(index)
                val locationOptional = Optional.ofNullable(locationTempCache["${locationSegment}_${locationType}"])
                val currentLocation = locationOptional.orElseGet {
                    val locationToSave = Location(locationSegment, parentLocationId, locationType).apply {
                        this.setCommonFieldsBeforeCreate(savedByUser)
                    }
                    val savedLocation = locationRepository.save(locationToSave)
                    locationTempCache["${savedLocation.name}_${savedLocation.type}"] = savedLocation
                    savedLocation
                }
                parentLocationId = currentLocation.id
                savedIdList.add(currentLocation.id)
            }
            nameToIdList[locationName] = savedIdList
        }
        locationTempCache.clear()
        return locationNames.map {locationName ->
            nameToIdList.getValue(locationName)
        }
    }

    override fun loadLocations(locationIds: List<Long>): List<LocationDto> {
        val locations = locationRepository.findAllById(locationIds)
        return locations.map { location ->
            LocationDto().apply {
                this.locationId = location.id
                this.locationType = location.type
                this.locationName = location.name
            }
        }
    }

    override fun loadParentLocations(locationId: Long): List<LocationDto> {
        return emptyList()
    }

    override fun loadImmediateSubLocations(locationId: Long): LocationDto {
        return LocationDto()
    }

    override fun loadSubLocationsWithLevels(locationId: Long): LocationDto {
        return LocationDto()
    }

    private fun mapLocationType(index: Int): LocationType {
        return when (index) {
            0 -> {
                LocationType.COUNTRY
            }
            1 -> {
                LocationType.PROVINCE
            }
            2 -> {
                LocationType.CITY
            }
            3 -> {
                LocationType.AREA
            }
            else -> {
                LocationType.NEIGHBORHOOD
            }
        }
    }
}

