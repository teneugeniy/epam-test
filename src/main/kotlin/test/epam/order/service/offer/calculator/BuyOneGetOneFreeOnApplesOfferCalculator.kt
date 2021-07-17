package test.epam.order.service.offer.calculator

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import test.epam.order.domain.entity.order.command.ApplyOfferCommand
import test.epam.order.domain.entity.order.command.CreateOrderCommand
import java.util.UUID

@Service
@Order(100)
class BuyOneGetOneFreeOnApplesOfferCalculator : OfferCalculator {

    companion object {
        const val OFFER_CODE = "BuyOneGetOneFreeOnApples"
        val OFFER_UID: UUID = UUID.fromString("94ef7b5e-eac3-4dfb-828a-35169ffb26ab")
    }

    @Transactional(readOnly = true)
    override fun apply(orderCommand: CreateOrderCommand, appliedOffers: List<ApplyOfferCommand>): ApplyOfferCommand? {
        return orderCommand.items.firstOrNull { it.item.name == "apples" }?.let { applesOrderItem ->
            ApplyOfferCommand(
                offerCode = OFFER_CODE,
                offerUid = OFFER_UID,
                item = applesOrderItem.item,
                amount = applesOrderItem.amount,
                discount = 0.toBigDecimal(),
                recalculatedPrice = appliedOffers.lastOrNull()?.recalculatedPrice ?: orderCommand.totalPrice
            )
        }
    }
}
