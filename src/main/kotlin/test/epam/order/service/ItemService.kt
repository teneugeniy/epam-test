package test.epam.order.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import test.epam.order.domain.entity.item.Item
import test.epam.order.repository.ItemRepository

interface ItemService {
    fun get(id: Long): Item
    fun findAll(): Collection<Item>
}

@Service
class ItemServiceImpl(
    private val itemRepository: ItemRepository
) : ItemService {

    @Transactional(readOnly = true)
    override fun get(id: Long): Item {
        return itemRepository.getById(id)
    }

    @Transactional(readOnly = true)
    override fun findAll(): Collection<Item> {
        return itemRepository.findAll()
    }
}
