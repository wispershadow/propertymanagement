package org.jxtech.propertytrade.platform.common.service.data

import org.jxtech.propertytrade.platform.common.persistence.entity.LocationType

class LocationDto {
    var locationId: Long = 0
    lateinit var locationType: LocationType
    lateinit var locationName: String
    var parentLocation: LocationDto? = null
    var subLocations: List<LocationDto> = emptyList()
}
