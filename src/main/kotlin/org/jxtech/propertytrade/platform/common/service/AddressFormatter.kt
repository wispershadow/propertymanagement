package org.jxtech.propertytrade.platform.common.service

import org.jxtech.propertytrade.platform.common.service.data.AddressSaveRequest

interface AddressFormatter {
    fun formatAddress(addressSaveRequest: AddressSaveRequest): String
}
