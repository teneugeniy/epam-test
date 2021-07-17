package test.epam.order.app.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import test.epam.order.dto.http.order.AddOrderRequestDto
import test.epam.order.dto.http.order.OrderDto
import test.epam.order.mapper.OrderMapper
import test.epam.order.service.OrderService
import test.epam.order.service.offer.OfferManager

interface OrderAppService {
    fun addOrder(request: AddOrderRequestDto): OrderDto
    fun getOrder(id: Long): OrderDto
    fun findAll(): Collection<OrderDto>
}

@Service
class OrderAppServiceImpl(
    private val orderService: OrderService,
    private val orderMapper: OrderMapper,
    private val offerManager: OfferManager
) : OrderAppService {

    @Transactional
    override fun addOrder(request: AddOrderRequestDto): OrderDto {
        val orderCommand = orderMapper.toCreateOrderCommand(request)
        val offers = offerManager.applyOffers(orderCommand = orderCommand)
        val order = orderService.create(orderCommand, offers)

        return orderMapper.toOrderDto(order)
    }

    @Transactional(readOnly = true)
    override fun getOrder(id: Long): OrderDto {
        val order = orderService.get(id)
        return orderMapper.toOrderDto(order)
    }

    @Transactional(readOnly = true)
    override fun findAll(): Collection<OrderDto> {
        val orders = orderService.findAll()
        return orders.map { orderMapper.toOrderDto(it) }
    }
}
