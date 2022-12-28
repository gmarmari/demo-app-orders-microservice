package gmarmari.demo.microservices.orders.services;

import gmarmari.demo.microservices.orders.api.OrderProductDto;
import gmarmari.demo.microservices.orders.entities.OrderDao;
import gmarmari.demo.microservices.orders.entities.OrderDetailsDao;
import gmarmari.demo.microservices.orders.entities.OrderProductMappingDao;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<OrderDao> getOrders(String username);

    Optional<OrderDao> getOrder(long orderId);

    Optional<OrderDetailsDao> getOrderDetails(long orderId);

    List<OrderProductMappingDao> getOrderProductMappings(long orderId);

    void delete(long orderId);

    void save(OrderDetailsDao orderDetails);

    void saveOrderProductMappings(long orderId, List<OrderProductMappingDao> mappings);
}
