package com.harman.demo.pizzaria.service.dto;

import com.harman.demo.pizzaria.domain.enumeration.PizzaSize;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.harman.demo.pizzaria.domain.Pizza} entity.
 */
public class PizzaDTO implements Serializable {

    private Long id;

    @NotNull
    private PizzaSize pizzaSize;

    @NotNull
    @DecimalMin(value = "1")
    private Float price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PizzaSize getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(PizzaSize pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PizzaDTO)) {
            return false;
        }

        PizzaDTO pizzaDTO = (PizzaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pizzaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PizzaDTO{" +
            "id=" + getId() +
            ", pizzaSize='" + getPizzaSize() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
