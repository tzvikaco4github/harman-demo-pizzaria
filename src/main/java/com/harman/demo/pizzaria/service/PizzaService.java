package com.harman.demo.pizzaria.service;

import com.harman.demo.pizzaria.domain.Pizza;
import com.harman.demo.pizzaria.repository.PizzaRepository;
import com.harman.demo.pizzaria.service.dto.PizzaDTO;
import com.harman.demo.pizzaria.service.mapper.PizzaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pizza}.
 */
@Service
@Transactional
public class PizzaService {

    private final Logger log = LoggerFactory.getLogger(PizzaService.class);

    private final PizzaRepository pizzaRepository;

    private final PizzaMapper pizzaMapper;

    public PizzaService(PizzaRepository pizzaRepository, PizzaMapper pizzaMapper) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaMapper = pizzaMapper;
    }

    /**
     * Save a pizza.
     *
     * @param pizzaDTO the entity to save.
     * @return the persisted entity.
     */
    public PizzaDTO save(PizzaDTO pizzaDTO) {
        log.debug("Request to save Pizza : {}", pizzaDTO);
        Pizza pizza = pizzaMapper.toEntity(pizzaDTO);
        pizza = pizzaRepository.save(pizza);
        return pizzaMapper.toDto(pizza);
    }

    /**
     * Update a pizza.
     *
     * @param pizzaDTO the entity to save.
     * @return the persisted entity.
     */
    public PizzaDTO update(PizzaDTO pizzaDTO) {
        log.debug("Request to save Pizza : {}", pizzaDTO);
        Pizza pizza = pizzaMapper.toEntity(pizzaDTO);
        pizza = pizzaRepository.save(pizza);
        return pizzaMapper.toDto(pizza);
    }

    /**
     * Partially update a pizza.
     *
     * @param pizzaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PizzaDTO> partialUpdate(PizzaDTO pizzaDTO) {
        log.debug("Request to partially update Pizza : {}", pizzaDTO);

        return pizzaRepository
            .findById(pizzaDTO.getId())
            .map(existingPizza -> {
                pizzaMapper.partialUpdate(existingPizza, pizzaDTO);

                return existingPizza;
            })
            .map(pizzaRepository::save)
            .map(pizzaMapper::toDto);
    }

    /**
     * Get all the pizzas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PizzaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pizzas");
        return pizzaRepository.findAll(pageable).map(pizzaMapper::toDto);
    }

    /**
     * Get one pizza by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PizzaDTO> findOne(Long id) {
        log.debug("Request to get Pizza : {}", id);
        return pizzaRepository.findById(id).map(pizzaMapper::toDto);
    }

    /**
     * Delete the pizza by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pizza : {}", id);
        pizzaRepository.deleteById(id);
    }
}
