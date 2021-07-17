package test.epam.order.domain.entity.order.command

import test.epam.order.domain.entity.item.Item
import java.math.BigDecimal
import java.util.UUID

data class ApplyOfferCommand(
    val offerCode: String,
    val offerUid: UUID,
    val item: Item?,
    val amount: Int,
    val discount: BigDecimal,
    val recalculatedPrice: BigDecimal,
)
