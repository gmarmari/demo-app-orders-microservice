package gmarmari.demo.microservices.orders.repositories;

import gmarmari.demo.microservices.orders.entities.OrderDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static gmarmari.demo.microservices.orders.OrderDataFactory.aOrderDao;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void save_findById() {
        // Given
        OrderDao order = aOrderDao();
        Long id = entityManager.persistAndGetId(order, Long.class);

        // When
        Optional<OrderDao> resultOptional = repository.findById(id);

        // Then
        assertThat(resultOptional).isPresent();
        OrderDao result = resultOptional.get();

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(order.getName());
        assertThat(result.getUsername()).isEqualTo(order.getUsername());
        assertThat(result.getCreationDate()).isEqualTo(order.getCreationDate());
        assertThat(result.getDeliveryDate()).isEqualTo(order.getDeliveryDate());
        assertThat(result.getStatus()).isEqualTo(order.getStatus());
        assertThat(result.getPaymentMethod()).isEqualTo(order.getPaymentMethod());
        assertThat(result.getPrize()).isEqualTo(order.getPrize());
        assertThat(result.getDeliveryFee()).isEqualTo(order.getDeliveryFee());
    }

    @Test
    void findByUsername_sortByDate() {
        // Given
        ZonedDateTime now = ZonedDateTime.now();
        String username = "Mickey";
        String anotherUsername = "Donalt";

        OrderDao orderA = aOrderDao();
        orderA.setDeliveryDate(now.minusMinutes(1));
        orderA.setUsername(username);
        entityManager.persist(orderA);

        OrderDao orderB = aOrderDao();
        orderB.setDeliveryDate(now.minusHours(1));
        orderB.setUsername(username);
        entityManager.persist(orderB);

        OrderDao orderC = aOrderDao();
        orderC.setDeliveryDate(now.minusDays(1));
        orderC.setUsername(username);
        entityManager.persist(orderC);

        OrderDao orderD = aOrderDao();
        orderD.setUsername(anotherUsername);
        entityManager.persist(orderC);

        // When
        List<OrderDao> list = repository.findByUsername(username, Sort.by("creationDate").ascending());

        // Then
        assertThat(list).containsExactly(orderA, orderB, orderC);
    }

}