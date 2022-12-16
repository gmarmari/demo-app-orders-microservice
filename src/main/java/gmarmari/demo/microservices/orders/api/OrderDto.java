package gmarmari.demo.microservices.orders.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.Objects;

@Schema(name = "Order_V01")
public class OrderDto {

    public final long id;

    @NotBlank
    @Size(max = 100)
    public final String name;

    @NotNull
    public final ZonedDateTime creationDate;

    @Nullable
    public final ZonedDateTime deliveryDate;


    @NotNull
    public final OrderStatusDto status;

    @NotNull
    public final PaymentMethodDto paymentMethod;

    @NotNull
    public final PrizeDto prize;

    @NotNull
    public final PrizeDto deliveryFee;

    public OrderDto(@JsonProperty("id") long id,
                    @JsonProperty("name") String name,
                    @JsonProperty("creationDate") ZonedDateTime creationDate,
                    @Nullable @JsonProperty("deliveryDate") ZonedDateTime deliveryDate,
                    @JsonProperty("status") OrderStatusDto status,
                    @JsonProperty("paymentMethod") PaymentMethodDto paymentMethod,
                    @JsonProperty("prize") PrizeDto prize,
                    @JsonProperty("deliveryFee") PrizeDto deliveryFee) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.deliveryDate = deliveryDate;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.prize = prize;
        this.deliveryFee = deliveryFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return id == orderDto.id && Objects.equals(name, orderDto.name) && Objects.equals(creationDate, orderDto.creationDate) && Objects.equals(deliveryDate, orderDto.deliveryDate) && status == orderDto.status && paymentMethod == orderDto.paymentMethod && Objects.equals(prize, orderDto.prize) && Objects.equals(deliveryFee, orderDto.deliveryFee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, creationDate, deliveryDate, status, paymentMethod, prize, deliveryFee);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                ", deliveryDate=" + deliveryDate +
                ", status=" + status +
                ", paymentMethod=" + paymentMethod +
                ", prize=" + prize +
                ", deliveryFee=" + deliveryFee +
                '}';
    }
}
