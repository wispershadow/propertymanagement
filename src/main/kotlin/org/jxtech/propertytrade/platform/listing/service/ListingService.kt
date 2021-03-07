package org.jxtech.propertytrade.platform.listing.service

import org.jxtech.propertytrade.platform.listing.service.data.ListingSaveRequest

interface ListingService {
    fun batchSave(saveRequests: List<ListingSaveRequest>, savedByUser: String): List<Long>
}
