package test.epam.order.app.service

import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import test.epam.order.domain.entity.order.Order
import test.epam.order.domain.entity.order.command.ApplyOfferCommand
import test.epam.order.domain.entity.order.command.CreateOrderCommand
import test.epam.order.dto.http.order.AddOrderRequestDto
import test.epam.order.dto.http.order.OrderDto
import test.epam.order.mapper.OrderMapper
import test.epam.order.service.OrderService
import test.epam.order.service.offer.OfferManager

class OrderAppServiceImplTests {

    private lateinit var orderAppService: OrderAppServiceImpl

    private lateinit var orderService: OrderService
    private lateinit var orderMapper: OrderMapper
    private lateinit var offerManager: OfferManager

    @BeforeEach
    fun beforeEach() {
        orderService = mock()
        orderMapper = mock()
        offerManager = mock()

        orderAppService = OrderAppServiceImpl(orderService, orderMapper, offerManager)
    }

    @Test
    fun `addOrder SHOULD run adding order pipeline`() {
        // arrange
        val request: AddOrderRequestDto = mock()
        val createOrderCommand: CreateOrderCommand = mock()
        val order: Order = mock()
        val orderDto: OrderDto = mock()
        val applyOfferCommand: ApplyOfferCommand = mock()
        whenever(orderMapper.toCreateOrderCommand(request)).thenReturn(createOrderCommand)
        whenever(orderMapper.toOrderDto(order)).thenReturn(orderDto)
        whenever(offerManager.applyOffers(createOrderCommand)).thenReturn(listOf(applyOfferCommand))
        whenever(orderService.create(createOrderCommand, listOf(applyOfferCommand))).thenReturn(order)

        // act
        val result = orderAppService.addOrder(request)

        // assert
        assertEquals(orderDto, result)
    }
}
