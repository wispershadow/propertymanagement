package org.jxtech.propertytrade.platform.property.persistence

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.jxtech.propertytrade.platform.property.persistence.config.PropertyJpaConfig
import org.jxtech.propertytrade.platform.property.persistence.entity.PublicLocation
import org.jxtech.propertytrade.platform.property.persistence.entity.PublicLocationType
import org.jxtech.propertytrade.platform.property.persistence.repository.PropertyRepository
import org.jxtech.propertytrade.platform.property.persistence.repository.PublicLocationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest(classes = [PublicLocPersistenceTestConfig::class, PropertyJpaConfig::class])
@ExtendWith(SpringExtension::class)
class PublicLocPersistenceTest {

    @Autowired
    private lateinit var publicLocationRepository: PublicLocationRepository

    @Test
    fun performTest() {
        val publicLocation = PublicLocation().apply {
            this.name = "publicLoc1"
            this.description = "Great park"
            this.addressId = 1L
            this.longitude = BigDecimal.valueOf(11.7)
            this.latitude = BigDecimal.valueOf(39.2)
            this.type = PublicLocationType.PARK
            this.version = 1L
            this.createdBy = "james"
            this.createdOn = LocalDateTime.now()
            this.lastUpdatedBy = "james"
            this.lastUpdatedOn = LocalDateTime.now()
        }
        publicLocationRepository.save(publicLocation)
        val publicLocOptional = publicLocationRepository.findById(1L)
        publicLocOptional.map {
            println(it.name)
            println(it.longitude)
            println(it.latitude)
        }
    }
}

@Configuration
@EnableAutoConfiguration
class PublicLocPersistenceTestConfig {
}
