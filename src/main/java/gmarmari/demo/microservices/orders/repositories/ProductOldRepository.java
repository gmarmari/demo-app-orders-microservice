package gmarmari.demo.microservices.orders.repositories;

import gmarmari.demo.microservices.orders.entities.ProductOldDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductOldRepository extends JpaRepository<ProductOldDao, Long> {

    Optional<ProductOldDao> findById(long productId);

}
