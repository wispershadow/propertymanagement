package org.jxtech.propertytrade.platform.property.persistence.entity

import org.jxtech.propertytrade.platform.common.persistence.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "M_IMAGE_RESOURCE")
@SequenceGenerator(name = "IMAGE_RESOURCE_ID_KEY", initialValue = 1, allocationSize = 1, sequenceName = "M_IMAGE_RESOURCE_ID_SEQUENCE")
class ImageResource(): BaseEntity()  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMAGE_RESOURCE_ID_KEY")
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "NAME")
    lateinit var name: String

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "LABEL")
    var label: String? = null

    @Column(name = "IMAGE_FULL_PATH")
    lateinit var imageFullPath: String

    @Column(name = "BELONG_TO")
    lateinit var belongToEntityType: String

    @Column(name = "REFERENCE_ID")
    var referenceId: Long = -1
}
