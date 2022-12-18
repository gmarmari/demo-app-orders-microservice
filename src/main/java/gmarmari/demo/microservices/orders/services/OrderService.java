package gmarmari.demo.microservices.orders.services;

import gmarmari.demo.microservices.orders.entities.OrderDao;
import gmarmari.demo.microservices.orders.entities.OrderDetailsDao;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<OrderDao> getOrders(String username);

    Optional<OrderDao> getOrder(long orderId);

    Optional<OrderDetailsDao> getOrderDetails(long orderId);

    List<Long> getOrderProductIds(long orderId);

    void delete(long orderId);

    void save(OrderDetailsDao orderDetails);

    void saveOrderProducts(long orderId, List<Long> productIds);
}
