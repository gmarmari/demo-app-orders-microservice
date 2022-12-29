package gmarmari.demo.microservices.orders.services.usecase;

import gmarmari.demo.microservices.orders.entities.OrderAddressDao;
import gmarmari.demo.microservices.orders.entities.OrderDao;
import gmarmari.demo.microservices.orders.entities.OrderDetailsDao;
import gmarmari.demo.microservices.orders.entities.OrderProductMappingDao;
import gmarmari.demo.microservices.orders.repositories.OrderAddressRepository;
import gmarmari.demo.microservices.orders.repositories.OrderProductMappingRepository;
import gmarmari.demo.microservices.orders.repositories.OrderRepository;
import gmarmari.demo.microservices.orders.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderUseCase implements OrderService {

    private static final Sort ORDERS_SORT = Sort.by("creationDate").ascending();
    private static final Sort ORDER_ADDRESS_SORT = Sort.by("type").ascending();

    private final OrderRepository orderRepository;
    private final OrderAddressRepository orderAddressRepository;
    private final OrderProductMappingRepository orderProductMappingRepository;

    @Autowired
    public OrderUseCase(OrderRepository orderRepository,
                        OrderAddressRepository orderAddressRepository,
                        OrderProductMappingRepository orderProductMappingRepository) {
        this.orderRepository = orderRepository;
        this.orderAddressRepository = orderAddressRepository;
        this.orderProductMappingRepository = orderProductMappingRepository;
    }

    @Override
    public List<OrderDao> getOrders(String username) {
        return orderRepository.findByUsername(username, ORDERS_SORT);
    }

    @Override
    public Optional<OrderDao> getOrder(long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Optional<OrderDetailsDao> getOrderDetails(long orderId) {
        Optional<OrderDao> optionalOrder = orderRepository.findById(orderId);
        List<OrderAddressDao> addresses = orderAddressRepository.findByOrderId(orderId, ORDER_ADDRESS_SORT);
        List<OrderProductMappingDao> products = orderProductMappingRepository.findByOrderId(orderId);
        return optionalOrder.map(orderDao -> new OrderDetailsDao(orderDao, addresses, products));
    }


    @Override
    public void delete(long orderId) {
        orderProductMappingRepository.deleteByOrderId(orderId);
        orderAddressRepository.deleteByOrderId(orderId);
        orderRepository.deleteById(orderId);
    }

    @Override
    public void save(OrderDetailsDao orderDetails) {
        OrderDao savedOrder = orderRepository.save(orderDetails.order);

        orderAddressRepository.deleteByOrderId(savedOrder.getId());
        orderDetails.addresses.forEach(orderAddressRepository::save);
    }

    @Override
    public void saveOrderProductMappings(long orderId,  List<OrderProductMappingDao> mappings) {
        orderProductMappingRepository.deleteByOrderId(orderId);
        orderProductMappingRepository.saveAll(mappings);
    }
}
