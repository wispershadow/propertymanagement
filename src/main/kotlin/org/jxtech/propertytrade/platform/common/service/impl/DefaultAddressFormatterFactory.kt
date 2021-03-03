package org.jxtech.propertytrade.platform.common.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.jxtech.propertytrade.platform.common.service.AddressFormatter
import org.jxtech.propertytrade.platform.common.service.AddressFormatterFactory
import org.jxtech.propertytrade.platform.common.service.data.Schema
import org.jxtech.propertytrade.platform.common.service.data.AddressSaveRequest
import org.jxtech.propertytrade.platform.common.service.data.Column
import org.jxtech.propertytrade.platform.common.service.data.USAddressSaveRequest
import java.lang.IllegalArgumentException

class DefaultAddressFormatterFactory: AddressFormatterFactory {
    private val schemaRepository = mapOf<String, Schema>(
        "US" to Schema(name ="us_address", columns = mutableListOf(
            Column(name = "street1", required = true),
            Column(name = "street2", required = true),
            Column(name = "street3", required = true)
        ))
    )

    private val objectMapper = ObjectMapper()

    override fun createAddressFormatter(addressSaveRequest: AddressSaveRequest): AddressFormatter {
        if (addressSaveRequest is USAddressSaveRequest) {
            return USAddressFormatter()
        }
        throw IllegalArgumentException("Unsupported address save request")
    }

    override fun getAddressSchema(addressSaveRequest: AddressSaveRequest): String {
        if (addressSaveRequest is USAddressSaveRequest) {
            return objectMapper.writeValueAsString(schemaRepository.getValue("US"))
        }
        throw IllegalArgumentException("Unsupported address save request")
    }
}
