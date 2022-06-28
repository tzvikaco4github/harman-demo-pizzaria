package com.harman.demo.pizzaria.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.harman.demo.pizzaria.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PizzaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PizzaDTO.class);
        PizzaDTO pizzaDTO1 = new PizzaDTO();
        pizzaDTO1.setId(1L);
        PizzaDTO pizzaDTO2 = new PizzaDTO();
        assertThat(pizzaDTO1).isNotEqualTo(pizzaDTO2);
        pizzaDTO2.setId(pizzaDTO1.getId());
        assertThat(pizzaDTO1).isEqualTo(pizzaDTO2);
        pizzaDTO2.setId(2L);
        assertThat(pizzaDTO1).isNotEqualTo(pizzaDTO2);
        pizzaDTO1.setId(null);
        assertThat(pizzaDTO1).isNotEqualTo(pizzaDTO2);
    }
}
