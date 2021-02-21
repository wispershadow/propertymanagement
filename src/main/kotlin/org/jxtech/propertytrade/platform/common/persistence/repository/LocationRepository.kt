package org.jxtech.propertytrade.platform.common.persistence.repository

import org.jxtech.propertytrade.platform.common.persistence.entity.Location
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface LocationRepository: CrudRepository<Location, Long> {
    fun findByName(name: String): Optional<Location>

    fun findByNameIn(names: List<String>): List<Location>
}
