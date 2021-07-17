package test.epam.order.mapper

import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import test.epam.order.domain.entity.item.Item
import test.epam.order.domain.entity.order.AppliedOffer
import test.epam.order.domain.entity.order.Order
import test.epam.order.domain.entity.order.OrderItem
import test.epam.order.domain.entity.order.command.CreateOrderCommand
import test.epam.order.domain.entity.order.command.CreateOrderItemCommand
import test.epam.order.dto.http.item.ItemDto
import test.epam.order.dto.http.order.AddOrderItemRequestDto
import test.epam.order.dto.http.order.AddOrderRequestDto
import test.epam.order.dto.http.order.AppliedOfferDto
import test.epam.order.dto.http.order.OrderDto
import test.epam.order.dto.http.order.OrderItemDto
import test.epam.order.service.ItemService
import java.time.LocalDateTime
import java.util.UUID

class OrderMapperImplTests {

    private lateinit var orderMapper: OrderMapperImpl

    private lateinit var itemService: ItemService
    private lateinit var itemMapper: ItemMapper

    @BeforeEach
    fun beforeEach() {
        itemService = mock()
        itemMapper = mock()

        orderMapper = OrderMapperImpl(itemService, itemMapper)
    }

    @Test
    fun `toCreateOrderCommand SHOULD build result from request`() {
        // arrange
        val request = AddOrderRequestDto(
            items = listOf(
                AddOrderItemRequestDto(
                    itemId = 1,
                    amount = 1
                )
            )
        )
        val item = Item(name = "item-name", price = 1.toBigDecimal())
        val expectedResult = CreateOrderCommand(
            items = listOf(
                CreateOrderItemCommand(
                    item = item,
                    price = 1.toBigDecimal(),
                    amount = 1,
                    calculatedTotal = 1.toBigDecimal()
                )
            ),
            totalPrice = 1.toBigDecimal()
        )
        whenever(itemService.get(1)).thenReturn(item)

        // act
        val result = orderMapper.toCreateOrderCommand(request)

        // assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `toCreateOrderCommand SHOULD multiply calculated total for each item`() {
        // arrange
        val request = AddOrderRequestDto(
            items = listOf(
                AddOrderItemRequestDto(
                    itemId = 1,
                    amount = 3
                )
            )
        )
        val item = Item(name = "item-name", price = 2.toBigDecimal())
        whenever(itemService.get(1)).thenReturn(item)

        // act
        val result = orderMapper.toCreateOrderCommand(request)

        // assert
        assertEquals(6.toBigDecimal(), result.items.first().calculatedTotal)
    }

    @Test
    fun `toCreateOrderCommand SHOULD sum up each item's total for totalPrice`() {
        // arrange
        val request = AddOrderRequestDto(
            items = listOf(
                AddOrderItemRequestDto(
                    itemId = 1,
                    amount = 3
                ),
                AddOrderItemRequestDto(
                    itemId = 2,
                    amount = 7
                ),
            )
        )
        val item1 = Item(name = "item-name-1", price = 2.toBigDecimal())
        val item2 = Item(name = "item-name-2", price = 9.toBigDecimal())
        whenever(itemService.get(1)).thenReturn(item1)
        whenever(itemService.get(2)).thenReturn(item2)

        // act
        val result = orderMapper.toCreateOrderCommand(request)

        // assert
        assertEquals(69.toBigDecimal(), result.totalPrice)
    }

    @Test
    fun `toOrderDto SHOULD map result from request`() {
        // arrange
        val item = Item(name = "item-name", price = 1.toBigDecimal())
        val itemDto = ItemDto(id = 1, name = "name", price = 2.toBigDecimal())
        val order = Order(
            items = listOf(
                OrderItem(
                    item = item,
                    price = 2.toBigDecimal(),
                    amount = 3,
                    calculatedTotal = 4.toBigDecimal()
                )
            ),
            timeStamp = LocalDateTime.MIN,
            totalPrice = 5.toBigDecimal(),
            appliedOffers = listOf(
                AppliedOffer(
                    offerUid = UUID.fromString("ea360bc8-e6b4-11eb-ba80-0242ac130004"),
                    offerCode = "super-offer",
                    item = item,
                    amount = 3,
                    discount = 3.toBigDecimal(),
                )
            ),
            totalPriceToPay = 10.toBigDecimal()
        ).apply { id = 6 }
        val expectedResult = OrderDto(
            id = 6,
            items = listOf(
                OrderItemDto(
                    item = itemDto,
                    price = 2.toBigDecimal(),
                    amount = 3,
                    calculatedTotal = 4.toBigDecimal()
                )
            ),
            totalPrice = 5.toBigDecimal(),
            appliedOffers = listOf(
                AppliedOfferDto(
                    offerUid = UUID.fromString("ea360bc8-e6b4-11eb-ba80-0242ac130004"),
                    offerCode = "super-offer",
                    discount = 3.toBigDecimal(),
                    item = itemDto,
                    amount = 3,
                )
            ),
            priceToPay = 10.toBigDecimal()
        )
        whenever(itemMapper.toItemDto(item)).thenReturn(itemDto)

        // act
        val result = orderMapper.toOrderDto(order)

        // assert
        assertEquals(expectedResult, result)
    }
}
