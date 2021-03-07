package org.jxtech.propertytrade.platform.common.persistence.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "M_LOCATION_HIERARCHY")
//@SequenceGenerator(name = "LOCATION_ID_KEY", initialValue = 1, allocationSize = 1, sequenceName = "M_LOCATION_HIERARCHY_ID_SEQUENCE")
class Location(
    @Column(name = "NAME")
    val name: String,

    @Column(name = "PARENT_ID")
    val parentId: Long = -1,

    @Column(name = "TYPE")
    val type: LocationType
): BaseEntity() {
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATION_ID_KEY")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "DATA")
    var data: String? = null
}

enum class LocationType {
    COUNTRY,
    PROVINCE,
    CITY,
    AREA,
    NEIGHBORHOOD
}
