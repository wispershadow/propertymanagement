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
@Table(name = "M_BUILDING")
@SequenceGenerator(name = "BUILDING_ID_KEY", initialValue = 1, allocationSize = 1, sequenceName = "M_BUILDING_ID_SEQUENCE")
class Building(): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUILDING_ID_KEY")
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "NAME")
    lateinit var name: String

    @Column(name = "ADDRESS_ID")
    var addressId: Long = -1L

    @Enumerated(EnumType.STRING)
    @Column(name = "BUILDING_TYPE")
    lateinit var buildingType: BuildingType

    @Column(name = "LONGITUDE")
    var longitude: BigDecimal? = null

    @Column(name = "LATITUDE")
    var latitude: BigDecimal? = null

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "TOTAL_UNITS")
    var totalUnits: Int = 0

    @Column(name = "TOTAL_STORIES")
    var totalStories: Int = 0

    @Column(name = "TOTAL_FAMILY_NUM")
    var totalFamilyNum: Int = 0

    @Column(name = "BUILT_YEAR")
    var builtYear: Int = 0

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    var status: EntityStatus = EntityStatus.ACTIVE
}

enum class BuildingType {
    SINGLE_HOUSE,
    TOWN_HOUSE,
    APARTMENT
}
