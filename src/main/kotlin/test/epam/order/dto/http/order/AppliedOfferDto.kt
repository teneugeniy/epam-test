package test.epam.order.dto.http.order

import test.epam.order.dto.http.item.ItemDto
import java.math.BigDecimal
import java.util.UUID

data class AppliedOfferDto(
    val offerUid: UUID,
    val offerCode: String,
    val discount: BigDecimal,
    val item: ItemDto?,
    val amount: Int,
)
