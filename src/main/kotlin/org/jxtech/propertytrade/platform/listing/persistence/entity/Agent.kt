package org.jxtech.propertytrade.platform.listing.persistence.entity

import org.jxtech.propertytrade.platform.common.persistence.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "M_AGENT")
@SequenceGenerator(name = "AGENT_ID_KEY", initialValue = 1, allocationSize = 1, sequenceName = "M_AGENT_ID_SEQUENCE")
abstract class Agent: BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGENT_ID_KEY")
    @Column(name = "ID")
    open var id: Long = 0

    @Enumerated(EnumType.STRING)
    @Column(name = "AGENCY_TYPE")
    open var agentType: AgentType = AgentType.FIRM
}

enum class AgentType {
    FIRM,
    PERSON
}
