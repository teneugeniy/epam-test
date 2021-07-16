package test.epam.order.mapper

import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.epam.order.domain.entity.item.Item
import test.epam.order.dto.http.item.ItemDto

class ItemMapperImplTests {

    private lateinit var itemMapper: ItemMapperImpl

    @BeforeEach
    fun beforeEach() {
        itemMapper = ItemMapperImpl()
    }

    @Test
    fun `toItemDto SHOULD return model mapped from Item`() {
        // arrange
        val item = Item(
            name = "item-name",
            price = 100.0.toBigDecimal()
        ).apply {
            id = 1
        }

        // act
        val result = itemMapper.toItemDto(item)

        // assert
        assertEquals(ItemDto(id = 1, name = "item-name", price = 100.0.toBigDecimal()), result)
    }
}
