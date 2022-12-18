package gmarmari.demo.microservices.orders.services.usecase;

import gmarmari.demo.microservices.orders.entities.*;
import gmarmari.demo.microservices.orders.repositories.OrderAddressRepository;
import gmarmari.demo.microservices.orders.repositories.OrderProductMappingRepository;
import gmarmari.demo.microservices.orders.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static gmarmari.demo.microservices.orders.CommonDataFactory.aLong;
import static gmarmari.demo.microservices.orders.CommonDataFactory.aText;
import static gmarmari.demo.microservices.orders.OrderDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderAddressRepository orderAddressRepository;

    @Mock
    private OrderProductMappingRepository orderProductMappingRepository;

    @InjectMocks
    private OrderUseCase useCase;

    @Test
    void getOrders() {
        // Given
        String username = aText();
        OrderDao orderA = aOrderDao(true);
        OrderDao orderB = aOrderDao(true);
        OrderDao orderC = aOrderDao(true);

        when(orderRepository.findByUsername(username, Sort.by("creationDate").ascending())).thenReturn(List.of(orderA, orderB, orderC));

        // When
        List<OrderDao> list = useCase.getOrders(username);

        // Then
        assertThat(list).containsExactly(orderA, orderB, orderC);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(orderAddressRepository);
        verifyNoInteractions(orderProductMappingRepository);
    }

    @Test
    void getOrder() {
        // Given
        OrderDao order = aOrderDao(true);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // When
        Optional<OrderDao> result = useCase.getOrder(order.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(order);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(orderAddressRepository);
        verifyNoInteractions(orderProductMappingRepository);
    }

    @Test
    void delete() {
        // Given
        long orderId = aLong();

        // When
        useCase.delete(orderId);

        // Then
        verify(orderProductMappingRepository).deleteByOrderId(orderId);
        verifyNoMoreInteractions(orderProductMappingRepository);

        verify(orderAddressRepository).deleteByOrderId(orderId);
        verifyNoMoreInteractions(orderAddressRepository);

        verify(orderRepository).deleteById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void getOrderDetails() {
        // Given
        OrderDao order = aOrderDao(true);
        OrderAddressDao addressA = aOrderAddressDao();
        addressA.setOrderId(order.getId());
        OrderAddressDao addressB = aOrderAddressDao();
        addressB.setOrderId(order.getId());

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderAddressRepository.findByOrderId(order.getId(), Sort.by("type").ascending()))
                .thenReturn(List.of(addressA, addressB));

        // When
        Optional<OrderDetailsDao> result = useCase.getOrderDetails(order.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().order).isEqualTo(order);
        assertThat(result.get().addresses).containsExactly(addressA, addressB);
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(orderAddressRepository);
        verifyNoInteractions(orderProductMappingRepository);
    }

    @Test
    void getOrderProductIds() {
        // Given
        OrderDao order = aOrderDao(true);
        long productIdA = aLong();
        long productIdB = aLong();

        OrderProductMappingDao mappingA = new OrderProductMappingDao();
        mappingA.setOrderId(order.getId());
        mappingA.setProductId(productIdA);

        OrderProductMappingDao mappingB = new OrderProductMappingDao();
        mappingB.setOrderId(order.getId());
        mappingB.setProductId(productIdB);

        when(orderProductMappingRepository.findByOrderId(order.getId()))
                .thenReturn(List.of(mappingA, mappingB));

        // When
        List<Long> list = useCase.getOrderProductIds(order.getId());

        // Then
        assertThat(list).containsExactly(productIdA, productIdB);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(orderAddressRepository);
        verifyNoMoreInteractions(orderProductMappingRepository);
    }

    @Test
    void save() {
        // Given
        OrderDetailsDao orderDetails = aOrderDetailsDao();

        when(orderRepository.save(orderDetails.order)).thenReturn(orderDetails.order);

        // When
        useCase.save(orderDetails);

        // Then
        verify(orderRepository).save(orderDetails.order);
        verifyNoMoreInteractions(orderRepository);

        verify(orderAddressRepository).deleteByOrderId(orderDetails.order.getId());
        orderDetails.addresses.forEach(address -> verify(orderAddressRepository).save(address));
        verifyNoMoreInteractions(orderAddressRepository);

        verifyNoInteractions(orderProductMappingRepository);
    }

    @Test
    void saveOrderProducts() {
        // Given
        long orderId = aLong();
        List<Long> productIds = List.of(aLong(), aLong(), aLong());

        // When
        useCase.saveOrderProducts(orderId, productIds);

        // Then
        verify(orderProductMappingRepository).deleteByOrderId(orderId);
        productIds.forEach(productId -> {
            OrderProductMappingDao mapping = new OrderProductMappingDao();
            mapping.setOrderId(orderId);
            mapping.setProductId(productId);
            verify(orderProductMappingRepository, atLeast(1)).save(mapping);
        });
        verifyNoMoreInteractions(orderProductMappingRepository);

        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(orderAddressRepository);
    }

}