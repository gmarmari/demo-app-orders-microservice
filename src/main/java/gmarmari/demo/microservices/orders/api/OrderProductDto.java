package gmarmari.demo.microservices.orders.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(name = "OrderProduct_V01")
public class OrderProductDto {

    public final long productId;

    public final int amount;

    public OrderProductDto(@JsonProperty("productId") long productId,
                           @JsonProperty("amount") int amount) {
        this.productId = productId;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProductDto that = (OrderProductDto) o;
        return productId == that.productId && amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, amount);
    }

    @Override
    public String toString() {
        return "OrderProductDto{" +
                "productId=" + productId +
                ", amount=" + amount +
                '}';
    }
}
