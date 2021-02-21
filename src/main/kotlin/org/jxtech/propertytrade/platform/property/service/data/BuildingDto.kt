package org.jxtech.propertytrade.platform.property.service.data

import org.jxtech.propertytrade.platform.common.service.data.AddressComponentDto
import org.jxtech.propertytrade.platform.property.persistence.entity.BuildingType
import java.math.BigDecimal

class BuildingDto {
    var id: Long = 0
    lateinit var name: String
    var description: String? = null
    var totalUnits: Int = 0
    var totalStories: Int = 0
    var totalFamilyNum: Int = 0
    var builtYear: Int = 0
    lateinit var buildingType: BuildingType
    lateinit var address: AddressComponentDto
    var longitude: BigDecimal? = null
    var latitude: BigDecimal? = null
}
