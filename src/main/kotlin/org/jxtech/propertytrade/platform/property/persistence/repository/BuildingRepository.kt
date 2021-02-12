package org.jxtech.propertytrade.platform.property.persistence.repository

import org.jxtech.propertytrade.platform.property.persistence.entity.Building
import org.springframework.data.repository.CrudRepository

interface BuildingRepository: CrudRepository<Building, Long> {
}
