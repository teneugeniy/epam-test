package test.epam.order.mapper

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import test.epam.order.domain.entity.order.CreateOrderCommand
import test.epam.order.domain.entity.order.CreateOrderItemCommand
import test.epam.order.domain.entity.order.Order
import test.epam.order.dto.http.order.AddOrderRequestDto
import test.epam.order.dto.http.order.OrderDto
import test.epam.order.dto.http.order.OrderItemDto
import test.epam.order.service.ItemService

interface OrderMapper {
    fun toCreateOrderCommand(request: AddOrderRequestDto): CreateOrderCommand
    fun toOrderDto(order: Order): OrderDto
}

@Service
class OrderMapperImpl(
    private val itemService: ItemService,
    private val itemMapper: ItemMapper
) : OrderMapper {

    @Transactional(readOnly = true)
    override fun toCreateOrderCommand(request: AddOrderRequestDto): CreateOrderCommand {
        val requestItems = request.items
            .map { itemService.get(it.itemId) to it.amount }
            .map { (item, count) ->
                CreateOrderItemCommand(
                    item = item,
                    price = item.price,
                    amount = count,
                    calculatedTotal = item.price * count.toBigDecimal()
                )
            }
        return CreateOrderCommand(
            items = requestItems,
            totalPrice = requestItems.sumOf { it.calculatedTotal }
        )
    }

    @Transactional(readOnly = true)
    override fun toOrderDto(order: Order): OrderDto {
        return OrderDto(
            id = order.idValue,
            totalPrice = order.totalPrice,
            items = order.items.map {
                OrderItemDto(
                    item = itemMapper.toItemDto(it.item),
                    price = it.price,
                    amount = it.amount,
                    calculatedTotal = it.calculatedTotal
                )
            }
        )
    }
}
