package org.jxtech.propertytrade.platform.listing.persistence.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@Qualifier("listingJpaConfig")
@EnableJpaRepositories(basePackages = ["org.jxtech.propertytrade.platform.listing.persistence.repository"])
@EntityScan(basePackages = ["org.jxtech.propertytrade.platform.listing.persistence.entity"])
class ListingJpaConfig {

}
