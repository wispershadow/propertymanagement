package org.jxtech.propertytrade.platform.property.persistence

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.jxtech.propertytrade.platform.property.persistence.config.PropertyJpaConfig
import org.jxtech.propertytrade.platform.property.persistence.entity.Building
import org.jxtech.propertytrade.platform.property.persistence.entity.BuildingType
import org.jxtech.propertytrade.platform.property.persistence.repository.BuildingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest(classes = [BuildingPersistenceTestConfig::class, PropertyJpaConfig::class])
@ExtendWith(SpringExtension::class)
class BuildingPersistenceTest {
    @Autowired
    private lateinit var buildingRepository: BuildingRepository

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun performTest() {
        val building = Building().apply {
            this.name = "Building1"
            this.addressId = 1L
            this.buildingType = BuildingType.APARTMENT
            this.longitude = BigDecimal.valueOf(12.5)
            this.latitude = BigDecimal.valueOf(31.3)
            this.totalUnits = 5
            this.totalStories = 3
            this.totalFamilyNum = 3
            this.builtYear = 1930
            this.version = 1L
            this.createdBy = "james"
            this.createdOn = LocalDateTime.now()
            this.lastUpdatedBy = "james"
            this.lastUpdatedOn = LocalDateTime.now()
        }
        buildingRepository.save(building)
        val buildingOptional = buildingRepository.findById(1L)
        buildingOptional.map {
            println(it.latitude)
            println(it.longitude)
            println(it.name)
        }
        val buildingList = buildingRepository.findByNameIn(listOf("Building1", "Building2"))
        Assertions.assertEquals(buildingList.size, 1)
    }
}

@Configuration
@EnableAutoConfiguration
class BuildingPersistenceTestConfig {
}
