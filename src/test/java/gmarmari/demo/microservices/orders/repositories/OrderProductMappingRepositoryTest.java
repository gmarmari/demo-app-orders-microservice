package gmarmari.demo.microservices.orders.repositories;

import gmarmari.demo.microservices.orders.entities.OrderDao;
import gmarmari.demo.microservices.orders.entities.OrderProductMappingDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Collectors;

import static gmarmari.demo.microservices.orders.CommonDataFactory.aLong;
import static gmarmari.demo.microservices.orders.OrderDataFactory.aOrderDao;
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
        OrderDao orderA = aOrderDao();
        Long orderIdA = entityManager.persistAndGetId(orderA, Long.class);

        OrderDao orderB = aOrderDao();
        Long orderIdB = entityManager.persistAndGetId(orderB, Long.class);

        long productId1 = 1;
        long productId2 = 2;

        OrderProductMappingDao mapping1 = new OrderProductMappingDao();
        mapping1.setOrderId(orderIdA);
        mapping1.setProductId(productId1);

        OrderProductMappingDao mapping2 = new OrderProductMappingDao();
        mapping2.setOrderId(orderIdA);
        mapping2.setProductId(productId2);

        OrderProductMappingDao mapping3 = new OrderProductMappingDao();
        mapping3.setOrderId(orderIdB);
        mapping3.setProductId(productId1);

        // When
        repository.save(mapping1);
        repository.save(mapping2);
        repository.save(mapping3);

        // Then
        assertThat(repository.findByOrderId(orderIdA).stream()
                .map(OrderProductMappingDao::getProductId).collect(Collectors.toList())).containsExactlyInAnyOrder(productId1, productId2);
        assertThat(repository.findByOrderId(orderIdB).stream()
                .map(OrderProductMappingDao::getProductId).collect(Collectors.toList())).containsExactly(productId1);
    }

    @Test
    void deleteByOrderId() {
        // Given
        OrderDao orderA = aOrderDao();
        Long orderIdA = entityManager.persistAndGetId(orderA, Long.class);

        OrderDao orderB = aOrderDao();
        Long orderIdB = entityManager.persistAndGetId(orderB, Long.class);

        long productId1 = 1;
        long productId2 = 2;

        OrderProductMappingDao mapping1 = new OrderProductMappingDao();
        mapping1.setOrderId(orderIdA);
        mapping1.setProductId(productId1);
        entityManager.persist(mapping1);

        OrderProductMappingDao mapping2 = new OrderProductMappingDao();
        mapping2.setOrderId(orderIdA);
        mapping2.setProductId(productId2);
        entityManager.persist(mapping2);

        OrderProductMappingDao mapping3 = new OrderProductMappingDao();
        mapping3.setOrderId(orderIdB);
        mapping3.setProductId(productId1);
        entityManager.persist(mapping3);

        OrderProductMappingDao mapping4 = new OrderProductMappingDao();
        mapping4.setOrderId(orderIdB);
        mapping4.setProductId(productId2);
        entityManager.persist(mapping4);

        // When
        repository.deleteByOrderId(orderIdA);

        // Then
        assertThat(repository.findByOrderId(orderIdA)).isEmpty();
        assertThat(repository.findByOrderId(orderIdB)).hasSize(2);
    }

}