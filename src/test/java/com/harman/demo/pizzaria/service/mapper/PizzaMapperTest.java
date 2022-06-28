package com.harman.demo.pizzaria.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PizzaMapperTest {

    private PizzaMapper pizzaMapper;

    @BeforeEach
    public void setUp() {
        pizzaMapper = new PizzaMapperImpl();
    }
}
