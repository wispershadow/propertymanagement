package org.jxtech.propertytrade.platform.listing.service.data

import org.jxtech.propertytrade.platform.common.service.data.AddressSaveRequest

class AgentSaveRequest {
    lateinit var firstName: String
    var middleName: String? = null
    lateinit var lastName: String
    var phone: String? = null
    lateinit var agentCompanyName: String
    lateinit var addressSaveRequest: AddressSaveRequest
}
