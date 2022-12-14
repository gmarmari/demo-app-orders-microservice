package gmarmari.demo.microservices.orders.services;

import gmarmari.demo.microservices.orders.entities.ProductOldDao;

import java.util.List;
import java.util.Optional;

public interface ProductOldService {

    void save(ProductOldDao productDao);

    void delete(long productId);

    Optional<ProductOldDao> getProduct(long productId);

    List<ProductOldDao> getProducts();

}
