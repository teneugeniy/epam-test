package test.epam.order.app.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import test.epam.order.dto.http.order.AddOrderRequestDto
import test.epam.order.dto.http.order.OrderDto
import test.epam.order.mapper.OrderMapper
import test.epam.order.service.OrderService

interface OrderAppService {
    fun addOrder(request: AddOrderRequestDto): OrderDto
}

@Service
class OrderAppServiceImpl(
    private val orderService: OrderService,
    private val orderMapper: OrderMapper
) : OrderAppService {

    @Transactional
    override fun addOrder(request: AddOrderRequestDto): OrderDto {
        val command = orderMapper.toCreateOrderCommand(request)
        val order = orderService.create(command)

        return orderMapper.toOrderDto(order)
    }
}
