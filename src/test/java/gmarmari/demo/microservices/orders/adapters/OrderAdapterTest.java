package gmarmari.demo.microservices.orders.adapters;


import gmarmari.demo.microservices.orders.api.*;
import gmarmari.demo.microservices.orders.entities.OrderAddressDao;
import gmarmari.demo.microservices.orders.entities.OrderDao;
import gmarmari.demo.microservices.orders.entities.OrderDetailsDao;
import gmarmari.demo.microservices.orders.entities.OrderProductMappingDao;
import gmarmari.demo.microservices.orders.services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static gmarmari.demo.microservices.orders.CommonDataFactory.aLong;
import static gmarmari.demo.microservices.orders.CommonDataFactory.aText;
import static gmarmari.demo.microservices.orders.OrderDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderAdapterTest {

    @Mock
    private OrderService service;

    @Captor
    private ArgumentCaptor<OrderDetailsDao> orderDetailsDaoCaptor;

    @Captor
    private ArgumentCaptor<List<OrderProductMappingDao>> orderProductMappingListCaptor;

    @InjectMocks
    private OrderAdapter adapter;


    @Test
    void getOrders() {
        // Given
        String username = aText();
        OrderDao daoA = aOrderDao(true);
        daoA.setUsername(username);
        OrderDao daoB = aOrderDao(true);
        daoB.setUsername(username);
        OrderDao daoC = aOrderDao(true);
        daoC.setUsername(username);

        when(service.getOrders(username)).thenReturn(List.of(daoA, daoB, daoC));

        // When
        List<OrderDto> list = adapter.getOrders(username);

        // Then
        assertThat(list).hasSize(3);
        verifyOrder(username, list.get(0), daoA);
        verifyOrder(username, list.get(1), daoB);
        verifyOrder(username, list.get(2), daoC);

        verifyNoMoreInteractions(service);
    }

    @Test
    void getOrder() {
        // Given
        long orderId = aLong();
        String username = aText();
        OrderDao dao = aOrderDao();
        dao.setId(orderId);
        dao.setUsername(username);

        when(service.getOrder(orderId)).thenReturn(Optional.of(dao));

        // When
        Optional<OrderDto> result = adapter.getOrder(orderId);

        // Then
        assertThat(result).isPresent();
        verifyOrder(username, result.get(), dao);

        verifyNoMoreInteractions(service);
    }

    @Test
    void getOrderDetails() {
        // Given
        long orderId = aLong();
        String username = aText();
        OrderDetailsDao dao = aOrderDetailsDao(orderId);
        dao.order.setUsername(username);

        when(service.getOrderDetails(orderId)).thenReturn(Optional.of(dao));

        // When
        Optional<OrderDetailsDto> result = adapter.getOrderDetails(orderId);

        // Then
        assertThat(result).isPresent();
        verifyOrder(username, result.get().order, dao.order);

        assertThat(result.get().addresses.size()).isEqualTo(dao.addresses.size());
        for (int i=0;i<dao.addresses.size();i++) {
            verifyOrderAddress(result.get().addresses.get(i), dao.addresses.get(i));
        }

        verifyNoMoreInteractions(service);
    }

    @Test
    void getOrderProductIds() {
        // Given
        long orderId = aLong();
        OrderProductMappingDao daoA = aOrderProductMappingDao();
        OrderProductMappingDao daoB = aOrderProductMappingDao();

        when(service.getOrderProductMappings(orderId)).thenReturn(List.of(daoA, daoB));

        // When
        List<OrderProductDto> list = adapter.getOrderProductIds(orderId);

        // Then
        assertThat(list).containsExactly(
                new OrderProductDto(daoA.getProductId(), daoA.getAmount()),
                new OrderProductDto(daoB.getProductId(), daoB.getAmount())
        );
        verifyNoMoreInteractions(service);
    }

    @Test
    void delete() {
        // Given
        long orderId = aLong();

        // When
        Response response = adapter.delete(orderId);

        // Then
        assertThat(response).isEqualTo(Response.OK);
        verify(service).delete(orderId);
        verifyNoMoreInteractions(service);
    }

    @Test
    void delete_error() {
        // Given
        long orderId = aLong();

        doThrow(new NullPointerException()).when(service).delete(orderId);

        // When
        Response response = adapter.delete(orderId);

        // Then
        assertThat(response).isEqualTo(Response.ERROR);
        verify(service).delete(orderId);
        verifyNoMoreInteractions(service);
    }

    @Test
    void save() {
        // Given
        long orderId = aLong();
        String username = aText();
        OrderDetailsDto dto = aOrderDetailsDto(orderId);

        // When
        Response result = adapter.save(username, dto);

        // Then
        assertThat(result).isEqualTo(Response.OK);

        verify(service).save(orderDetailsDaoCaptor.capture());
        verifyNoMoreInteractions(service);
        OrderDetailsDao dao = orderDetailsDaoCaptor.getValue();

        verifyOrder(username, dto.order, dao.order);

        assertThat(dto.addresses.size()).isEqualTo(dao.addresses.size());
        for (int i=0;i<dao.addresses.size();i++) {
            verifyOrderAddress(dto.addresses.get(i), dao.addresses.get(i));
        }
    }

    @Test
    void save_error() {
        // Given
        long orderId = aLong();
        String username = aText();
        OrderDetailsDto dto = aOrderDetailsDto(orderId);

        doThrow(new NullPointerException()).when(service).save(any());

        // When
        Response result = adapter.save(username, dto);

        // Then
        assertThat(result).isEqualTo(Response.ERROR);
        verifyNoMoreInteractions(service);
    }

    @Test
    void saveOrderProducts() {
        // Given
        long orderId = aLong();
        List<OrderProductDto> dtoList = List.of(aOrderProductDto(), aOrderProductDto());

        // When
        Response result = adapter.saveOrderProducts(orderId, dtoList);

        // Then
        assertThat(result).isEqualTo(Response.OK);

        verify(service).saveOrderProductMappings(eq(orderId), orderProductMappingListCaptor.capture());

        List<OrderProductMappingDao> daoList = orderProductMappingListCaptor.getValue();
        assertThat(daoList).hasSize(2);
        verifyOrderProducts(orderId, dtoList.get(0), daoList.get(0));
        verifyOrderProducts(orderId, dtoList.get(1), daoList.get(1));
        verifyNoMoreInteractions(service);
    }

    @Test
    void saveOrderProducts_error() {
        // Given
        long orderId = aLong();
        List<OrderProductDto> dtoList = List.of(aOrderProductDto(), aOrderProductDto());

        doThrow(new NullPointerException()).when(service).saveOrderProductMappings(eq(orderId), any());

        // When
        Response result = adapter.saveOrderProducts(orderId, dtoList);

        // Then
        assertThat(result).isEqualTo(Response.ERROR);
        verifyNoMoreInteractions(service);
    }


    private void verifyOrder(String username, OrderDto dto, OrderDao dao) {
        assertThat(dto.id).isEqualTo(dao.getId());
        assertThat(dto.name).isEqualTo(dao.getName());
        assertThat(username).isEqualTo(dao.getUsername());
        assertThat(dto.creationDate).isEqualTo(dao.getCreationDate());
        assertThat(dto.deliveryDate).isEqualTo(dao.getDeliveryDate());
        assertThat(dto.status.name()).isEqualTo(dao.getStatus().name());
        assertThat(dto.paymentMethod.name()).isEqualTo(dao.getPaymentMethod().name());
        assertThat(dto.prize.amount).isEqualTo(dao.getPrize().amount);
        assertThat(dto.prize.unit.name()).isEqualTo(dao.getPrize().unit.name());
        assertThat(dto.deliveryFee.amount).isEqualTo(dao.getDeliveryFee().amount);
        assertThat(dto.deliveryFee.unit.name()).isEqualTo(dao.getDeliveryFee().unit.name());
    }

    private void verifyOrderAddress(OrderAddressDto dto, OrderAddressDao dao) {
        assertThat(dto.id).isEqualTo(dao.getId());
        assertThat(dto.orderId).isEqualTo(dao.getOrderId());
        assertThat(dto.type.name()).isEqualTo(dao.getType().name());
        assertThat(dto.name).isEqualTo(dao.getName());
        assertThat(dto.street).isEqualTo(dao.getStreet());
        assertThat(dto.postalCode).isEqualTo(dao.getPostalCode());
        assertThat(dto.city).isEqualTo(dao.getCity());
        assertThat(dto.state).isEqualTo(dao.getState());
        assertThat(dto.country).isEqualTo(dao.getCountry());
        assertThat(dto.tel).isEqualTo(dao.getTel());
        assertThat(dto.email).isEqualTo(dao.getEmail());
        assertThat(dto.website).isEqualTo(dao.getWebsite());
    }

    private void verifyOrderProducts(long orderId, OrderProductDto dto, OrderProductMappingDao dao) {
        assertThat(orderId).isEqualTo(dao.getOrderId());
        assertThat(dto.productId).isEqualTo(dao.getProductId());
        assertThat(dto.amount).isEqualTo(dao.getAmount());
    }

}