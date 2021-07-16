package test.epam.order.controller.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import test.epam.order.app.service.ItemAppService
import test.epam.order.dto.http.item.ItemDto

@RestController
@RequestMapping("/api/items")
class ItemsApiController(
    private val itemAppService: ItemAppService
) {

    @GetMapping
    fun list(): Collection<ItemDto> = itemAppService.getAll()
}
