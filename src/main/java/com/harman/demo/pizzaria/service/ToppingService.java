package com.harman.demo.pizzaria.service;

import com.harman.demo.pizzaria.domain.Topping;
import com.harman.demo.pizzaria.repository.ToppingRepository;
import com.harman.demo.pizzaria.service.dto.ToppingDTO;
import com.harman.demo.pizzaria.service.mapper.ToppingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Topping}.
 */
@Service
@Transactional
public class ToppingService {

    private final Logger log = LoggerFactory.getLogger(ToppingService.class);

    private final ToppingRepository toppingRepository;

    private final ToppingMapper toppingMapper;

    public ToppingService(ToppingRepository toppingRepository, ToppingMapper toppingMapper) {
        this.toppingRepository = toppingRepository;
        this.toppingMapper = toppingMapper;
    }

    /**
     * Save a topping.
     *
     * @param toppingDTO the entity to save.
     * @return the persisted entity.
     */
    public ToppingDTO save(ToppingDTO toppingDTO) {
        log.debug("Request to save Topping : {}", toppingDTO);
        Topping topping = toppingMapper.toEntity(toppingDTO);
        topping = toppingRepository.save(topping);
        return toppingMapper.toDto(topping);
    }

    /**
     * Update a topping.
     *
     * @param toppingDTO the entity to save.
     * @return the persisted entity.
     */
    public ToppingDTO update(ToppingDTO toppingDTO) {
        log.debug("Request to save Topping : {}", toppingDTO);
        Topping topping = toppingMapper.toEntity(toppingDTO);
        topping = toppingRepository.save(topping);
        return toppingMapper.toDto(topping);
    }

    /**
     * Partially update a topping.
     *
     * @param toppingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ToppingDTO> partialUpdate(ToppingDTO toppingDTO) {
        log.debug("Request to partially update Topping : {}", toppingDTO);

        return toppingRepository
            .findById(toppingDTO.getId())
            .map(existingTopping -> {
                toppingMapper.partialUpdate(existingTopping, toppingDTO);

                return existingTopping;
            })
            .map(toppingRepository::save)
            .map(toppingMapper::toDto);
    }

    /**
     * Get all the toppings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ToppingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Toppings");
        return toppingRepository.findAll(pageable).map(toppingMapper::toDto);
    }

    /**
     * Get one topping by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ToppingDTO> findOne(Long id) {
        log.debug("Request to get Topping : {}", id);
        return toppingRepository.findById(id).map(toppingMapper::toDto);
    }

    /**
     * Delete the topping by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Topping : {}", id);
        toppingRepository.deleteById(id);
    }
}
