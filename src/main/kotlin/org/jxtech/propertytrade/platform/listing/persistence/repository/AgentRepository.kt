package org.jxtech.propertytrade.platform.listing.persistence.repository

import org.jxtech.propertytrade.platform.listing.persistence.entity.Agent
import org.springframework.data.repository.CrudRepository

interface AgentRepository: CrudRepository<Agent, Long>
