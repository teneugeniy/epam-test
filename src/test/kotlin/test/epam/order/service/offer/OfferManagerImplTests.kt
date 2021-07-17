package test.epam.order.service.offer

import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import test.epam.order.domain.entity.order.command.ApplyOfferCommand
import test.epam.order.domain.entity.order.command.CreateOrderCommand
import test.epam.order.service.offer.calculator.OfferCalculator

class OfferManagerImplTests {

    private lateinit var offerManager: OfferManagerImpl

    private lateinit var offerCalculator1: OfferCalculator
    private lateinit var offerCalculator2: OfferCalculator

    @BeforeEach
    fun beforeEach() {
        offerCalculator1 = mock()
        offerCalculator2 = mock()

        offerManager = OfferManagerImpl(listOf(offerCalculator1, offerCalculator2))
    }

    @Test
    fun `applyOffers SHOULD return empty list WHEN no offers are applicable`() {
        // arrange
        val orderCommand: CreateOrderCommand = mock()
        whenever(offerCalculator1.apply(any(), any())).thenReturn(null)
        whenever(offerCalculator2.apply(any(), any())).thenReturn(null)

        // act
        val result = offerManager.applyOffers(orderCommand)

        // assert
        assertEquals(0, result.size)
    }

    @Test
    fun `applyOffers SHOULD return offers in right order WHEN they are applicable by calculators`() {
        // arrange
        val orderCommand: CreateOrderCommand = mock()
        val offer1: ApplyOfferCommand = mock()
        val offer2: ApplyOfferCommand = mock()
        whenever(offerCalculator1.apply(orderCommand, emptyList())).thenReturn(offer1)
        whenever(offerCalculator2.apply(orderCommand, listOf(offer1))).thenReturn(offer2)

        // act
        val result = offerManager.applyOffers(orderCommand)

        // assert
        assertEquals(listOf(offer1, offer2), result)
    }

    @Test
    fun `applyOffers SHOULD filter out offers WHEN not all of them are applicable`() {
        // arrange
        val orderCommand: CreateOrderCommand = mock()
        val offer2: ApplyOfferCommand = mock()
        whenever(offerCalculator1.apply(orderCommand, emptyList())).thenReturn(null)
        whenever(offerCalculator2.apply(orderCommand, emptyList())).thenReturn(offer2)

        // act
        val result = offerManager.applyOffers(orderCommand)

        // assert
        assertEquals(listOf(offer2), result)
    }
}
