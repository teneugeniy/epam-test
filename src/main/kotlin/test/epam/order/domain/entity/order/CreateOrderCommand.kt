package test.epam.order.domain.entity.order

import test.epam.order.domain.entity.item.Item
import java.math.BigDecimal

data class CreateOrderCommand(
    val items: Collection<CreateOrderItemCommand>,
    val totalPrice: BigDecimal,
)

data class CreateOrderItemCommand(
    val item: Item,
    val price: BigDecimal,
    val amount: Int,
    val calculatedTotal: BigDecimal,
)
