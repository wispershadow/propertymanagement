package org.jxtech.propertytrade.platform.property.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.jxtech.propertytrade.platform.common.persistence.config.CommonJpaConfig
import org.jxtech.propertytrade.platform.common.service.config.CommonServiceConfig
import org.jxtech.propertytrade.platform.common.service.data.AddressSaveRequest
import org.jxtech.propertytrade.platform.common.service.data.USAddressSaveRequest
import org.jxtech.propertytrade.platform.common.util.DataLoadUtil
import org.jxtech.propertytrade.platform.property.persistence.config.PropertyJpaConfig
import org.jxtech.propertytrade.platform.property.service.config.PropertyServiceConfig
import org.jxtech.propertytrade.platform.property.service.data.PropertySaveRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration

@SpringBootTest(classes = [PropertyServiceTestConfig::class,
    PropertyServiceConfig::class, PropertyJpaConfig::class,
    CommonServiceConfig::class, CommonJpaConfig::class])
class PropertyServiceTest {
    @Autowired
    private lateinit var propertyService: PropertyService

    private val objectMapper = ObjectMapper()

    @Test
    fun testBatchSaveProperty() {
        val addressSaveRequests = mapOf<Int, AddressSaveRequest>(
            0 to USAddressSaveRequest("US,New York,New York,Queens,Long Island City,Hunters Point", "11101").apply {
                this.street1 = "22-18 Jackson Avenue"
            },
            1 to USAddressSaveRequest("US,New York,New York,Queens,Briarwood", "11435").apply {
                this.street1 = "85-15 Main Street"
            }
        )

        val saveReqList = DataLoadUtil.loadTestDataAsPojoList("propertySaveRequests1.json", PropertySaveRequest::class.java)
        saveReqList.forEachIndexed {index, propertySaveRequest ->
            val mappedIndex = if (index > 1) {
                1
            }
            else {
                0
            }
            propertySaveRequest.buildingSaveRequest.addressSaveRequest = addressSaveRequests.getValue(mappedIndex)
        }
        propertyService.batchSave(saveReqList, "james")
        val propertyDto1 = propertyService.loadPropertyById(1)
        val propertyDto2 = propertyService.loadPropertyById(2)
        Assertions.assertEquals(propertyDto1.building.id, propertyDto2.building.id)
        val propertyDto3 = propertyService.loadPropertyById(3)
        Assertions.assertFalse(propertyDto1.building.id == propertyDto3.building.id)
        Assertions.assertEquals(propertyDto1.building.address.addressComponents["street1"],
            "22-18 Jackson Avenue")
        Assertions.assertEquals(propertyDto1.building.address.locations.size, 6)
        Assertions.assertEquals(propertyDto3.building.address.addressComponents["street1"],
            "85-15 Main Street")
        Assertions.assertEquals(propertyDto3.building.address.locations.size, 5)
        println(objectMapper.writeValueAsString(propertyDto1.building.address))
        println(objectMapper.writeValueAsString(propertyDto3.building.address))

    }
}

@Configuration
@EnableAutoConfiguration
class PropertyServiceTestConfig {
}
