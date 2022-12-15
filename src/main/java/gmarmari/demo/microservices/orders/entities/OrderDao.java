package gmarmari.demo.microservices.orders.entities;

import gmarmari.demo.microservices.orders.entities.converters.PrizeConverter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class OrderDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String username;

    @NotNull
    private ZonedDateTime creationDate;

    @Nullable
    private ZonedDateTime deliveryDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatusDao status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethodDao paymentMethod;

    @NotNull
    @Convert(converter = PrizeConverter.class)
    @Column(name="prize")
    private PrizeDao prize;

    @NotNull
    @Convert(converter = PrizeConverter.class)
    @Column(name="delivery_fee")
    private PrizeDao deliveryFee;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Nullable
    public ZonedDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(@Nullable ZonedDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public OrderStatusDao getStatus() {
        return status;
    }

    public void setStatus(OrderStatusDao status) {
        this.status = status;
    }

    public PaymentMethodDao getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodDao paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PrizeDao getPrize() {
        return prize;
    }

    public void setPrize(PrizeDao prize) {
        this.prize = prize;
    }

    public PrizeDao getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(PrizeDao deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDao orderDao = (OrderDao) o;
        return id == orderDao.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderDao{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", creationDate=" + creationDate +
                ", deliveryDate=" + deliveryDate +
                ", status=" + status +
                ", paymentMethod=" + paymentMethod +
                ", prize=" + prize +
                ", deliveryFee=" + deliveryFee +
                '}';
    }
}
