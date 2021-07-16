package test.epam.order.controller.api

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class ItemsApiControllerTests {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `get SHOULD return apples and oranges`() {
        mvc.perform(get("/api/items"))

            // then
            .andExpect(status().isOk)
            .andExpect(
                content().json(
                    """
                    [
                        {
                            "name": "apples",
                            "price": 0.60
                        },
                        {
                            "name": "oranges",
                            "price": 0.25
                        }
                    ]"""
                )
            )
    }
}
