package org.jxtech.propertytrade.platform.common.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.jxtech.propertytrade.platform.common.persistence.config.CommonJpaConfig
import org.jxtech.propertytrade.platform.common.service.config.CommonServiceConfig
import org.jxtech.propertytrade.platform.common.service.data.USAddressSaveRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(classes = [AddressServiceTestConfig::class,
    CommonServiceConfig::class, CommonJpaConfig::class])
@ExtendWith(SpringExtension::class)
class AddressServiceTest {
    private val objectMapper = ObjectMapper()
    @Autowired
    private lateinit var addressService: AddressService

    @Test
    fun testBatchSaveAddress() {
        val batchSaveAddressRequests = listOf(
            USAddressSaveRequest("US,New York,New York,Queens,Long Island City,Hunters Point", "11101").apply {
                this.street1 = "22-18 Jackson Avenue"
                this.street2 = "GALERIE"
                this.street3 = "Long Island City, NY 11101"
            },
            USAddressSaveRequest("US,New York,New York,Queens,Briarwood", "11435").apply {
                this.street1 = "85-15 Main Street"
                this.street2 = "Jamaica"
                this.street3 = "NY 11435"
            },
            USAddressSaveRequest("US,New York,New York,Queens,Long Island City,Hunters Point", "11101").apply {
                this.street1 = "22-18 Jackson Avenue"
                this.street2 = "GALERIE"
                this.street3 = "Long Island City, NY 11101"
            }
        )
        val addressIds = addressService.batchSave(batchSaveAddressRequests, "james")
        Assertions.assertEquals(addressIds, listOf(1L, 2L, 1L))
        val addressComponentDto = addressService.loadAddressById(1)
        println(objectMapper.writeValueAsString(addressComponentDto))
    }

}

@Configuration
@EnableAutoConfiguration
class AddressServiceTestConfig {

}
