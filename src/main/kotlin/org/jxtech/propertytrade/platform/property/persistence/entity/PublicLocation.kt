package org.jxtech.propertytrade.platform.property.persistence.entity

import org.jxtech.propertytrade.platform.common.persistence.entity.BaseEntity
import org.jxtech.propertytrade.platform.common.persistence.entity.EntityStatus
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "M_PUBLIC_LOCATION")
@SequenceGenerator(name = "PUBLIC_LOCATION_ID_KEY", initialValue = 1, allocationSize = 1, sequenceName = "M_PUBLIC_LOCATION_ID_SEQUENCE")
class PublicLocation(): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PUBLIC_LOCATION_ID_KEY")
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "NAME")
    lateinit var name: String

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "ADDRESS_ID")
    var addressId: Long = -1L

    @Column(name = "LONGITUDE")
    var longitude: BigDecimal? = null

    @Column(name = "LATITUDE")
    var latitude: BigDecimal? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    lateinit var type: PublicLocationType

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    var status: EntityStatus = EntityStatus.ACTIVE
}

enum class PublicLocationType {
    PARK
}
