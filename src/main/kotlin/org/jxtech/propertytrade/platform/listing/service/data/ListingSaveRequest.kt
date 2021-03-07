package org.jxtech.propertytrade.platform.listing.service.data

import org.jxtech.propertytrade.platform.listing.persistence.entity.ListingOwnerType
import org.jxtech.propertytrade.platform.listing.persistence.entity.ListingType
import org.jxtech.propertytrade.platform.property.service.data.PropertySaveRequest

class ListingSaveRequest {
    lateinit var price: String
    lateinit var startDate: String //yyyy/MM/dd
    lateinit var listingType: ListingType
    lateinit var listingOwnerType: ListingOwnerType
    lateinit var propertySaveRequest: PropertySaveRequest
    lateinit var agentSaveRequest: AgentSaveRequest
    var propertyId: Long = -1
    var agentId: Long = -1

    fun getSearchKey(): String {
        return "${agentId}|${propertyId}"
    }
}
