package gmarmari.demo.microservices.orders.entities;

import gmarmari.demo.microservices.orders.entities.converters.PrizeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "products")
public class ProductDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    private int amount;

    @NotNull
    @Convert(converter = PrizeConverter.class)
    @Column(name="prize")
    private PrizeDao prize;

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public PrizeDao getPrize() {
        return prize;
    }

    public void setPrize(PrizeDao prize) {
        this.prize = prize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDao that = (ProductDao) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProductDao{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", prize=" + prize +
                '}';
    }
}
