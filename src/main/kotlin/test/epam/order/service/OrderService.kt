package test.epam.order.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import test.epam.order.domain.entity.order.AppliedOffer
import test.epam.order.domain.entity.order.Order
import test.epam.order.domain.entity.order.OrderItem
import test.epam.order.domain.entity.order.command.ApplyOfferCommand
import test.epam.order.domain.entity.order.command.CreateOrderCommand
import test.epam.order.repository.OrderRepository
import java.time.LocalDateTime

interface OrderService {
    fun create(command: CreateOrderCommand, offerCommands: List<ApplyOfferCommand>): Order
}

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val timeStampProvider: () -> LocalDateTime = { LocalDateTime.now() }
) : OrderService {

    @Transactional
    override fun create(command: CreateOrderCommand, offerCommands: List<ApplyOfferCommand>): Order {
        val order = Order(
            timeStamp = timeStampProvider.invoke(),
            totalPrice = command.totalPrice,
            items = command.items.map { orderItem ->
                OrderItem(
                    item = orderItem.item,
                    price = orderItem.price,
                    amount = orderItem.amount,
                    calculatedTotal = orderItem.calculatedTotal
                )
            },
            appliedOffers = offerCommands.map { offerCommand ->
                AppliedOffer(
                    offerCode = offerCommand.offerCode,
                    offerUid = offerCommand.offerUid,
                    discount = offerCommand.discount,
                    item = offerCommand.item,
                    amount = offerCommand.amount,
                )
            },
            totalPriceToPay = offerCommands.lastOrNull()?.recalculatedPrice ?: command.totalPrice
        )
        return orderRepository.save(order)
    }
}
