package gmarmari.demo.microservices.orders.repositories;

import gmarmari.demo.microservices.orders.entities.OrderAddressDao;
import gmarmari.demo.microservices.orders.entities.OrderAddressTypeDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderAddressRepository extends JpaRepository<OrderAddressDao, Long> {

    List<OrderAddressDao> findByOrderId(long orderId, Sort sort);

    Optional<OrderAddressDao> findByOrderIdAndType(long orderId, OrderAddressTypeDao type);

    void deleteByOrderId(long orderId);

}
