package org.jxtech.propertytrade.platform.common.service

import org.jxtech.propertytrade.platform.common.service.data.LocationDto

interface LocationService {
    // a list of location name string, each one separated by ',' which forms a full hierarchy
    // return the locationIds for each name
    fun batchSave(locationNames: List<String>, savedByUser: String): List<List<Long>>

    fun loadLocations(locationIds: List<Long>): List<LocationDto>

    fun loadParentLocations(locationId: Long): List<LocationDto>

    fun loadImmediateSubLocations(locationId: Long): LocationDto

    fun loadSubLocationsWithLevels(locationId: Long): LocationDto
}
