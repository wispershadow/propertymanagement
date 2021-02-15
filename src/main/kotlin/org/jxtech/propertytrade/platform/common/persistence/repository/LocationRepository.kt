package org.jxtech.propertytrade.platform.common.persistence.repository

import org.jxtech.propertytrade.platform.common.persistence.entity.Location
import org.springframework.data.repository.CrudRepository

interface LocationRepository: CrudRepository<Location, Long> {
}
