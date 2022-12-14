package gmarmari.demo.microservices.orders.api.controllers;

import gmarmari.demo.microservices.orders.adapters.ProductAdapter;
import gmarmari.demo.microservices.orders.api.ProductDetailsDto;
import gmarmari.demo.microservices.orders.api.ProductDto;
import gmarmari.demo.microservices.orders.api.ProductNotFoundException;
import gmarmari.demo.microservices.orders.api.ProductsAPi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ProductRestController implements ProductsAPi {

    private final ProductAdapter adapter;

    @Autowired
    public ProductRestController(ProductAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public List<ProductDto> getProducts() {
        return adapter.getProducts();
    }

    @Override
    public ProductDto getProductById(long productId) {
        return adapter.getProduct(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public ProductDetailsDto getProductDetailsById(long productId) {
        return adapter.getProductDetails(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public void deleteById(long productId) {
        adapter.delete(productId)
                .throwIfError(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An error occurred by deleting the product"));
    }

    @Override
    public void saveProduct(ProductDetailsDto productDetails) {
        adapter.save(productDetails)
                .throwIfError(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An error occurred by saving the product"));
    }
}
