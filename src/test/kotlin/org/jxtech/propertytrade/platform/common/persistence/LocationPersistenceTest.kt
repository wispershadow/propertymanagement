package org.jxtech.propertytrade.platform.common.persistence

import org.junit.jupiter.api.extension.ExtendWith
import org.jxtech.propertytrade.platform.common.persistence.config.CommonJpaConfig
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(classes = [LocationServiceTestConfig::class, CommonJpaConfig::class])
@ExtendWith(SpringExtension::class)
class LocationPersistenceTest {
}

@Configuration
@EnableAutoConfiguration
class LocationServiceTestConfig {
}
