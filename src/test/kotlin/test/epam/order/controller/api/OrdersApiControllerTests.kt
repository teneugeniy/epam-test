package test.epam.order.controller.api

import com.jayway.jsonpath.JsonPath
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import test.epam.order.domain.entity.item.Item
import test.epam.order.repository.ItemRepository
import test.epam.order.repository.OrderRepository

@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class OrdersApiControllerTests {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Test
    fun `post SHOULD create and return a new order`() {
        val newItem = itemRepository.save(
            Item(
                name = "newItem",
                price = 10.0.toBigDecimal()
            )
        )

        val result = mvc.perform(
            post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "items": [
                            {
                                "itemId": ${newItem.idValue},
                                "amount": 5
                            }
                        ]
                    }
                """
                )
        )
            .andExpect(status().isOk)
            .andExpect(
                content().json(
                    """
                    {
                        "items": [
                            {
                                "item": {
                                    "id": ${newItem.idValue},
                                    "name": "newItem"
                                },
                                "price": 10.0,
                                "amount": 5,
                                "calculatedTotal": 50.0
                            }
                        ],
                        "totalPrice": 50.0
                    }"""
                )
            )
            .andReturn()

        val orderId: Long = JsonPath.read(result.response.contentAsString, "$.id")
        val savedOrder = orderRepository.getById(orderId)

        assertEquals(50.0.toBigDecimal(), savedOrder.totalPrice)
        assertEquals(1, savedOrder.items.size)
        savedOrder.items.first().also { orderItem ->
            assertEquals(newItem, orderItem.item)
            assertEquals(10.0.toBigDecimal(), orderItem.price)
            assertEquals(5, orderItem.amount)
            assertEquals(50.0.toBigDecimal(), orderItem.calculatedTotal)
        }
    }
}
