package com.harman.demo.pizzaria.domain;

import com.harman.demo.pizzaria.domain.enumeration.PizzaSize;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pizza.
 */
@Entity
@Table(name = "pizza")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pizza implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "pizza_size", nullable = false)
    private PizzaSize pizzaSize;

    @NotNull
    @DecimalMin(value = "1")
    @Column(name = "price", nullable = false)
    private Float price;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pizza id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PizzaSize getPizzaSize() {
        return this.pizzaSize;
    }

    public Pizza pizzaSize(PizzaSize pizzaSize) {
        this.setPizzaSize(pizzaSize);
        return this;
    }

    public void setPizzaSize(PizzaSize pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public Float getPrice() {
        return this.price;
    }

    public Pizza price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pizza)) {
            return false;
        }
        return id != null && id.equals(((Pizza) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pizza{" +
            "id=" + getId() +
            ", pizzaSize='" + getPizzaSize() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
