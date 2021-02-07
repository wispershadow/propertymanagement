package org.jxtech.propertytrade.platform.listing.persistence.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.Table

@Entity
@Table(name = "M_AGENT_COMPANY")
@PrimaryKeyJoinColumn(name = "ID")
class CompanyAgent: Agent() {
    @Column(name = "NAME")
    lateinit var name: String

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "ADDRESS_ID")
    var addressId: Long = -1
}
