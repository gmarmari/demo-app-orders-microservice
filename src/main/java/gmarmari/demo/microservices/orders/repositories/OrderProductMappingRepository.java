package gmarmari.demo.microservices.orders.repositories;

import gmarmari.demo.microservices.orders.entities.OrderProductMappingDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductMappingRepository extends JpaRepository<OrderProductMappingDao, Long> {

    List<OrderProductMappingDao> findByOrderId(long oderId);

    void deleteByOrderId(long orderId);


}
