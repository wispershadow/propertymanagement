package org.jxtech.propertytrade.platform.common.persistence.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass
open class BaseEntity {
    @Version
    @Column(name = "VERSION")
    open var version: Long = 1

    @CreatedDate
    @Column(name = "CREATED_ON", columnDefinition = "TIMESTAMP")
    open lateinit var createdOn: LocalDateTime

    @Column(name = "CREATED_BY")
    open lateinit var createdBy: String

    @LastModifiedDate
    @Column(name = "LAST_UPDATED_ON", columnDefinition = "TIMESTAMP")
    open lateinit var lastUpdatedOn: LocalDateTime

    @Column(name = "LAST_UPDATED_BY")
    open lateinit var lastUpdatedBy: String

}
