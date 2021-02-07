package org.jxtech.propertytrade.platform.property.persistence.repository

import org.jxtech.propertytrade.platform.property.persistence.entity.Amenity
import org.springframework.data.repository.CrudRepository

interface AmenityRepository: CrudRepository<Amenity, Long> {
}
