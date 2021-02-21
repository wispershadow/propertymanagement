package org.jxtech.propertytrade.platform.property.service.data

import org.jxtech.propertytrade.platform.common.service.data.AddressSaveRequest
import org.jxtech.propertytrade.platform.property.persistence.entity.BuildingType

class BuildingSaveRequest {
    lateinit var addressSaveRequest: AddressSaveRequest
    var buildingDescription: String = ""
    lateinit var buildingType: BuildingType
    var totalUnit: Int = 0
    var totalStories: Int = 0
    var totalFamilyNum: Int = 0
    var builtYear: Int = 0
}
