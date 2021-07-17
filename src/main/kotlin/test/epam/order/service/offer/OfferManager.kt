package test.epam.order.service.offer

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import test.epam.order.domain.entity.order.command.ApplyOfferCommand
import test.epam.order.domain.entity.order.command.CreateOrderCommand
import test.epam.order.service.offer.calculator.OfferCalculator

interface OfferManager {
    fun applyOffers(orderCommand: CreateOrderCommand): List<ApplyOfferCommand>
}

@Service
class OfferManagerImpl(
    private val offerCalculators: List<OfferCalculator>
) : OfferManager {

    @Transactional(readOnly = true)
    override fun applyOffers(orderCommand: CreateOrderCommand): List<ApplyOfferCommand> {
        return offerCalculators.fold(emptyList()) { appliedOffers, offer ->
            offer.apply(orderCommand, appliedOffers)?.let { appliedOffers + it } ?: appliedOffers
        }
    }
}
