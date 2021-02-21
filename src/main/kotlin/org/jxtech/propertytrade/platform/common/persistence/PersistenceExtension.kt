package org.jxtech.propertytrade.platform.common.persistence

import org.jxtech.propertytrade.platform.common.persistence.entity.BaseEntity
import java.time.LocalDateTime

fun BaseEntity.setCommonFieldsBeforeCreate(savedByUser: String) {
    val saveTime = LocalDateTime.now()
    this.createdOn = saveTime
    this.createdBy = savedByUser
    this.lastUpdatedOn = saveTime
    this.lastUpdatedBy = savedByUser
}

fun BaseEntity.setCommonFieldsBeforeUpdate(savedByUser: String) {
    val saveTime = LocalDateTime.now()
    this.lastUpdatedOn = saveTime
    this.lastUpdatedBy = savedByUser
}
