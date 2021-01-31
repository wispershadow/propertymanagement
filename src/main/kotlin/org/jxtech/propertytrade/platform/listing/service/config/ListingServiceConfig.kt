package org.jxtech.propertytrade.platform.listing.service.config

import org.jxtech.propertytrade.platform.listing.persistence.repository.ListingRepository
import org.jxtech.propertytrade.platform.listing.service.ListingService
import org.jxtech.propertytrade.platform.listing.service.impl.DefaultListingService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ListingServiceConfig {
    @Bean
    fun listingService(listingRepository: ListingRepository): ListingService {
        return DefaultListingService(listingRepository)
    }
}
