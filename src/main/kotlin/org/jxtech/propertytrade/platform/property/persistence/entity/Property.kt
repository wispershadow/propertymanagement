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
@Table(name = "M_PROPERTY")
@SequenceGenerator(name = "PROPERTY_ID_KEY", initialValue = 1, allocationSize = 1, sequenceName = "M_PROPERTY_ID_SEQUENCE")
class Property(
    @Column(name = "BUILDING_ID")
    val buildingId: Long = -1
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPERTY_ID_KEY")
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "NAME")
    lateinit var name: String

    @Column(name = "SIZE")
    lateinit var size: String

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "BEDROOM_NUM")
    var bedRoomNumber: Int = 0

    @Column(name = "BATHROOM_NUM")
    var bathRoomNumber: Int = 0

    @Column(name = "TOTAL_ROOM_NUM")
    var totalRoomNumber: Int = 0

    @Column(name = "TAGS")
    var tags: String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    var status: EntityStatus = EntityStatus.ACTIVE

    fun getTagsAsList(): List<String> {
        return tags?.split(",")?.toList()?: emptyList()
    }

    fun setTagsList(tagsList: List<String>) {
        this.tags = tagsList.joinToString(",")
    }
}
