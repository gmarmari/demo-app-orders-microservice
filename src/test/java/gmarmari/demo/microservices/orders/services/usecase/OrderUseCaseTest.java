package gmarmari.demo.microservices.orders.services.usecase;

import gmarmari.demo.microservices.orders.entities.OrderAddressDao;
import gmarmari.demo.microservices.orders.entities.OrderDao;
import gmarmari.demo.microservices.orders.entities.OrderDetailsDao;
import gmarmari.demo.microservices.orders.entities.OrderProductMappingDao;
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
        Optional<OrderDetailsDao> result = useCase.getOrderDetails(order.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().order).isEqualTo(order);
        assertThat(result.get().addresses).containsExactly(addressA, addressB);
        assertThat(result.get().products).containsExactly(mappingA, mappingB);
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(orderAddressRepository);
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
        verify(orderAddressRepository).flush();
        orderDetails.addresses.forEach(address -> verify(orderAddressRepository, atLeast(1)).save(address));
        verifyNoMoreInteractions(orderAddressRepository);

        verify(orderProductMappingRepository).deleteByOrderId(orderDetails.order.getId());
        verify(orderProductMappingRepository).flush();
        orderDetails.products.forEach(product -> verify(orderProductMappingRepository, atLeast(1)).save(product));
        verifyNoMoreInteractions(orderProductMappingRepository);
    }

}