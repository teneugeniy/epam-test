package test.epam.order.controller.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import test.epam.order.app.service.OrderAppService
import test.epam.order.dto.http.order.AddOrderRequestDto
import test.epam.order.dto.http.order.OrderDto

@RestController
@RequestMapping("/api/orders")
class OrdersApiController(
    private val orderAppService: OrderAppService,
) {

    @PostMapping
    fun add(@RequestBody request: AddOrderRequestDto): OrderDto = orderAppService.addOrder(request)
}
