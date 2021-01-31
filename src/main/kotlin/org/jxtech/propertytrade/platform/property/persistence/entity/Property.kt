package org.jxtech.propertytrade.platform.property.persistence.entity

import org.jxtech.propertytrade.platform.common.persistence.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "M_PROPERTY")
class Property(
): BaseEntity() {
    @Id
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "NAME")
    lateinit var name: String

    @Column(name = "ADDRESS")
    lateinit var address: String

    @Column(name = "SIZE")
    lateinit var size: String

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "IMAGE_PATH")
    var imagePath: String? = null

    @Column(name = "LOCATION_ID")
    val locationId: Long = -1

    @Column(name = "FULL_LOCATION_PATH")
    var fullLocationPath: String? = null

    @Column(name = "TAGS")
    var tags: String? = null

    fun getTagsAsList(): List<String> {
        return tags?.split(",")?.toList()?: emptyList()
    }

    fun setTagsList(tagsList: List<String>) {
        this.tags = tagsList.joinToString(",")
    }

    fun getFullLocationPathAsList(): List<String> {
        return fullLocationPath?.split(",")?.toList()?: emptyList()
    }

    fun setFullLocationPathList(fullLocationPathList: List<String>) {
        this.fullLocationPath = fullLocationPathList.joinToString(",")
    }
}
