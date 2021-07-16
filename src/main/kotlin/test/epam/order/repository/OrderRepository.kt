package test.epam.order.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import test.epam.order.domain.entity.order.Order

@Repository
interface OrderRepository : JpaRepository<Order, Long>
