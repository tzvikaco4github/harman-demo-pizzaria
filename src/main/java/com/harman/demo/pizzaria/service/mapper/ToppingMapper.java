package com.harman.demo.pizzaria.service.mapper;

import com.harman.demo.pizzaria.domain.Topping;
import com.harman.demo.pizzaria.service.dto.ToppingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Topping} and its DTO {@link ToppingDTO}.
 */
@Mapper(componentModel = "spring")
public interface ToppingMapper extends EntityMapper<ToppingDTO, Topping> {}
