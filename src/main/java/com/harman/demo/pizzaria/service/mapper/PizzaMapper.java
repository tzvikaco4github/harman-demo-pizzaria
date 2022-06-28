package com.harman.demo.pizzaria.service.mapper;

import com.harman.demo.pizzaria.domain.Pizza;
import com.harman.demo.pizzaria.service.dto.PizzaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pizza} and its DTO {@link PizzaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PizzaMapper extends EntityMapper<PizzaDTO, Pizza> {}
