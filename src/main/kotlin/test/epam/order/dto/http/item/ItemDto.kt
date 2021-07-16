package test.epam.order.dto.http.item

import java.math.BigDecimal

data class ItemDto(
    val id: Long,
    val name: String,
    val price: BigDecimal,
)
