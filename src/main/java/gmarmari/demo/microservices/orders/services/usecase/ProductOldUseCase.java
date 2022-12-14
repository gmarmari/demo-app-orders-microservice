package gmarmari.demo.microservices.orders.services.usecase;

import gmarmari.demo.microservices.orders.entities.ProductOldDao;
import gmarmari.demo.microservices.orders.repositories.ProductOldRepository;
import gmarmari.demo.microservices.orders.services.ProductOldService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductOldUseCase implements ProductOldService {

    private static final Sort SORT_BY_NAME_ASC = Sort.by(Sort.Order.asc("name").ignoreCase());

    private final ProductOldRepository repository;

    public ProductOldUseCase(ProductOldRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(ProductOldDao productDao) {
        repository.save(productDao);
    }

    @Override
    public void delete(long productId) {
        repository.deleteById(productId);
    }

    @Override
    public Optional<ProductOldDao> getProduct(long productId) {
        return repository.findById(productId);
    }

    @Override
    public List<ProductOldDao> getProducts() {
        return repository.findAll(SORT_BY_NAME_ASC);
    }
}
