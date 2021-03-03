package org.jxtech.propertytrade.platform.property.persistence

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.jxtech.propertytrade.platform.property.persistence.config.PropertyJpaConfig
import org.jxtech.propertytrade.platform.property.persistence.entity.Property
import org.jxtech.propertytrade.platform.property.persistence.repository.PropertyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

@SpringBootTest(classes = [PropertyPersistenceTestConfig::class, PropertyJpaConfig::class])
@ExtendWith(SpringExtension::class)
class PropertyPersistenceTest {
    @Autowired
    private lateinit var propertyRepository: PropertyRepository

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun performTest() {
        val property = Property(buildingId = 1).apply {
            this.name = "property1"
            this.description = "whatever price"
            this.size = "400 * 400"
            this.version = 1L
            this.createdBy = "james"
            this.createdOn = LocalDateTime.now()
            this.lastUpdatedBy = "james"
            this.lastUpdatedOn = LocalDateTime.now()
            this.setTagsList(listOf("Big", "comfortable"))
        }
        propertyRepository.save(property)
        val propertyOptional = propertyRepository.findById(1L)
        propertyOptional.map {
            println(it.name)
            println(it.size)
        }
        val propertyList = propertyRepository.findByNameIn(listOf("property1", "property2"))
        Assertions.assertEquals(propertyList.size, 1)
    }
}

@Configuration
@EnableAutoConfiguration
class PropertyPersistenceTestConfig {
}
