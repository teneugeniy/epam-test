package test.epam.order.domain.entity.order

import test.epam.order.domain.entity.item.Item
import java.math.BigDecimal
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Embeddable
class AppliedOffer(

    @Column(name = "offer_uid")
    val offerUid: UUID,

    @Column(name = "offer_code")
    val offerCode: String,

    // TODO Extract to a separate class together with amount. AppliedOffer might contain several items with different amounts
    @ManyToOne
    @JoinColumn(name = "item_id")
    val item: Item?,

    @Column(name = "amount")
    val amount: Int,

    @Column(name = "discount")
    val discount: BigDecimal,
)
