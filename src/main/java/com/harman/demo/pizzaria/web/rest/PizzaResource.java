package com.harman.demo.pizzaria.web.rest;

import com.harman.demo.pizzaria.repository.PizzaRepository;
import com.harman.demo.pizzaria.service.PizzaService;
import com.harman.demo.pizzaria.service.dto.PizzaDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.harman.demo.pizzaria.domain.Pizza}.
 */
@RestController
@RequestMapping("/api")
public class PizzaResource {

    private final Logger log = LoggerFactory.getLogger(PizzaResource.class);

    private static final String ENTITY_NAME = "pizza";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PizzaService pizzaService;

    private final PizzaRepository pizzaRepository;

    public PizzaResource(PizzaService pizzaService, PizzaRepository pizzaRepository) {
        this.pizzaService = pizzaService;
        this.pizzaRepository = pizzaRepository;
    }

    /**
     * {@code POST  /pizzas} : Create a new pizza.
     *
     * @param pizzaDTO the pizzaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pizzaDTO, or with status {@code 400 (Bad Request)} if the pizza has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pizzas")
    public ResponseEntity<PizzaDTO> createPizza(@Valid @RequestBody PizzaDTO pizzaDTO) throws URISyntaxException {
        log.debug("REST request to save Pizza : {}", pizzaDTO);
        if (pizzaDTO.getId() != null) {
            throw new BadRequestAlertException("A new pizza cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PizzaDTO result = pizzaService.save(pizzaDTO);
        return ResponseEntity
            .created(new URI("/api/pizzas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pizzas/:id} : Updates an existing pizza.
     *
     * @param id the id of the pizzaDTO to save.
     * @param pizzaDTO the pizzaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pizzaDTO,
     * or with status {@code 400 (Bad Request)} if the pizzaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pizzaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pizzas/{id}")
    public ResponseEntity<PizzaDTO> updatePizza(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PizzaDTO pizzaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pizza : {}, {}", id, pizzaDTO);
        if (pizzaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pizzaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pizzaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PizzaDTO result = pizzaService.update(pizzaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pizzaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pizzas/:id} : Partial updates given fields of an existing pizza, field will ignore if it is null
     *
     * @param id the id of the pizzaDTO to save.
     * @param pizzaDTO the pizzaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pizzaDTO,
     * or with status {@code 400 (Bad Request)} if the pizzaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pizzaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pizzaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pizzas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PizzaDTO> partialUpdatePizza(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PizzaDTO pizzaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pizza partially : {}, {}", id, pizzaDTO);
        if (pizzaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pizzaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pizzaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PizzaDTO> result = pizzaService.partialUpdate(pizzaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pizzaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pizzas} : get all the pizzas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pizzas in body.
     */
    @GetMapping("/pizzas")
    public ResponseEntity<List<PizzaDTO>> getAllPizzas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pizzas");
        Page<PizzaDTO> page = pizzaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pizzas/:id} : get the "id" pizza.
     *
     * @param id the id of the pizzaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pizzaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pizzas/{id}")
    public ResponseEntity<PizzaDTO> getPizza(@PathVariable Long id) {
        log.debug("REST request to get Pizza : {}", id);
        Optional<PizzaDTO> pizzaDTO = pizzaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pizzaDTO);
    }

    /**
     * {@code DELETE  /pizzas/:id} : delete the "id" pizza.
     *
     * @param id the id of the pizzaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pizzas/{id}")
    public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
        log.debug("REST request to delete Pizza : {}", id);
        pizzaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
