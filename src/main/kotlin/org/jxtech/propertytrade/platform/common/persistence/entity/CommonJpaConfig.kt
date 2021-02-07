package org.jxtech.propertytrade.platform.common.persistence.entity

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@Qualifier("commonJpaConfig")
@EnableJpaRepositories(basePackages = ["org.jxtech.propertytrade.platform.common.persistence.repository"])
@EntityScan(basePackages = ["org.jxtech.propertytrade.platform.common.persistence.entity"])
class CommonJpaConfig {
}
