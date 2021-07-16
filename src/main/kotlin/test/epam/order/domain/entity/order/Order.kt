package test.epam.order.domain.entity.order

import test.epam.order.domain.entity.BaseEntity
import test.epam.order.domain.entity.item.Item
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "orders")
class Order(

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = [JoinColumn(name = "order_id")])
    val items: Collection<OrderItem>,

    @Column(name = "time_stamp")
    val timeStamp: LocalDateTime,

    @Column(name = "total_price")
    val totalPrice: BigDecimal,
) : BaseEntity()

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
