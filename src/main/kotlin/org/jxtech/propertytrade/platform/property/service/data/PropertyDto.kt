package org.jxtech.propertytrade.platform.property.service.data

class PropertyDto {
    var id: Long = 0
    lateinit var name: String
    var description: String? = null
    lateinit var size: String
    var bedRoomNumber: Int = 0
    var bathRoomNumber: Int = 0
    var totalRoomNumber: Int = 0
    lateinit var building: BuildingDto
}
