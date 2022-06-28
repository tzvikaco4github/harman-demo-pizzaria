package com.harman.demo.pizzaria.web.rest;

import com.harman.demo.pizzaria.repository.ToppingRepository;
import com.harman.demo.pizzaria.service.ToppingQueryService;
import com.harman.demo.pizzaria.service.ToppingService;
import com.harman.demo.pizzaria.service.criteria.ToppingCriteria;
import com.harman.demo.pizzaria.service.dto.ToppingDTO;
import com.harman.demo.pizzaria.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.harman.demo.pizzaria.domain.Topping}.
 */
@RestController
@RequestMapping("/api")
public class ToppingResource {

    private final Logger log = LoggerFactory.getLogger(ToppingResource.class);

    private static final String ENTITY_NAME = "topping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToppingService toppingService;

    private final ToppingRepository toppingRepository;

    private final ToppingQueryService toppingQueryService;

    public ToppingResource(ToppingService toppingService, ToppingRepository toppingRepository, ToppingQueryService toppingQueryService) {
        this.toppingService = toppingService;
        this.toppingRepository = toppingRepository;
        this.toppingQueryService = toppingQueryService;
    }

    /**
     * {@code POST  /toppings} : Create a new topping.
     *
     * @param toppingDTO the toppingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toppingDTO, or with status {@code 400 (Bad Request)} if the topping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/toppings")
    public ResponseEntity<ToppingDTO> createTopping(@Valid @RequestBody ToppingDTO toppingDTO) throws URISyntaxException {
        log.debug("REST request to save Topping : {}", toppingDTO);
        if (toppingDTO.getId() != null) {
            throw new BadRequestAlertException("A new topping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToppingDTO result = toppingService.save(toppingDTO);
        return ResponseEntity
            .created(new URI("/api/toppings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /toppings/:id} : Updates an existing topping.
     *
     * @param id the id of the toppingDTO to save.
     * @param toppingDTO the toppingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toppingDTO,
     * or with status {@code 400 (Bad Request)} if the toppingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toppingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/toppings/{id}")
    public ResponseEntity<ToppingDTO> updateTopping(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ToppingDTO toppingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Topping : {}, {}", id, toppingDTO);
        if (toppingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toppingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toppingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ToppingDTO result = toppingService.update(toppingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, toppingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /toppings/:id} : Partial updates given fields of an existing topping, field will ignore if it is null
     *
     * @param id the id of the toppingDTO to save.
     * @param toppingDTO the toppingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toppingDTO,
     * or with status {@code 400 (Bad Request)} if the toppingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the toppingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the toppingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/toppings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ToppingDTO> partialUpdateTopping(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ToppingDTO toppingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Topping partially : {}, {}", id, toppingDTO);
        if (toppingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toppingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toppingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ToppingDTO> result = toppingService.partialUpdate(toppingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, toppingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /toppings} : get all the toppings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toppings in body.
     */
    @GetMapping("/toppings")
    public ResponseEntity<List<ToppingDTO>> getAllToppings(
        ToppingCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Toppings by criteria: {}", criteria);
        Page<ToppingDTO> page = toppingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /toppings/count} : count all the toppings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/toppings/count")
    public ResponseEntity<Long> countToppings(ToppingCriteria criteria) {
        log.debug("REST request to count Toppings by criteria: {}", criteria);
        return ResponseEntity.ok().body(toppingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /toppings/:id} : get the "id" topping.
     *
     * @param id the id of the toppingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toppingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/toppings/{id}")
    public ResponseEntity<ToppingDTO> getTopping(@PathVariable Long id) {
        log.debug("REST request to get Topping : {}", id);
        Optional<ToppingDTO> toppingDTO = toppingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toppingDTO);
    }

    /**
     * {@code DELETE  /toppings/:id} : delete the "id" topping.
     *
     * @param id the id of the toppingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/toppings/{id}")
    public ResponseEntity<Void> deleteTopping(@PathVariable Long id) {
        log.debug("REST request to delete Topping : {}", id);
        toppingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
