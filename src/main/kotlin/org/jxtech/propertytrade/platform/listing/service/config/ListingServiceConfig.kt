package org.jxtech.propertytrade.platform.listing.service.config

import org.jxtech.propertytrade.platform.common.service.AddressService
import org.jxtech.propertytrade.platform.listing.persistence.repository.AgentRepository
import org.jxtech.propertytrade.platform.listing.persistence.repository.ListingRepository
import org.jxtech.propertytrade.platform.listing.service.AgentService
import org.jxtech.propertytrade.platform.listing.service.ListingService
import org.jxtech.propertytrade.platform.listing.service.impl.DefaultAgentService
import org.jxtech.propertytrade.platform.listing.service.impl.DefaultListingService
import org.jxtech.propertytrade.platform.property.service.PropertyService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ListingServiceConfig {
    @Bean
    fun agentService(agentRepository: AgentRepository, addressService: AddressService): AgentService {
        return DefaultAgentService(agentRepository, addressService)
    }

    @Bean
    fun listingService(listingRepository: ListingRepository, agentService: AgentService,
        propertyService: PropertyService): ListingService {
        return DefaultListingService(listingRepository, agentService, propertyService)
    }
}
