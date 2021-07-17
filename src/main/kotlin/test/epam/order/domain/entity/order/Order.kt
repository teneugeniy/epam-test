package test.epam.order.domain.entity.order

import test.epam.order.domain.entity.BaseEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.JoinColumn
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

    @ElementCollection
    @CollectionTable(name = "applied_offers", joinColumns = [JoinColumn(name = "order_id")])
    val appliedOffers: Collection<AppliedOffer>,

    @Column(name = "total_price_to_pay")
    val totalPriceToPay: BigDecimal,
) : BaseEntity()
