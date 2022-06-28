package com.harman.demo.pizzaria.service;

import com.harman.demo.pizzaria.domain.*; // for static metamodels
import com.harman.demo.pizzaria.domain.Topping;
import com.harman.demo.pizzaria.repository.ToppingRepository;
import com.harman.demo.pizzaria.service.criteria.ToppingCriteria;
import com.harman.demo.pizzaria.service.dto.ToppingDTO;
import com.harman.demo.pizzaria.service.mapper.ToppingMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Topping} entities in the database.
 * The main input is a {@link ToppingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ToppingDTO} or a {@link Page} of {@link ToppingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ToppingQueryService extends QueryService<Topping> {

    private final Logger log = LoggerFactory.getLogger(ToppingQueryService.class);

    private final ToppingRepository toppingRepository;

    private final ToppingMapper toppingMapper;

    public ToppingQueryService(ToppingRepository toppingRepository, ToppingMapper toppingMapper) {
        this.toppingRepository = toppingRepository;
        this.toppingMapper = toppingMapper;
    }

    /**
     * Return a {@link List} of {@link ToppingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ToppingDTO> findByCriteria(ToppingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Topping> specification = createSpecification(criteria);
        return toppingMapper.toDto(toppingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ToppingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ToppingDTO> findByCriteria(ToppingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Topping> specification = createSpecification(criteria);
        return toppingRepository.findAll(specification, page).map(toppingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ToppingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Topping> specification = createSpecification(criteria);
        return toppingRepository.count(specification);
    }

    /**
     * Function to convert {@link ToppingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Topping> createSpecification(ToppingCriteria criteria) {
        Specification<Topping> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Topping_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Topping_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Topping_.price));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Topping_.description));
            }
        }
        return specification;
    }
}
