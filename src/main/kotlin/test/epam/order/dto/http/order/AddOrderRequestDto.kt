package test.epam.order.dto.http.order

// Ideally the order should first be reserved or populated with items versions or checksums to avoid problems with everchanging prices
data class AddOrderRequestDto(
    val items: Collection<AddOrderItemRequestDto>
)

data class AddOrderItemRequestDto(
    val itemId: Long,
    val amount: Int
)
