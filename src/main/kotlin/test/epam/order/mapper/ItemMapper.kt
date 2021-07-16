package test.epam.order.mapper

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import test.epam.order.domain.entity.item.Item
import test.epam.order.dto.http.item.ItemDto

interface ItemMapper {
    fun toItemDto(item: Item): ItemDto
}

@Service
class ItemMapperImpl : ItemMapper {

    @Transactional(readOnly = true)
    override fun toItemDto(item: Item): ItemDto {
        return ItemDto(
            id = item.idValue,
            name = item.name,
            price = item.price
        )
    }
}
