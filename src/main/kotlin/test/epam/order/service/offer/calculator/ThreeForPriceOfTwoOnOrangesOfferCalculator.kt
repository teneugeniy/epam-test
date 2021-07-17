package test.epam.order.service.offer.calculator

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import test.epam.order.domain.entity.order.command.ApplyOfferCommand
import test.epam.order.domain.entity.order.command.CreateOrderCommand
import java.util.UUID

@Service
@Order(200)
class ThreeForPriceOfTwoOnOrangesOfferCalculator : OfferCalculator {

    companion object {
        const val OFFER_CODE = "ThreeForPriceOfTwoOnOranges"
        val OFFER_UID: UUID = UUID.fromString("ab4696e3-cadf-4933-83f1-c3e5f815a978")
    }

    @Transactional(readOnly = true)
    override fun apply(orderCommand: CreateOrderCommand, appliedOffers: List<ApplyOfferCommand>): ApplyOfferCommand? {
        return orderCommand.items.firstOrNull { it.item.name == "oranges" && it.amount >= 3 }?.let { orangesOrderItem ->
            val discount = (orangesOrderItem.amount / 3).toBigDecimal() * orangesOrderItem.item.price
            ApplyOfferCommand(
                offerCode = OFFER_CODE,
                offerUid = OFFER_UID,
                item = null,
                amount = 0,
                discount = discount,
                recalculatedPrice = (appliedOffers.lastOrNull()?.recalculatedPrice ?: orderCommand.totalPrice) - discount
            )
        }
    }
}
