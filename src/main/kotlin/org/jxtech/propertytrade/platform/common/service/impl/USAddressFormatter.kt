package org.jxtech.propertytrade.platform.common.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.jxtech.propertytrade.platform.common.service.AddressFormatter
import org.jxtech.propertytrade.platform.common.service.data.AddressSaveRequest
import org.jxtech.propertytrade.platform.common.service.data.USAddressSaveRequest

class USAddressFormatter: AddressFormatter {
    private val objectMapper = ObjectMapper()

    override fun formatAddress(addressSaveRequest: AddressSaveRequest): String {
        if (addressSaveRequest is USAddressSaveRequest) {
            val dataMap = mapOf("street1" to addressSaveRequest.street1,
                "street2" to addressSaveRequest.street2,
                "street3" to addressSaveRequest.street3)
            return objectMapper.writeValueAsString(dataMap)
        }
        else {
            throw IllegalArgumentException("Only US Address are supported")
        }
    }
}
