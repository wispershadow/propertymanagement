package org.jxtech.propertytrade.platform.property.persistence.repository

import org.jxtech.propertytrade.platform.property.persistence.entity.ImageResource
import org.springframework.data.repository.CrudRepository

interface ImageResourceRepository: CrudRepository<ImageResource, Long> {
}
