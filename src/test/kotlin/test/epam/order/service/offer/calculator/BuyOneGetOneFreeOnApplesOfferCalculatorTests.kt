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

class BuyOneGetOneFreeOnApplesOfferCalculatorTests {

    private lateinit var offerCalculator: BuyOneGetOneFreeOnApplesOfferCalculator

    @BeforeEach
    fun beforeEach() {
        offerCalculator = BuyOneGetOneFreeOnApplesOfferCalculator()
    }

    @Test
    fun `apply SHOULD return null WHEN there are no apples in order`() {
        // arrange
        val orderCommand = CreateOrderCommand(items = emptyList(), totalPrice = 1.toBigDecimal())

        // act
        val result = offerCalculator.apply(orderCommand, emptyList())

        // assert
        assertNull(result)
    }

    @Test
    fun `apply SHOULD return offer command with doubled apples WHEN there are apples in order`() {
        // arrange
        val applesItem: Item = mock {
            on { it.name }.then { "apples" }
        }
        val orderCommand = CreateOrderCommand(
            items = listOf(
                CreateOrderItemCommand(
                    item = applesItem,
                    price = 1.toBigDecimal(),
                    amount = 10,
                    calculatedTotal = 10.toBigDecimal()
                )
            ),
            totalPrice = 1.toBigDecimal()
        )
        val expectedResult = ApplyOfferCommand(
            offerCode = BuyOneGetOneFreeOnApplesOfferCalculator.OFFER_CODE,
            offerUid = BuyOneGetOneFreeOnApplesOfferCalculator.OFFER_UID,
            item = applesItem,
            amount = 10,
            discount = 0.toBigDecimal(),
            recalculatedPrice = 1.toBigDecimal()
        )

        // act
        val result = offerCalculator.apply(orderCommand, emptyList())

        // assert
        assertEquals(expectedResult, result)
    }
}
