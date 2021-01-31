package org.jxtech.propertytrade.platform.common.persistence.entity

import com.neovisionaries.i18n.CurrencyCode
import java.math.BigInteger
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class Price {
    @Column(name="AMOUNT")
    lateinit var amount: BigInteger

    @Enumerated(EnumType.STRING)
    @Column(name="CURRENCY")
    lateinit var currency: CurrencyCode
}
