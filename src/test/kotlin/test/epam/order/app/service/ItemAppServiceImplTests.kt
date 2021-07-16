package test.epam.order.app.service

import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import test.epam.order.domain.entity.item.Item
import test.epam.order.dto.http.item.ItemDto
import test.epam.order.mapper.ItemMapper
import test.epam.order.service.ItemService

class ItemAppServiceImplTests {

    private lateinit var itemAppService: ItemAppServiceImpl

    private lateinit var itemService: ItemService
    private lateinit var itemMapper: ItemMapper

    @BeforeEach
    fun beforeEach() {
        itemService = mock()
        itemMapper = mock()

        itemAppService = ItemAppServiceImpl(itemService, itemMapper)
    }

    @Test
    fun `getAll SHOULD return list of items`() {
        // arrange
        val item: Item = mock()
        val itemDto: ItemDto = mock()
        whenever(itemService.findAll()).thenReturn(listOf(item))
        whenever(itemMapper.toItemDto(item)).thenReturn(itemDto)

        // act
        val result = itemAppService.getAll()

        // assert
        assertEquals(listOf(itemDto), result)
    }
}
