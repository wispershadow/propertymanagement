package org.jxtech.propertytrade.platform.listing.persistence.entity

import org.jxtech.propertytrade.platform.common.persistence.entity.Price
import java.time.LocalDate
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "T_LISTING_PRICE_ADJUST_HISTORY")
//@SequenceGenerator(name = "LISTING_PRICE_ADJUST_HISTORY_KEY", initialValue = 1, allocationSize = 1, sequenceName = "T_LISTING_PRICE_ADJUST_HISTORY_ID_SEQUENCE")
class ListingPriceAdjustHistory(
    @Column(name = "LISTING_ID")
    val listingId: Long = -1
) {
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LISTING_ID_KEY")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name="amount", column = Column(name = "LISTING_PRICE")),
        AttributeOverride(name="currency", column = Column(name = "LISTING_CURRENCY"))
    )
    lateinit var price: Price

    @Column(name = "EFFECTIVE_DATE_START", columnDefinition = "DATE")
    lateinit var effectiveDateStart: LocalDate

    @Column(name = "EFFECTIVE_DATE_END", columnDefinition = "DATE")
    lateinit var effectiveDateEnd: LocalDate

}
