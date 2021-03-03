package org.jxtech.propertytrade.platform.property.persistence.repository

import org.jxtech.propertytrade.platform.property.persistence.entity.Building
import org.jxtech.propertytrade.platform.property.persistence.entity.Property
import org.springframework.data.repository.CrudRepository

interface BuildingRepository: CrudRepository<Building, Long> {
    fun findByNameIn(nameList: List<String>): List<Building>
}
