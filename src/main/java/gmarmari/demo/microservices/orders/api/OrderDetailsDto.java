package gmarmari.demo.microservices.orders.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Schema(name = "OrderDetails_V01")
public class OrderDetailsDto {

    @NotNull
    public final OrderDto order;

    @NotNull
    public final List<OrderAddressDto> addresses;


    public OrderDetailsDto(@JsonProperty("order") OrderDto order,
                           @JsonProperty("addresses") List<OrderAddressDto> addresses) {
        this.order = order;
        this.addresses = addresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetailsDto that = (OrderDetailsDto) o;
        return Objects.equals(order, that.order) && Objects.equals(addresses, that.addresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, addresses);
    }

    @Override
    public String toString() {
        return "OrderDetailsDto{" +
                "order=" + order +
                ", addresses=" + addresses +
                '}';
    }
}
