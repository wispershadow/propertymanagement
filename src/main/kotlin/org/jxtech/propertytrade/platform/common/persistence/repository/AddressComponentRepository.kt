package org.jxtech.propertytrade.platform.common.persistence.repository

import org.jxtech.propertytrade.platform.common.persistence.entity.AddressComponent
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface AddressComponentRepository: CrudRepository<AddressComponent, Long> {
    fun findByAddressDetails(addressDetails: String): Optional<AddressComponent>

    fun findByAddressDetailsIn(addressDetailsList: List<String>): List<AddressComponent>
}
