package org.jxtech.propertytrade.platform.listing.persistence.entity

import org.jxtech.propertytrade.platform.common.persistence.entity.BaseEntity
import org.jxtech.propertytrade.platform.common.persistence.entity.Price
import java.time.LocalDate
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "T_LISTING")
@SequenceGenerator(name = "LISTING_ID_KEY", initialValue = 1, allocationSize = 1, sequenceName = "T_LISTING_ID_SEQUENCE")
class Listing(
    @Column(name = "PROPERTY_ID")
    val propertyId: Long = -1,

    @Column(name = "AGENT_ID")
    val agentId: Long = -1
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LISTING_ID_KEY")
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name="amount", column = Column(name = "LISTING_PRICE")),
        AttributeOverride(name="currency", column = Column(name = "LISTING_CURRENCY"))
    )
    lateinit var price: Price

    @Column(name = "EFFECTIVE_DATE_START", columnDefinition = "DATE")
    lateinit var effectiveDateStart: LocalDate

    @Column(name = "EFFECTIVE_DATE_END", columnDefinition = "DATE")
    var effectiveDateEnd: LocalDate? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "LISTING_TYPE")
    var listingType: ListingType = ListingType.RENTAL

    @Enumerated(EnumType.STRING)
    @Column(name = "OWNER_TYPE")
    var listingOwnerType: ListingOwnerType? = null

    @Column(name = "SOURCE")
    var source: String? = null

    @Column(name = "STATUS")
    var status: ListingStatus = ListingStatus.OPEN
}

enum class ListingType {
    RENTAL,
    SALE,
    BOTH
}

enum class ListingOwnerType {
    INDIVIDUAL,
    COMPANY,
    SPONSOR
}

enum class ListingStatus {
    OPEN,
    INACTIVE,
    SUCCESS,
    FAIL
}
