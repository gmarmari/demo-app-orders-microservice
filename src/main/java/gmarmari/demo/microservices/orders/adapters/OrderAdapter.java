package gmarmari.demo.microservices.orders.adapters;

import gmarmari.demo.microservices.orders.api.*;
import gmarmari.demo.microservices.orders.entities.*;
import gmarmari.demo.microservices.orders.services.OrderService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderAdapter {

    private final OrderService service;

    public OrderAdapter(OrderService service) {
        this.service = service;
    }

    public List<OrderDto> getOrders(String username) {
        return service.getOrders(username).stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public Optional<OrderDto> getOrder(long orderId) {
        return service.getOrder(orderId).map(this::convert);
    }

    public Optional<OrderDetailsDto> getOrderDetails(long orderId) {
        return service.getOrderDetails(orderId).map(this::convert);
    }

    public List<ProductDto> getOrderProducts(long orderId) {
        return service.getOrderProducts(orderId).stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public Response delete(long orderId) {
        try {
            service.delete(orderId);
            return Response.OK;
        } catch (Exception e) {
            return Response.ERROR;
        }
    }

    public Response save(String username, OrderDetailsDto orderDetails) {
        try {
            service.save(convert(orderDetails, username));
            return Response.OK;
        } catch (Exception e) {
            return Response.ERROR;
        }
    }

    public Response saveOrderProducts(long orderId, List<ProductDto> products) {
        try {
            service.saveOrderProducts(orderId, products.stream().map(this::convert).collect(Collectors.toList()));
            return Response.OK;
        } catch (Exception e) {
            return Response.ERROR;
        }
    }



    //region convert methods

    private OrderDto convert(OrderDao dao) {
        return new OrderDto(
                dao.getId(),
                dao.getName(),
                dao.getCreationDate(),
                dao.getDeliveryDate(),
                OrderStatusDto.valueOf(dao.getStatus().name()),
                PaymentMethodDto.valueOf(dao.getPaymentMethod().name()),
                convert(dao.getPrize()),
                convert(dao.getDeliveryFee())
        );
    }

    private OrderDetailsDto convert(OrderDetailsDao dao) {
        return new OrderDetailsDto(
                convert(dao.order),
                dao.addresses.stream().map(this::convert).collect(Collectors.toList())
        );
    }

    private OrderAddressDto convert(OrderAddressDao dao) {
        return new OrderAddressDto(
                dao.getId(),
                dao.getOrderId(),
                OrderAddressTypeDto.valueOf(dao.getType().name()),
                dao.getName(),
                dao.getStreet(),
                dao.getPostalCode(),
                dao.getCity(),
                dao.getState(),
                dao.getCountry(),
                dao.getTel(),
                dao.getEmail(),
                dao.getWebsite()
        );
    }

    private ProductDto convert(ProductDao dao) {
        return new ProductDto(
                dao.getId(),
                dao.getName(),
                dao.getAmount(),
                convert(dao.getPrize())
        );
    }

    private PrizeDto convert(PrizeDao dao) {
        return new PrizeDto(
                dao.amount,
                PrizeUnitDto.valueOf(dao.unit.name())
        );
    }



    private OrderDao convert(OrderDto dto, String username) {
        OrderDao dao = new OrderDao();
        dao.setId(dto.id);
        dao.setName(dto.name);
        dao.setUsername(username);
        dao.setCreationDate(dto.creationDate);
        dao.setDeliveryDate(dto.deliveryDate);
        dao.setStatus(OrderStatusDao.valueOf(dto.status.name()));
        dao.setPaymentMethod(PaymentMethodDao.valueOf(dto.paymentMethod.name()));
        dao.setPrize(convert(dto.prize));
        dao.setDeliveryFee(convert(dto.deliveryFee));
        return dao;
    }

    private OrderDetailsDao convert(OrderDetailsDto dto, String username) {
        return new OrderDetailsDao(
                convert(dto.order, username),
                dto.addresses.stream().map(this::convert).collect(Collectors.toList())
        );
    }

    private OrderAddressDao convert(OrderAddressDto dto) {
        OrderAddressDao dao = new OrderAddressDao();
        dao.setId(dto.id);
        dao.setOrderId(dto.orderId);
        dao.setType(OrderAddressTypeDao.valueOf(dto.type.name()));
        dao.setName(dto.name);
        dao.setStreet(dto.street);
        dao.setPostalCode(dto.postalCode);
        dao.setCity(dto.city);
        dao.setState(dto.state);
        dao.setCountry(dto.country);
        dao.setTel(dto.tel);
        dao.setEmail(dto.email);
        dao.setWebsite(dto.website);
        return dao;
    }

    private ProductDao convert(ProductDto dto) {
        ProductDao dao = new ProductDao();
        dao.setId(dto.id);
        dao.setName(dto.name);
        dao.setAmount(dto.amount);
        dao.setPrize(convert(dto.prize));
        return dao;
    }


    private PrizeDao convert(PrizeDto dto) {
        return new PrizeDao(
                dto.amount,
                PrizeUnitDao.valueOf(dto.unit.name())
        );
    }

    //endregion convert methods

}
