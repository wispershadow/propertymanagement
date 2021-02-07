package org.jxtech.propertytrade.platform.common.persistence

import org.junit.jupiter.api.extension.ExtendWith
import org.jxtech.propertytrade.platform.common.persistence.entity.CommonJpaConfig
import org.jxtech.propertytrade.platform.common.persistence.repository.LocationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(classes = [LocationPersistenceTestConfig::class, CommonJpaConfig::class])
@ExtendWith(SpringExtension::class)
class LocationPersistenceTest {
    @Autowired
    private lateinit var locationRepository: LocationRepository



}

@Configuration
@EnableAutoConfiguration
class LocationPersistenceTestConfig {
}
