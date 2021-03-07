package org.jxtech.propertytrade.platform.property.persistence.entity

import org.jxtech.propertytrade.platform.common.persistence.entity.BaseEntity
import org.jxtech.propertytrade.platform.common.persistence.entity.EntityStatus
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
@Table(name = "M_AMENITY")
//@SequenceGenerator(name = "AMENITY_ID_KEY", initialValue = 1, allocationSize = 1, sequenceName = "M_AMENITY_ID_SEQUENCE")
class Amenity(): BaseEntity() {
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMENITY_ID_KEY")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "NAME")
    lateinit var name: String

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    lateinit var type: AmenityType

    @Column(name = "CATEGORIES")
    var categories: String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    var status: EntityStatus = EntityStatus.ACTIVE
}

enum class AmenityType {
    BUILDING,
    PROPERTY
}
