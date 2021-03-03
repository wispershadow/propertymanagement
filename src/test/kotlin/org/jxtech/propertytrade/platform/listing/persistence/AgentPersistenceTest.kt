package org.jxtech.propertytrade.platform.listing.persistence

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.jxtech.propertytrade.platform.listing.persistence.config.ListingJpaConfig
import org.jxtech.propertytrade.platform.listing.persistence.entity.AgentType
import org.jxtech.propertytrade.platform.listing.persistence.entity.CompanyAgent
import org.jxtech.propertytrade.platform.listing.persistence.entity.PersonalAgent
import org.jxtech.propertytrade.platform.listing.persistence.repository.AgentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

@SpringBootTest(classes = [AgentPersistenceTestConfig::class, ListingJpaConfig::class])
@ExtendWith(SpringExtension::class)
class AgentPersistenceTest {
    @Autowired
    private lateinit var agentRepository: AgentRepository

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun performTest() {
        val companyAgent = CompanyAgent().apply {
            this.agentType = AgentType.FIRM
            this.name = "company1"
            this.description = "whatever"
            this.addressId = 2L
            this.version = 1L
            this.createdBy = "james"
            this.createdOn = LocalDateTime.now()
            this.lastUpdatedBy = "james"
            this.lastUpdatedOn = LocalDateTime.now()
        }
        val companyAgentSaved = agentRepository.save(companyAgent)
        val personalAgent = PersonalAgent().apply {
            this.agentType = AgentType.PERSON
            this.firstName = "james"
            this.lastName = "zhan"
            this.phone = "13004111"
            this.agentCompanyId = companyAgentSaved.id
            this.version = 1L
            this.createdBy = "james"
            this.createdOn = LocalDateTime.now()
            this.lastUpdatedBy = "james"
            this.lastUpdatedOn = LocalDateTime.now()
        }
        agentRepository.save(personalAgent)
        val existingAgentOption = agentRepository.findById(2)
        existingAgentOption.map {
            val pa = it as PersonalAgent
            println(pa.agentType)
            println(pa.firstName)
        }

        val companyAgentOption = agentRepository.findCompanyAgentByName("company1")
        Assertions.assertTrue(companyAgentOption.isPresent)
        val personalAgentOption = agentRepository.findPersonalAgentByName("james", "zhan", null)
        Assertions.assertTrue(personalAgentOption.isPresent)
    }

}

@Configuration
@EnableAutoConfiguration
class AgentPersistenceTestConfig {
}
