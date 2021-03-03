package org.jxtech.propertytrade.platform.common.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.jxtech.propertytrade.platform.common.persistence.config.CommonJpaConfig
import org.jxtech.propertytrade.platform.common.service.config.CommonServiceConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(classes = [LocationServiceTestConfig::class,
    CommonServiceConfig::class, CommonJpaConfig::class])
@ExtendWith(SpringExtension::class)
class LocationServiceTest {
    @Autowired
    private lateinit var locationService: LocationService

    @Test
    fun testBatchSaveLocation() {
        val locationNamesList = listOf("US,New York,New York,Queens,Astoria,Ditmars-Steinway",
            "US,New York,New York,Queens,Long Island City,Hunters Point")
        val savedLocationIdList = locationService.batchSave(locationNamesList, "user1")
        Assertions.assertEquals(savedLocationIdList, listOf(listOf(1L, 2L, 3L, 4L, 5L, 6L), listOf(1L, 2L, 3L, 4L, 7L, 8L)))
        val allLocationIds = mutableSetOf<Long>()
        savedLocationIdList.forEach {locationIds ->
            allLocationIds.addAll(locationIds)
        }
        val locationDtoList = locationService.loadLocations(allLocationIds.toList())
        locationDtoList.forEach {locationDto ->
            println("${locationDto.locationName}_${locationDto.locationType}")
        }
    }
}

@Configuration
@EnableAutoConfiguration
class LocationServiceTestConfig {
}
