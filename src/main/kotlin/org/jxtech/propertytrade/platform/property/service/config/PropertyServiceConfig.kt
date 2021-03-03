package org.jxtech.propertytrade.platform.property.service.config

import org.jxtech.propertytrade.platform.common.service.AddressService
import org.jxtech.propertytrade.platform.property.persistence.repository.BuildingRepository
import org.jxtech.propertytrade.platform.property.persistence.repository.PropertyRepository
import org.jxtech.propertytrade.platform.property.service.BuildingService
import org.jxtech.propertytrade.platform.property.service.PropertyService
import org.jxtech.propertytrade.platform.property.service.impl.DefaultBuildingService
import org.jxtech.propertytrade.platform.property.service.impl.DefaultPropertyService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PropertyServiceConfig {
    @Bean
    fun propertyService(propertyRepository: PropertyRepository, buildingService: BuildingService): PropertyService {
        return DefaultPropertyService(propertyRepository, buildingService)
    }

    @Bean
    fun buildingService(buildingRepository: BuildingRepository, addressService: AddressService): BuildingService {
        return DefaultBuildingService(buildingRepository, addressService)
    }
}
