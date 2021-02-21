package org.jxtech.propertytrade.platform.property.service

import org.jxtech.propertytrade.platform.property.service.data.BuildingDto
import org.jxtech.propertytrade.platform.property.service.data.BuildingSaveRequest

interface BuildingService {
    fun batchSave(saveRequests: List<BuildingSaveRequest>, savedByUser: String): List<Long>

    fun loadBuildingById(buildingId: Long): BuildingDto
}
