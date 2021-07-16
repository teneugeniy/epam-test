package test.epam.order.domain.entity.item

import test.epam.order.domain.entity.BaseEntity
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

// TODO it shouldn't be just price. There should be a history. Orders should reference specific price history items
@Entity
@Table(name = "items")
class Item(
    @Column(name = "name", unique = true)
    var name: String,

    @Column(name = "price")
    var price: BigDecimal
) : BaseEntity()
