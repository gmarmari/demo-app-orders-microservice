package gmarmari.demo.microservices.orders;

import gmarmari.demo.microservices.orders.api.*;
import gmarmari.demo.microservices.orders.entities.*;

import java.util.List;

import static gmarmari.demo.microservices.orders.CommonDataFactory.*;

@SuppressWarnings("unused")
public class OrderDataFactory {

    private OrderDataFactory() {
        // Hide constructor
    }


    //region dto


    public static OrderDto aOrderDto() {
        return aOrderDto(aLong());
    }
    public static OrderDto aOrderDto(long orderId) {
        int indexStatus = aInt(OrderStatusDto.values().length);
        OrderStatusDto status = OrderStatusDto.values()[indexStatus];

        int indexPayment = aInt(PaymentMethodDto.values().length);
        PaymentMethodDto paymentMethod = PaymentMethodDto.values()[indexPayment];

        return new OrderDto(
                orderId,
                aText(),
                aDate(),
                aDate().plusDays(3),
                status,
                paymentMethod,
                aPrizeDto(),
                aPrizeDto()
        );
    }

    public static OrderAddressDto aOrderAddressDto() {
        return aOrderAddressDto(aLong());
    }

    public static OrderAddressDto aOrderAddressDto(long orderId) {
        int index = aInt(OrderAddressTypeDto.values().length);
        OrderAddressTypeDto type = OrderAddressTypeDto.values()[index];

        return new OrderAddressDto(
                aLong(),
                orderId,
                type,
                aText(),
                aText(),
                aText(),
                aText(),
                aNullableText(),
                aText(),
                aNullableText(),
                aNullableText(),
                aNullableText()
        );
    }

    public static OrderDetailsDto aOrderDetailsDto() {
        return aOrderDetailsDto(aLong());
    }

    public static OrderDetailsDto aOrderDetailsDto(long orderId) {
        return new OrderDetailsDto(
                aOrderDto(orderId),
                List.of(aOrderAddressDto(orderId))
        );
    }

    public static OrderProductDto aOrderProductDto() {
        return new OrderProductDto(aLong(), aInt());
    }

    public static PrizeDto aPrizeDto() {
        int index = aInt(PrizeUnitDto.values().length);
        return new PrizeDto(aDouble(), PrizeUnitDto.values()[index]);
    }


    //endregion dto


    //region dao


    public static OrderDao aOrderDao() {
        return aOrderDao(false);
    }
    public static OrderDao aOrderDao(boolean withId) {
        OrderDao dao = new OrderDao();
        if (withId) {
            dao.setId(aLong());
        }
        dao.setName(aText());
        dao.setUsername("test-user");
        dao.setCreationDate(aDate());
        dao.setDeliveryDate(aDate().plusDays(3));

        int indexStatus = aInt(OrderStatusDao.values().length);
        dao.setStatus(OrderStatusDao.values()[indexStatus]);

        int indexPayment = aInt(PaymentMethodDao.values().length);
        dao.setPaymentMethod(PaymentMethodDao.values()[indexPayment]);

        dao.setPrize(aPrizeDao());
        dao.setDeliveryFee(aPrizeDao());

        return dao;
    }

    public static OrderAddressDao aOrderAddressDao() {
        return aOrderAddressDao(false);
    }

    public static OrderAddressDao aOrderAddressDao(boolean withId) {
        OrderAddressDao dao = new OrderAddressDao();
        if (withId) {
            dao.setId(aLong());
        }

        int index = aInt(OrderAddressTypeDao.values().length);
        dao.setType(OrderAddressTypeDao.values()[index]);

        dao.setName(aText());
        dao.setStreet(aText());
        dao.setPostalCode(aText());
        dao.setCity(aText());
        dao.setState(aNullableText());
        dao.setCountry(aText());
        dao.setTel(aNullableText());
        dao.setEmail(aNullableText());
        dao.setWebsite(aNullableText());
        return dao;
    }

    public static OrderDetailsDao aOrderDetailsDao() {
        return aOrderDetailsDao(aLong());
    }

    public static OrderDetailsDao aOrderDetailsDao(long orderId) {
        OrderDao order = aOrderDao();
        order.setId(orderId);
        OrderAddressDao shippingAddress = aOrderAddressDao(true);
        shippingAddress.setType(OrderAddressTypeDao.SHIPPING);
        shippingAddress.setOrderId(orderId);
        OrderAddressDao billingAddress = aOrderAddressDao(true);
        billingAddress.setType(OrderAddressTypeDao.BILLING);
        billingAddress.setOrderId(orderId);

        return new OrderDetailsDao(
                order,
                List.of(shippingAddress, billingAddress)
        );
    }

    public static OrderProductMappingDao aOrderProductMappingDao() {
        return aOrderProductMappingDao(false);
    }

    public static OrderProductMappingDao aOrderProductMappingDao(boolean withId) {
        OrderProductMappingDao dao = new OrderProductMappingDao();
        if (withId) {
            dao.setId(aLong());
        }
        dao.setOrderId(aLong());
        dao.setProductId(aLong());
        dao.setAmount(aInt());
        return dao;
    }

    public static PrizeDao aPrizeDao() {
        int index = aInt(PrizeUnitDao.values().length);
        return new PrizeDao(aDouble(), PrizeUnitDao.values()[index]);
    }


    //endregion dao


}
