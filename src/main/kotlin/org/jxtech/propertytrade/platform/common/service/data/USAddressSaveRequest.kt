package org.jxtech.propertytrade.platform.common.service.data

class USAddressSaveRequest(locations: String, postalCode: String?): AddressSaveRequest(locations, postalCode) {
    var street1: String = ""
    var street2: String = ""
    var street3: String = ""

    override fun getConcatAddressString(): String {
        return "${street1} ${street2} ${street3}"
    }
}
