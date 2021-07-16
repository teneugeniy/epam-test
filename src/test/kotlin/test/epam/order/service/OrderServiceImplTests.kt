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
import test.epam.order.domain.entity.order.CreateOrderCommand
import test.epam.order.domain.entity.order.CreateOrderItemCommand
import test.epam.order.repository.OrderRepository
import java.time.LocalDateTime

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
                    calculatedTotal = 10.toBigDecimal()
                )
            ),
            totalPrice = 5.toBigDecimal()
        )

        // act
        val result = orderService.create(command)

        // assert
        assertEquals(5.toBigDecimal(), result.totalPrice)
        assertEquals(LocalDateTime.MAX, result.timeStamp)
        assertEquals(1, result.items.size)
        result.items.first().let { orderItem ->
            assertEquals(item, orderItem.item)
            assertEquals(1.toBigDecimal(), orderItem.price)
            assertEquals(1, orderItem.amount)
            assertEquals(10.toBigDecimal(), orderItem.calculatedTotal)
        }
    }
}
