package gmarmari.demo.microservices.orders.api.controllers;


import gmarmari.demo.microservices.orders.adapters.OrderAdapter;
import gmarmari.demo.microservices.orders.api.OrderDetailsDto;
import gmarmari.demo.microservices.orders.api.OrderDto;
import gmarmari.demo.microservices.orders.api.OrderNotFoundException;
import gmarmari.demo.microservices.orders.api.OrdersApi;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class OrderRestController implements OrdersApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderRestController.class);

    private final OrderAdapter adapter;

    @Autowired
    public OrderRestController(OrderAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    @CircuitBreaker(name = "breaker", fallbackMethod = "getOrdersFallback")
    public List<OrderDto> getOrders() {
        return adapter.getOrders(getUsername());
    }

    public List<OrderDto> getOrdersFallback(Throwable t) {
        LOGGER.warn("Fallback method for getOrders", t);
        return List.of();
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
    @CircuitBreaker(name = "breaker", fallbackMethod = "getOrderProductIdsFallback")
    public List<Long> getOrderProductIds(long orderId) {
        return adapter.getOrderProductIds(orderId);
    }

    public List<Long> getOrderProductIdsFallback(long orderId, Throwable t) {
        LOGGER.warn("Fallback method for getOrderProductIds", t);
        return List.of();
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
    public void saveOrderProducts(long orderId, List<Long> productIds) {
        adapter.saveOrderProducts(orderId, productIds)
                .throwIfError(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An error occurred by saving the order products"));
    }

    private String getUsername() {
        // Authentication not implemented yet
        return "super_user";
    }
}
