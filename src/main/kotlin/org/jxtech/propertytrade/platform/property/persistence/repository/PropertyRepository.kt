package org.jxtech.propertytrade.platform.property.persistence.repository

import org.jxtech.propertytrade.platform.property.persistence.entity.Property
import org.springframework.data.repository.CrudRepository

interface PropertyRepository: CrudRepository<Property, Long> {
}
