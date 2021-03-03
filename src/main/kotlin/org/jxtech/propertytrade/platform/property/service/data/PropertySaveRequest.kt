package org.jxtech.propertytrade.platform.property.service.data

class PropertySaveRequest {
    var propertyNo: String = ""
    var propertyDescription: String = ""
    var propertySize: String = ""
    var bedRoomNo: Int = 0
    var bathRoomNo: Int = 0
    var totalRoomNo: Int = 0
    var tags: String = ""
    var buildingId: Long = -1
}
