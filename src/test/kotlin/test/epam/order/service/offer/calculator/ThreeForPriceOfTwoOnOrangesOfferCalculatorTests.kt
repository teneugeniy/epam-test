package test.epam.order.service.offer.calculator

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import test.epam.order.domain.entity.item.Item
import test.epam.order.domain.entity.order.command.ApplyOfferCommand
import test.epam.order.domain.entity.order.command.CreateOrderCommand
import test.epam.order.domain.entity.order.command.CreateOrderItemCommand

class ThreeForPriceOfTwoOnOrangesOfferCalculatorTests {

    private lateinit var offerCalculator: ThreeForPriceOfTwoOnOrangesOfferCalculator

    @BeforeEach
    fun beforeEach() {
        offerCalculator = ThreeForPriceOfTwoOnOrangesOfferCalculator()
    }

    @Test
    fun `apply SHOULD return null WHEN there are no oranges in order`() {
        // arrange
        val orderCommand = CreateOrderCommand(items = emptyList(), totalPrice = 1.toBigDecimal())

        // act
        val result = offerCalculator.apply(orderCommand, emptyList())

        // assert
        assertNull(result)
    }

    @Test
    fun `apply SHOULD return null WHEN there are less then 3 oranges in order`() {
        // arrange
        val orangesItem: Item = mock {
            on { it.name }.then { "oranges" }
        }
        val orderCommand = CreateOrderCommand(
            items = listOf(
                CreateOrderItemCommand(
                    item = orangesItem,
                    price = 1.toBigDecimal(),
                    amount = 2,
                    calculatedTotal = 2.toBigDecimal()
                )
            ),
            totalPrice = 1.toBigDecimal()
        )

        // act
        val result = offerCalculator.apply(orderCommand, emptyList())

        // assert
        assertNull(result)
    }

    @Test
    fun `apply SHOULD return an offer with discount WHEN there are more then 3 oranges in order`() {
        // arrange
        val orangesItem: Item = mock {
            on { it.name }.then { "oranges" }
            on { it.price }.then { 1.toBigDecimal() }
        }
        val orderCommand = CreateOrderCommand(
            items = listOf(
                CreateOrderItemCommand(
                    item = orangesItem,
                    price = 1.toBigDecimal(),
                    amount = 4,
                    calculatedTotal = 4.toBigDecimal()
                )
            ),
            totalPrice = 4.toBigDecimal()
        )
        val expectedResult = ApplyOfferCommand(
            offerCode = ThreeForPriceOfTwoOnOrangesOfferCalculator.OFFER_CODE,
            offerUid = ThreeForPriceOfTwoOnOrangesOfferCalculator.OFFER_UID,
            item = null,
            amount = 0,
            discount = 1.toBigDecimal(),
            recalculatedPrice = 3.toBigDecimal()
        )

        // act
        val result = offerCalculator.apply(orderCommand, emptyList())

        // assert
        assertEquals(expectedResult, result)
    }
}
