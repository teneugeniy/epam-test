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

    @Test
    fun `getOrder SHOULD return mapped order by id`() {
        // arrange
        val order: Order = mock()
        val orderDto: OrderDto = mock()
        whenever(orderMapper.toOrderDto(order)).thenReturn(orderDto)
        whenever(orderService.get(1)).thenReturn(order)

        // act
        val result = orderAppService.getOrder(1)

        // assert
        assertEquals(orderDto, result)
    }

    @Test
    fun `findAll SHOULD return and map all fetched orders`() {
        // arrange
        val order1: Order = mock()
        val order2: Order = mock()
        val orderDto1: OrderDto = mock()
        val orderDto2: OrderDto = mock()
        whenever(orderMapper.toOrderDto(order1)).thenReturn(orderDto1)
        whenever(orderMapper.toOrderDto(order2)).thenReturn(orderDto2)
        whenever(orderService.findAll()).thenReturn(listOf(order1, order2))

        // act
        val result = orderAppService.findAll()

        // assert
        assertEquals(listOf(orderDto1, orderDto2), result)
    }
}
