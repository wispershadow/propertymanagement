package org.jxtech.propertytrade.platform.listing.service.impl

import org.jxtech.propertytrade.platform.listing.persistence.entity.Listing
import org.jxtech.propertytrade.platform.listing.persistence.repository.ListingRepository
import org.jxtech.propertytrade.platform.listing.service.ListingService

class DefaultListingService(private val listingRepository: ListingRepository): ListingService {
    override fun createListing(listing: Listing): Long {
        val saveResult = listingRepository.save(listing)
        return saveResult.id
    }
}

