package test.epam.order.service.offer.calculator

import test.epam.order.domain.entity.order.command.ApplyOfferCommand
import test.epam.order.domain.entity.order.command.CreateOrderCommand

interface OfferCalculator {
    fun apply(orderCommand: CreateOrderCommand, appliedOffers: List<ApplyOfferCommand>): ApplyOfferCommand?
}
