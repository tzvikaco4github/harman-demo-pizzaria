package com.harman.demo.pizzaria.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.harman.demo.pizzaria.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PizzaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pizza.class);
        Pizza pizza1 = new Pizza();
        pizza1.setId(1L);
        Pizza pizza2 = new Pizza();
        pizza2.setId(pizza1.getId());
        assertThat(pizza1).isEqualTo(pizza2);
        pizza2.setId(2L);
        assertThat(pizza1).isNotEqualTo(pizza2);
        pizza1.setId(null);
        assertThat(pizza1).isNotEqualTo(pizza2);
    }
}
