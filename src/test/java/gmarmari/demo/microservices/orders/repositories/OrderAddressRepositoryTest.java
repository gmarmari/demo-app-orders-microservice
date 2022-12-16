package gmarmari.demo.microservices.orders.repositories;


import gmarmari.demo.microservices.orders.entities.OrderAddressDao;
import gmarmari.demo.microservices.orders.entities.OrderAddressTypeDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static gmarmari.demo.microservices.orders.OrderDataFactory.aOrderAddressDao;
import static gmarmari.demo.microservices.orders.OrderDataFactory.aOrderDao;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DataJpaTest
class OrderAddressRepositoryTest {

    @Autowired
    private OrderAddressRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void save_findById() {
        // Given
        Long orderId = entityManager.persistAndGetId(aOrderDao(), Long.class);

        OrderAddressDao address = aOrderAddressDao();
        address.setOrderId(orderId);
        Long id = entityManager.persistAndGetId(address, Long.class);

        // When
        Optional<OrderAddressDao> resultOptional = repository.findById(id);

        // Then
        assertThat(resultOptional).isPresent();
        OrderAddressDao result = resultOptional.get();

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getOrderId()).isEqualTo(orderId);
        assertThat(result.getType()).isEqualTo(address.getType());
        assertThat(result.getName()).isEqualTo(address.getName());
        assertThat(result.getStreet()).isEqualTo(address.getStreet());
        assertThat(result.getPostalCode()).isEqualTo(address.getPostalCode());
        assertThat(result.getCity()).isEqualTo(address.getCity());
        assertThat(result.getState()).isEqualTo(address.getState());
        assertThat(result.getCountry()).isEqualTo(address.getCountry());
        assertThat(result.getTel()).isEqualTo(address.getTel());
        assertThat(result.getEmail()).isEqualTo(address.getEmail());
        assertThat(result.getWebsite()).isEqualTo(address.getWebsite());
    }

    @Test
    void save_findByOrderId() {
        // Given
        Long orderId = entityManager.persistAndGetId(aOrderDao(), Long.class);

        OrderAddressDao addressBilling = aOrderAddressDao();
        addressBilling.setOrderId(orderId);
        addressBilling.setType(OrderAddressTypeDao.BILLING);
        entityManager.persist(addressBilling);

        OrderAddressDao addressShipping = aOrderAddressDao();
        addressShipping.setOrderId(orderId);
        addressShipping.setType(OrderAddressTypeDao.SHIPPING);
        entityManager.persist(addressShipping);

        Sort sort = Sort.by("type").ascending();
        // When
        List<OrderAddressDao> list = repository.findByOrderId(orderId, sort);

        // Then
        assertThat(list).containsExactly(addressBilling, addressShipping);
    }

    @Test
    void save_findByOrderIdAndType() {
        // Given
        Long orderId = entityManager.persistAndGetId(aOrderDao(), Long.class);

        OrderAddressDao addressBilling = aOrderAddressDao();
        addressBilling.setOrderId(orderId);
        addressBilling.setType(OrderAddressTypeDao.BILLING);
        entityManager.persist(addressBilling);

        OrderAddressDao addressShipping = aOrderAddressDao();
        addressShipping.setOrderId(orderId);
        addressShipping.setType(OrderAddressTypeDao.SHIPPING);
        entityManager.persist(addressShipping);

        // When
        Optional<OrderAddressDao> resultOptional = repository.findByOrderIdAndType(orderId, OrderAddressTypeDao.BILLING);

        // Then
        assertThat(resultOptional).isPresent();
        assertThat(resultOptional.get()).isEqualTo(addressBilling);
    }

    @Test
    void deleteByOrderId() {
        // Given
        Long orderId = entityManager.persistAndGetId(aOrderDao(), Long.class);

        OrderAddressDao addressBilling = aOrderAddressDao();
        addressBilling.setOrderId(orderId);
        addressBilling.setType(OrderAddressTypeDao.BILLING);
        entityManager.persist(addressBilling);

        OrderAddressDao addressShipping = aOrderAddressDao();
        addressShipping.setOrderId(orderId);
        addressShipping.setType(OrderAddressTypeDao.SHIPPING);
        entityManager.persist(addressShipping);

        // When
        repository.deleteByOrderId(orderId);

        // Then
        // When
        assertThat(repository.findByOrderId(orderId, Sort.by("type"))).isEmpty();
    }

    @Test
    void unique_orderId_type() {
        // Given
        Long orderId = entityManager.persistAndGetId(aOrderDao(), Long.class);

        OrderAddressDao address = aOrderAddressDao();
        address.setOrderId(orderId);
        entityManager.persist(address);

        OrderAddressDao anotherAddress = aOrderAddressDao();
        anotherAddress.setOrderId(orderId);
        anotherAddress.setType(address.getType());

        // When
        // Then
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(anotherAddress));
    }


}