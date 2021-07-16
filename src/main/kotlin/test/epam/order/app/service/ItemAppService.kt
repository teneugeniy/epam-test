package test.epam.order.app.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import test.epam.order.dto.http.item.ItemDto
import test.epam.order.mapper.ItemMapper
import test.epam.order.service.ItemService

interface ItemAppService {
    fun getAll(): Collection<ItemDto>
}

@Service
class ItemAppServiceImpl(
    private val itemService: ItemService,
    private val itemMapper: ItemMapper
) : ItemAppService {

    @Transactional(readOnly = true)
    override fun getAll(): Collection<ItemDto> {
        return itemService.findAll().map { itemMapper.toItemDto(it) }
    }
}
