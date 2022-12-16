package gmarmari.demo.microservices.orders.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "order_product_mapping")
public class OrderProductMappingDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderDao order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    protected ProductDao product;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderDao getOrder() {
        return order;
    }

    public void setOrder(OrderDao order) {
        this.order = order;
    }

    public ProductDao getProduct() {
        return product;
    }

    public void setProduct(ProductDao product) {
        this.product = product;
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
                ", order=" + order +
                ", product=" + product +
                '}';
    }
}
