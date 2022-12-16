package gmarmari.demo.microservices.orders.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Schema(name = "OrderAddress_V01")
public class OrderAddressDto {

    public final long id;

    public final long orderId;

    @NotNull
    public final OrderAddressTypeDto type;

    @NotBlank
    @Size(max = 100)
    public final String name;

    @NotBlank
    @Size(max = 100)
    public final String street;

    @NotBlank
    @Size(max = 100)
    public final String postalCode;

    @NotBlank
    @Size(max = 100)
    public final String city;

    @Nullable
    @Size(max = 100)
    public final String state;

    @NotBlank
    @Size(max = 100)
    public final String country;

    @Nullable
    @Size(max = 100)
    public final String tel;

    @Nullable
    @Size(max = 100)
    public final String email;

    @Nullable
    @Size(max = 100)
    public final String website;

    public OrderAddressDto(@JsonProperty("id") long id,
                           @JsonProperty("orderId") long orderId,
                           @JsonProperty("type") OrderAddressTypeDto type,
                           @JsonProperty("name") String name,
                           @JsonProperty("street") String street,
                           @JsonProperty("postalCode") String postalCode,
                           @JsonProperty("city") String city,
                           @Nullable @JsonProperty("state") String state,
                           @JsonProperty("country") String country,
                           @Nullable @JsonProperty("tel") String tel,
                           @Nullable @JsonProperty("email") String email,
                           @Nullable @JsonProperty("website") String website) {
        this.id = id;
        this.orderId = orderId;
        this.type = type;
        this.name = name;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.tel = tel;
        this.email = email;
        this.website = website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderAddressDto that = (OrderAddressDto) o;
        return id == that.id && orderId == that.orderId && type == that.type && Objects.equals(name, that.name) && Objects.equals(street, that.street) && Objects.equals(postalCode, that.postalCode) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(country, that.country) && Objects.equals(tel, that.tel) && Objects.equals(email, that.email) && Objects.equals(website, that.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, type, name, street, postalCode, city, state, country, tel, email, website);
    }

    @Override
    public String toString() {
        return "OrderAddressDto{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
