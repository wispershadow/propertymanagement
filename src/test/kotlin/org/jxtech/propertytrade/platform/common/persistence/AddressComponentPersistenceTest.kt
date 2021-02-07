package org.jxtech.propertytrade.platform.common.persistence

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.jxtech.propertytrade.platform.common.persistence.entity.AddressComponent
import org.jxtech.propertytrade.platform.common.persistence.entity.CommonJpaConfig
import org.jxtech.propertytrade.platform.common.persistence.repository.AddressComponentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(classes = [AddressComponentPersistenceTestConfig::class, CommonJpaConfig::class])
@ExtendWith(SpringExtension::class)
class AddressComponentPersistenceTest {
    @Autowired
    private lateinit var addressComponentRepository: AddressComponentRepository

    @Test
    fun testAddressComponent() {
        val addressComponent = AddressComponent().apply {
            this.region = "US"
            this.locationId = 3
            this.addressSchema = "{}"
            this.addressDetails = "{\"street1\":\"abc\", \"street2\":\"1s\", \"street3\":\"4222\"}"
            this.fullLocationPath = "1,2,3"
        }
        addressComponentRepository.save(addressComponent)
        
    }


}

@Configuration
@EnableAutoConfiguration
class AddressComponentPersistenceTestConfig {
}
