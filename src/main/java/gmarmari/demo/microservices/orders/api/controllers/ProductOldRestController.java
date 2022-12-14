package gmarmari.demo.microservices.orders.api.controllers;

import gmarmari.demo.microservices.orders.adapters.ProductOldAdapter;
import gmarmari.demo.microservices.orders.api.ProductOldApi;
import gmarmari.demo.microservices.orders.api.ProductOldDto;
import gmarmari.demo.microservices.orders.api.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ProductOldRestController implements ProductOldApi {

    private final ProductOldAdapter adapter;

    @Autowired
    public ProductOldRestController(ProductOldAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public List<ProductOldDto> getProducts() {
        return adapter.getProducts();
    }

    @Override
    public ProductOldDto getProductById(long id) {
        return adapter.getProduct(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public void deleteById(long id) {
        adapter.delete(id)
                .throwIfError(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An error occurred by deleting the product"));
    }

    @Override
    public void saveProduct(ProductOldDto product) {
        adapter.save(product)
                .throwIfError(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An error occurred by saving the product"));

    }
}
