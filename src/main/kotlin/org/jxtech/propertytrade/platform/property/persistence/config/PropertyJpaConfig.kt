package org.jxtech.propertytrade.platform.property.persistence.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@Qualifier("propertyJpaConfig")
@EnableJpaRepositories(basePackages = ["org.jxtech.propertytrade.platform.property.persistence.repository"])
@EntityScan(basePackages = ["org.jxtech.propertytrade.platform.property.persistence.entity"])
class PropertyJpaConfig {

}
