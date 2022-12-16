package gmarmari.demo.microservices.orders.repositories;

import gmarmari.demo.microservices.orders.entities.OrderDao;
import gmarmari.demo.microservices.orders.entities.OrderProductMappingDao;
import gmarmari.demo.microservices.orders.entities.ProductDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Collectors;

import static gmarmari.demo.microservices.orders.OrderDataFactory.aOrderDao;
import static gmarmari.demo.microservices.orders.ProductDataFactory.aProductDao;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class OrderProductMappingRepositoryTest {

    @Autowired
    private OrderProductMappingRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void save_findAllByOrderId() {
        // Given
        ProductDao product1 = aProductDao();
        entityManager.persist(product1);

        ProductDao product2 = aProductDao();
        entityManager.persist(product2);

        OrderDao orderA = aOrderDao();
        Long orderIdA = entityManager.persistAndGetId(orderA, Long.class);

        OrderDao orderB = aOrderDao();
        Long orderIdB = entityManager.persistAndGetId(orderB, Long.class);

        OrderProductMappingDao mapping1 = new OrderProductMappingDao();
        mapping1.setOrder(orderA);
        mapping1.setProduct(product1);

        OrderProductMappingDao mapping2 = new OrderProductMappingDao();
        mapping2.setOrder(orderA);
        mapping2.setProduct(product2);

        OrderProductMappingDao mapping3 = new OrderProductMappingDao();
        mapping3.setOrder(orderB);
        mapping3.setProduct(product1);

        // When
        repository.save(mapping1);
        repository.save(mapping2);
        repository.save(mapping3);

        // Then
        assertThat(repository.findByOrderId(orderIdA).stream()
                .map(OrderProductMappingDao::getProduct).collect(Collectors.toList())).containsExactlyInAnyOrder(product1, product2);
        assertThat(repository.findByOrderId(orderIdB).stream()
                .map(OrderProductMappingDao::getProduct).collect(Collectors.toList())).containsExactly(product1);
    }

    @Test
    void deleteByOrderId() {
        // Given
        ProductDao product1 = aProductDao();
        entityManager.persist(product1);

        ProductDao product2 = aProductDao();
        entityManager.persist(product2);

        OrderDao orderA = aOrderDao();
        Long orderIdA = entityManager.persistAndGetId(orderA, Long.class);

        OrderDao orderB = aOrderDao();
        Long orderIdB = entityManager.persistAndGetId(orderB, Long.class);

        OrderProductMappingDao mapping1 = new OrderProductMappingDao();
        mapping1.setOrder(orderA);
        mapping1.setProduct(product1);
        entityManager.persist(mapping1);

        OrderProductMappingDao mapping2 = new OrderProductMappingDao();
        mapping2.setOrder(orderA);
        mapping2.setProduct(product2);
        entityManager.persist(mapping2);

        OrderProductMappingDao mapping3 = new OrderProductMappingDao();
        mapping3.setOrder(orderB);
        mapping3.setProduct(product1);
        entityManager.persist(mapping3);

        OrderProductMappingDao mapping4 = new OrderProductMappingDao();
        mapping4.setOrder(orderB);
        mapping4.setProduct(product2);
        entityManager.persist(mapping4);

        // When
        repository.deleteByOrderId(orderIdA);

        // Then
        assertThat(repository.findByOrderId(orderIdA)).isEmpty();
        assertThat(repository.findByOrderId(orderIdB)).hasSize(2);
    }

}