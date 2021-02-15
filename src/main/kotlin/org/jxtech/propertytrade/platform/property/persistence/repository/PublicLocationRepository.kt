package org.jxtech.propertytrade.platform.property.persistence.repository

import org.jxtech.propertytrade.platform.property.persistence.entity.PublicLocation
import org.springframework.data.repository.CrudRepository

interface PublicLocationRepository: CrudRepository<PublicLocation, Long> {
}
