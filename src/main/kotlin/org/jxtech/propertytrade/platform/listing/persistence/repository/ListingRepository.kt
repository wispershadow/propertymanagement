package org.jxtech.propertytrade.platform.listing.persistence.repository

import org.jxtech.propertytrade.platform.listing.persistence.entity.Listing
import org.springframework.data.repository.CrudRepository

interface ListingRepository: CrudRepository<Listing, Long>
