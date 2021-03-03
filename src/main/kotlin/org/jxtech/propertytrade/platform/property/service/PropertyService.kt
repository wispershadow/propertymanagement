package org.jxtech.propertytrade.platform.property.service

import org.jxtech.propertytrade.platform.property.service.data.PropertyDto
import org.jxtech.propertytrade.platform.property.service.data.PropertySaveRequest

interface PropertyService {
    fun batchSave(saveRequests: List<PropertySaveRequest>, savedByUser: String): List<Long>

    fun loadPropertyById(propertyId: Long): PropertyDto
}
