package org.jxtech.propertytrade.platform.listing.persistence.repository

import org.jxtech.propertytrade.platform.listing.persistence.entity.Listing
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface ListingRepository: CrudRepository<Listing, Long> {
    fun findByAgentIdAndPropertyId(agentId: Long, propertyId: Long): Optional<Listing>
}
