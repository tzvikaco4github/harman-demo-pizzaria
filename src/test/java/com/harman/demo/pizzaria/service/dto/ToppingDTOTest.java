package com.harman.demo.pizzaria.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.harman.demo.pizzaria.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ToppingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToppingDTO.class);
        ToppingDTO toppingDTO1 = new ToppingDTO();
        toppingDTO1.setId(1L);
        ToppingDTO toppingDTO2 = new ToppingDTO();
        assertThat(toppingDTO1).isNotEqualTo(toppingDTO2);
        toppingDTO2.setId(toppingDTO1.getId());
        assertThat(toppingDTO1).isEqualTo(toppingDTO2);
        toppingDTO2.setId(2L);
        assertThat(toppingDTO1).isNotEqualTo(toppingDTO2);
        toppingDTO1.setId(null);
        assertThat(toppingDTO1).isNotEqualTo(toppingDTO2);
    }
}
