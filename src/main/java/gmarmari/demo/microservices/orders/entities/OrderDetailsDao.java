package gmarmari.demo.microservices.orders.entities;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class OrderDetailsDao {

    @NotNull
    public final OrderDao order;

    @NotNull
    public final List<OrderAddressDao> addresses;

    public OrderDetailsDao(@NotNull OrderDao order,
                           @NotNull List<OrderAddressDao> addresses) {
        this.order = order;
        this.addresses = addresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetailsDao that = (OrderDetailsDao) o;
        return Objects.equals(order, that.order) && Objects.equals(addresses, that.addresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, addresses);
    }

    @Override
    public String toString() {
        return "OrderDetailsDao{" +
                "order=" + order +
                ", addresses=" + addresses +
                '}';
    }
}
