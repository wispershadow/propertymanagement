package org.jxtech.propertytrade.platform.common.persistence.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "M_ADDRESS_COMPONENT")
@SequenceGenerator(name = "ADDRESS_COMPONENT_KEY", initialValue = 1, allocationSize = 1, sequenceName = "M_ADDRESS_COMPONENT_SEQUENCE")
class AddressComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADDRESS_COMPONENT_KEY")
    @Column(name = "ID")
    open var id: Long = 0

    @Column(name = "ADDRESS_SCHEMA")
    var addressSchema: String? = null

    @Column(name = "REGION")
    var region: String = "US"

    @Column(name = "ADDRESS_DETAILS")
    var addressDetails: String = "{}"

    @Column(name = "POSTAL_CODE")
    var postalCode: String? = null

    @Column(name = "LOCATION_ID")
    var locationId: Long = -1

    @Column(name = "FULL_LOCATION_PATH")
    var fullLocationPath: String? = null

}
