package com.harman.demo.pizzaria.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.harman.demo.pizzaria.domain.Topping} entity.
 */
public class ToppingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2)
    private String name;

    @NotNull
    private Float price;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToppingDTO)) {
            return false;
        }

        ToppingDTO toppingDTO = (ToppingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, toppingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToppingDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
