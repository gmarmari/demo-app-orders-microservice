package gmarmari.demo.microservices.orders.repositories;

import gmarmari.demo.microservices.orders.entities.OrderDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderDao, Long> {

    List<OrderDao> findByUsername(String username, Sort sort);

}
