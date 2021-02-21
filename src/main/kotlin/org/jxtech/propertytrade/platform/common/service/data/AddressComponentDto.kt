package org.jxtech.propertytrade.platform.common.service.data

class AddressComponentDto {
    var id: Long = 0
    var addressComponents: Map<String, Any> = emptyMap()
    var postalCode: String? = null
    var locations: List<LocationDto> = emptyList()
}
