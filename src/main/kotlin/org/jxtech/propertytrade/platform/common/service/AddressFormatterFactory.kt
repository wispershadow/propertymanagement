package org.jxtech.propertytrade.platform.common.service

import org.jxtech.propertytrade.platform.common.service.data.AddressSaveRequest

interface AddressFormatterFactory {
    fun createAddressFormatter(addressSaveRequest: AddressSaveRequest): AddressFormatter

    fun getAddressSchema(addressSaveRequest: AddressSaveRequest): String
}
