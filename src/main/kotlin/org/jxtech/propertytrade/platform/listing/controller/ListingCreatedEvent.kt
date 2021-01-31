package org.jxtech.propertytrade.platform.listing.controller

class ListingCreatedEvent(
    val listingId: Long,
    val description: String,
    val price: String
) {
}
