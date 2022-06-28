package com.harman.demo.pizzaria.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToppingMapperTest {

    private ToppingMapper toppingMapper;

    @BeforeEach
    public void setUp() {
        toppingMapper = new ToppingMapperImpl();
    }
}
