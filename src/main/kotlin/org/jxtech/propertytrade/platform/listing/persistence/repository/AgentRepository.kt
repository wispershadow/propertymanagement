package org.jxtech.propertytrade.platform.listing.persistence.repository

import org.jxtech.propertytrade.platform.listing.persistence.entity.Agent
import org.jxtech.propertytrade.platform.listing.persistence.entity.CompanyAgent
import org.jxtech.propertytrade.platform.listing.persistence.entity.PersonalAgent
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.Optional

interface AgentRepository: CrudRepository<Agent, Long> {
    @Query("from PersonalAgent pa where pa.firstName=:firstName and pa.lastName=:lastName and (pa.middleName is null or pa.middleName=:middleName)")
    fun findPersonalAgentByName(@Param("firstName") firstName: String,
        @Param("lastName") lastName: String,
        @Param("middleName") middleName: String?): Optional<PersonalAgent>

    @Query("from CompanyAgent ca where ca.name=:companyName")
    fun findCompanyAgentByName(
        @Param("companyName") companyName: String): Optional<CompanyAgent>
}
