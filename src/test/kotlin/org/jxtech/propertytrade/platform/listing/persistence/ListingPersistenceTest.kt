package org.jxtech.propertytrade.platform.listing.persistence

import com.neovisionaries.i18n.CurrencyCode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.jxtech.propertytrade.platform.common.persistence.entity.Price
import org.jxtech.propertytrade.platform.listing.persistence.config.ListingJpaConfig
import org.jxtech.propertytrade.platform.listing.persistence.entity.Listing
import org.jxtech.propertytrade.platform.listing.persistence.entity.ListingPriceAdjustHistory
import org.jxtech.propertytrade.platform.listing.persistence.repository.ListingPriceAdjustHistoryRepository
import org.jxtech.propertytrade.platform.listing.persistence.repository.ListingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest(classes = [ListingPersistenceTestConfig::class, ListingJpaConfig::class])
@ExtendWith(SpringExtension::class)
class ListingPersistenceTest {
    @Autowired
    private lateinit var listingRepository: ListingRepository

    @Autowired
    private lateinit var listingPriceAdjustHistoryRepository: ListingPriceAdjustHistoryRepository

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun performTest() {
        val listing = Listing(
            propertyId = 1,
            agentId = 1
        ).apply {
            this.description = "whatever price"
            this.price = Price().apply {
                this.amount = BigInteger.valueOf(1000)
                this.currency = CurrencyCode.USD
            }
            this.effectiveDateStart = LocalDate.now().minusDays(1)
            this.effectiveDateEnd = LocalDate.now()
            this.version = 1L
            this.createdBy = "james"
            this.createdOn = LocalDateTime.now()
            this.lastUpdatedBy = "james"
            this.lastUpdatedOn = LocalDateTime.now()
        }
        listingRepository.save(listing)
        val existingListingOption = listingRepository.findById(1)
        existingListingOption.map {
            println(it.price?.amount)
        }
        val listingPriceAdjustHistory = ListingPriceAdjustHistory(listingId = 1).apply {
            this.price = Price().apply {
                this.amount = BigInteger.valueOf(500)
                this.currency = CurrencyCode.USD
            }
            this.effectiveDateStart = LocalDate.now().minusDays(5)
            this.effectiveDateEnd = LocalDate.now().minusDays(4)
        }
        listingPriceAdjustHistoryRepository.save(listingPriceAdjustHistory)

    }
}

@Configuration
@EnableAutoConfiguration
class ListingPersistenceTestConfig {
}
