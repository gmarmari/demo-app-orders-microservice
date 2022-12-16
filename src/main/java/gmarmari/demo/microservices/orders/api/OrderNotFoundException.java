package gmarmari.demo.microservices.orders.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrderNotFoundException extends ResponseStatusException {
    public OrderNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Order not found");
    }
}
