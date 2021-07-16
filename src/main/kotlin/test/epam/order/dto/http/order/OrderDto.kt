package test.epam.order.dto.http.order

import test.epam.order.dto.http.item.ItemDto
import java.math.BigDecimal

data class OrderDto(
    val id: Long,
    val items: Collection<OrderItemDto>,
    val totalPrice: BigDecimal,
)

data class OrderItemDto(
    val item: ItemDto,
    val price: BigDecimal,
    val amount: Int,
    val calculatedTotal: BigDecimal
)
