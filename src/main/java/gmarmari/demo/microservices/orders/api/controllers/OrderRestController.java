package gmarmari.demo.microservices.orders.api.controllers;


import gmarmari.demo.microservices.orders.adapters.OrderAdapter;
import gmarmari.demo.microservices.orders.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class OrderRestController implements OrdersApi {

    private final OrderAdapter adapter;

    @Autowired
    public OrderRestController(OrderAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public List<OrderDto> getOrders() {
        return adapter.getOrders(getUsername());
    }

    @Override
    public OrderDto getOrderById(long orderId) {
        return adapter.getOrder(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public OrderDetailsDto getOrderDetailsById(long orderId) {
        return adapter.getOrderDetails(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public List<ProductDto> getOrderProducts(long orderId) {
        return adapter.getOrderProducts(orderId);
    }

    @Override
    public void deleteById(long orderId) {
        adapter.delete(orderId)
                .throwIfError(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An error occurred by deleting the order"));
    }

    @Override
    public void saveOrder(OrderDetailsDto orderDetails) {
        adapter.save(getUsername(), orderDetails)
                .throwIfError(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An error occurred by saving the order"));
    }

    @Override
    public void saveOrderProducts(long orderId, List<ProductDto> products) {
        adapter.saveOrderProducts(orderId,products)
                .throwIfError(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An error occurred by saving the order products"));
    }

    private String getUsername() {
        // Authentication not implemented yet
        return "super_user";
    }
}
