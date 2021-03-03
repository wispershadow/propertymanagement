package org.jxtech.propertytrade.platform.common.service.data

open class AddressSaveRequest(val locations: String = "",
    var postalCode: String?) {
    open fun getConcatAddressString(): String {
        return ""
    }
}
