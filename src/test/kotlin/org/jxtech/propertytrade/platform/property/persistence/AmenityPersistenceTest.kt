package org.jxtech.propertytrade.platform.property.persistence

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.jxtech.propertytrade.platform.property.persistence.config.PropertyJpaConfig
import org.jxtech.propertytrade.platform.property.persistence.entity.Amenity
import org.jxtech.propertytrade.platform.property.persistence.entity.AmenityType
import org.jxtech.propertytrade.platform.property.persistence.repository.AmenityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

@SpringBootTest(classes = [AmenityPersistenceTestConfig::class, PropertyJpaConfig::class])
@ExtendWith(SpringExtension::class)
class AmenityPersistenceTest {
    @Autowired
    private lateinit var amenityRepository: AmenityRepository

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun performTest() {
        val amenity = Amenity().apply {
            this.name = "amenity1"
            this.description = "description1"
            this.type = AmenityType.BUILDING
            this.categories = "HIGHLIGHTS,CO-OP RULES"
            this.version = 1L
            this.createdBy = "james"
            this.createdOn = LocalDateTime.now()
            this.lastUpdatedBy = "james"
            this.lastUpdatedOn = LocalDateTime.now()
        }
        amenityRepository.save(amenity)
        val amenityOptional = amenityRepository.findById(1L)
        amenityOptional.map {
            println(it.name)
            println(it.categories)
        }
    }

}

@Configuration
@EnableAutoConfiguration
class AmenityPersistenceTestConfig {
}
