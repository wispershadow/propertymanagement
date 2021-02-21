package org.jxtech.propertytrade.platform.common.service

import org.jxtech.propertytrade.platform.common.service.data.AddressComponentDto
import org.jxtech.propertytrade.platform.common.service.data.AddressSaveRequest

interface AddressService {
    fun batchSave(saveRequests: List<AddressSaveRequest>, savedByUser: String): List<Long>

    fun loadAddressById(addressId: Long): AddressComponentDto
}
