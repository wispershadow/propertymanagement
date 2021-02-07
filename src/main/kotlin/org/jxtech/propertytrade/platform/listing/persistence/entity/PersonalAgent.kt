package org.jxtech.propertytrade.platform.listing.persistence.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.Table

@Entity
@Table(name = "M_AGENT_PERSONAL")
@PrimaryKeyJoinColumn(name = "ID")
class PersonalAgent: Agent() {
    @Column(name = "FIRST_NAME")
    lateinit var firstName: String

    @Column(name = "LAST_NAME")
    lateinit var lastName: String

    @Column(name = "MIDDLE_NAME")
    var middleName: String? = null

    @Column(name = "PHONE")
    var phone: String? = null

    @Column(name = "AGENT_COMPANY_ID")
    var agentCompanyId: Long  = -1
}
