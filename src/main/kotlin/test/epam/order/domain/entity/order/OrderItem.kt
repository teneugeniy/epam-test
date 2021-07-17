package test.epam.order.domain.entity.order

import test.epam.order.domain.entity.item.Item
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

// TODO it shouldn't be just price. There should be a history. Orders should reference specific price history items
@Embeddable
class OrderItem(

    @ManyToOne
    @JoinColumn(name = "item_id")
    val item: Item,

    @Column(name = "price")
    val price: BigDecimal,

    @Column(name = "amount")
    val amount: Int,

    @Column(name = "calculated_total")
    val calculatedTotal: BigDecimal
)
