package com.harman.demo.pizzaria.repository;

import com.harman.demo.pizzaria.domain.Pizza;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pizza entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {}
