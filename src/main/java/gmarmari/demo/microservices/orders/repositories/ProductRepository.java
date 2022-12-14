package gmarmari.demo.microservices.orders.repositories;

import gmarmari.demo.microservices.orders.entities.ProductDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductDao, Long> {

}
