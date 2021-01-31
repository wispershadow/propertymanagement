package org.jxtech.propertytrade.platform.listing.service

import org.jxtech.propertytrade.platform.listing.controller.ListingCreatedEvent

interface ListingEventListener {
    fun onListingPosted(listingCreatedEvent: ListingCreatedEvent)
}
