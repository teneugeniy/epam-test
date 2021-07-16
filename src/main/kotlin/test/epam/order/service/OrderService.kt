package test.epam.order.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import test.epam.order.domain.entity.order.CreateOrderCommand
import test.epam.order.domain.entity.order.Order
import test.epam.order.domain.entity.order.OrderItem
import test.epam.order.repository.OrderRepository
import java.time.LocalDateTime

interface OrderService {
    fun create(command: CreateOrderCommand): Order
}

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val timeStampProvider: () -> LocalDateTime = { LocalDateTime.now() }
) : OrderService {

    @Transactional
    override fun create(command: CreateOrderCommand): Order {
        val order = Order(
            timeStamp = timeStampProvider.invoke(),
            totalPrice = command.totalPrice,
            items = command.items.map {
                OrderItem(
                    item = it.item,
                    price = it.price,
                    amount = it.amount,
                    calculatedTotal = it.calculatedTotal
                )
            }
        )
        return orderRepository.save(order)
    }
}
