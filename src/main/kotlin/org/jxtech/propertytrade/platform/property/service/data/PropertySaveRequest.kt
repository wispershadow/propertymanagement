package org.jxtech.propertytrade.platform.property.service.data

class PropertySaveRequest {
    lateinit var buildingSaveRequest: BuildingSaveRequest
    var propertyNo: String = ""
    var propertyDescription: String = ""
    var propertySize: String = ""
    var bedRoomNo: Int = 0
    var bathRoomNo: Int = 0
    var totalRoomNo: Int = 0
    var tags: String = ""

    fun getFullName(): String {
        return "${propertyNo}-${buildingSaveRequest.getFullName()}"
    }
}
