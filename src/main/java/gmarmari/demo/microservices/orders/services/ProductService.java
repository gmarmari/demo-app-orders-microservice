package gmarmari.demo.microservices.orders.services;

import gmarmari.demo.microservices.orders.entities.ProductDao;
import gmarmari.demo.microservices.orders.entities.ProductDetailsDao;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductDao> getProducts();

    Optional<ProductDao> getProduct(long productId);


    Optional<ProductDetailsDao> getProductDetails(long productId);

    void delete(long productId);

    void save(ProductDetailsDao productDetails);

}
