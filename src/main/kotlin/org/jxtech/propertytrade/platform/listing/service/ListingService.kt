package org.jxtech.propertytrade.platform.listing.service

import org.jxtech.propertytrade.platform.listing.persistence.entity.Listing

interface ListingService {
    fun createListing(listing: Listing): Long
}
