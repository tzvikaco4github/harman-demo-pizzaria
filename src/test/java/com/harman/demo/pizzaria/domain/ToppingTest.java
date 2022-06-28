package com.harman.demo.pizzaria.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.harman.demo.pizzaria.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ToppingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Topping.class);
        Topping topping1 = new Topping();
        topping1.setId(1L);
        Topping topping2 = new Topping();
        topping2.setId(topping1.getId());
        assertThat(topping1).isEqualTo(topping2);
        topping2.setId(2L);
        assertThat(topping1).isNotEqualTo(topping2);
        topping1.setId(null);
        assertThat(topping1).isNotEqualTo(topping2);
    }
}
