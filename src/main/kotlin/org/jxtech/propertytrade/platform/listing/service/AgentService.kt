package org.jxtech.propertytrade.platform.listing.service

import org.jxtech.propertytrade.platform.listing.service.data.AgentSaveRequest

interface AgentService {
    fun batchSave(saveRequests: List<AgentSaveRequest>, savedByUser: String): List<Long>
}
