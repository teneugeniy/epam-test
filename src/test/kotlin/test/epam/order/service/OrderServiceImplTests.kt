package test.epam.order.service

import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import test.epam.order.domain.entity.item.Item
import test.epam.order.domain.entity.order.command.ApplyOfferCommand
import test.epam.order.domain.entity.order.command.CreateOrderCommand
import test.epam.order.domain.entity.order.command.CreateOrderItemCommand
import test.epam.order.repository.OrderRepository
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Transactional
class OrderServiceImplTests {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var orderRepository: OrderRepository

    private lateinit var orderService: OrderServiceImpl

    @BeforeEach
    fun beforeEach() {
        orderService = OrderServiceImpl(orderRepository = orderRepository, timeStampProvider = { LocalDateTime.MAX })
    }

    @Test
    fun `create SHOULD persist the order to the database`() {
        // arrange
        val item = Item(name = "name", price = 1.toBigDecimal())
        with(entityManager) {
            persist(item)
            flush()
        }

        val command = CreateOrderCommand(
            items = listOf(
                CreateOrderItemCommand(
                    item = item,
                    price = 1.toBigDecimal(),
                    amount = 1,
                    calculatedTotal = 10.toBigDecimal(),
                )
            ),
            totalPrice = 5.toBigDecimal(),
        )

        // act
        val result = orderService.create(command, emptyList())

        // assert
        assertEquals(5.toBigDecimal(), result.totalPrice)
        assertEquals(LocalDateTime.MAX, result.timeStamp)
        assertEquals(1, result.items.size)
        assertEquals(5.toBigDecimal(), result.totalPriceToPay)
        assertEquals(0, result.appliedOffers.size)
        result.items.first().let { orderItem ->
            assertEquals(item, orderItem.item)
            assertEquals(1.toBigDecimal(), orderItem.price)
            assertEquals(1, orderItem.amount)
            assertEquals(10.toBigDecimal(), orderItem.calculatedTotal)
        }
    }

    @Test
    fun `create SHOULD use totalPrice as totalPriceToPay WHEN no offers are applied`() {
        // arrange
        val command = CreateOrderCommand(
            items = listOf(),
            totalPrice = 5.toBigDecimal(),
        )

        // act
        val result = orderService.create(command, emptyList())

        // assert
        assertEquals(5.toBigDecimal(), result.totalPriceToPay)
    }

    @Test
    fun `create SHOULD use recalculatedPrice of the last offer as totalPriceToPay WHEN several offers are applied`() {
        // arrange
        val command = CreateOrderCommand(
            items = listOf(),
            totalPrice = 5.toBigDecimal(),
        )
        val offerCommands = listOf(
            ApplyOfferCommand(
                offerCode = "offer1",
                offerUid = UUID.fromString("ea360bc8-e6b4-11eb-ba80-0242ac130004"),
                item = null,
                discount = 0.toBigDecimal(),
                recalculatedPrice = 10.toBigDecimal(),
                amount = 0,
            ),
            ApplyOfferCommand(
                offerCode = "offer2",
                offerUid = UUID.fromString("ba8baccb-dc0b-48e4-b20b-caebbd26973a"),
                item = null,
                discount = 0.toBigDecimal(),
                recalculatedPrice = 20.toBigDecimal(),
                amount = 0,
            )
        )

        // act
        val result = orderService.create(command, offerCommands)

        // assert
        assertEquals(20.toBigDecimal(), result.totalPriceToPay)
    }
}
