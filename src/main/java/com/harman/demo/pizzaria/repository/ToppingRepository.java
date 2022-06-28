package com.harman.demo.pizzaria.repository;

import com.harman.demo.pizzaria.domain.Topping;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Topping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToppingRepository extends JpaRepository<Topping, Long>, JpaSpecificationExecutor<Topping> {}
