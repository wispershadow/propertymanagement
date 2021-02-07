package org.jxtech.propertytrade.platform.listing.persistence.repository

import org.jxtech.propertytrade.platform.listing.persistence.entity.ListingPriceAdjustHistory
import org.springframework.data.repository.CrudRepository

interface ListingPriceAdjustHistoryRepository: CrudRepository<ListingPriceAdjustHistory, Long> {
}
