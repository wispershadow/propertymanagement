package org.jxtech.propertytrade.platform.common.persistence.repository

import org.jxtech.propertytrade.platform.common.persistence.entity.AddressComponent
import org.springframework.data.repository.CrudRepository

interface AddressComponentRepository: CrudRepository<AddressComponent, Long> {
}
