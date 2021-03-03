package org.jxtech.propertytrade.platform.common.service.config

import org.jxtech.propertytrade.platform.common.persistence.repository.AddressComponentRepository
import org.jxtech.propertytrade.platform.common.persistence.repository.LocationRepository
import org.jxtech.propertytrade.platform.common.service.AddressFormatterFactory
import org.jxtech.propertytrade.platform.common.service.AddressService
import org.jxtech.propertytrade.platform.common.service.LocationService
import org.jxtech.propertytrade.platform.common.service.impl.DefaultAddressFormatterFactory
import org.jxtech.propertytrade.platform.common.service.impl.DefaultAddressService
import org.jxtech.propertytrade.platform.common.service.impl.DefaultLocationService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommonServiceConfig {
    @Bean
    fun locationService(locationRepository: LocationRepository): LocationService {
        return DefaultLocationService(locationRepository)
    }

    @Bean
    fun addressFormatterFactory(): AddressFormatterFactory {
        return DefaultAddressFormatterFactory()
    }

    @Bean
    fun addressService(addressComponentRepository: AddressComponentRepository,
        locationService: LocationService,
        addressFormatterFactory: AddressFormatterFactory): AddressService {
        return DefaultAddressService(addressComponentRepository, locationService, addressFormatterFactory)
    }
}
