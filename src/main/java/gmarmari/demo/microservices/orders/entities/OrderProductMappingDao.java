package gmarmari.demo.microservices.orders.entities;

import io.swagger.v3.oas.annotations.info.License;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "order_product_mapping")
public class OrderProductMappingDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long orderId;

    private long productId;

    private int amount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProductMappingDao that = (OrderProductMappingDao) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderProductMappingDao{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", amount=" + amount +
                '}';
    }
}
