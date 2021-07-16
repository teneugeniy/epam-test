package test.epam.order.service

import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import test.epam.order.domain.entity.item.Item
import test.epam.order.repository.ItemRepository

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Transactional
class ItemServiceImplTests {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var itemRepository: ItemRepository

    private lateinit var itemService: ItemServiceImpl

    @BeforeEach
    fun beforeEach() {
        itemService = ItemServiceImpl(itemRepository)
    }

    @Test
    fun `get SHOULD return item from database WHEN the item is found`() {
        // arrange
        val item = Item(name = "name", price = 1.toBigDecimal())
        with(entityManager) {
            persist(item)
            flush()
        }

        // act
        val result = itemService.get(item.idValue)

        // assert
        assertEquals(item, result)
    }

    @Test
    fun `get SHOULD throw exception WHEN the item is not found`() {
        // arrange
        // act
        val exception: Exception = assertThrows { itemService.get(Long.MIN_VALUE) }

        // assert
        assertThat(exception, instanceOf(JpaObjectRetrievalFailureException::class.java))
    }
}
