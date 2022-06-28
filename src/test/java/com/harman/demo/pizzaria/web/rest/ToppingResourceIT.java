package com.harman.demo.pizzaria.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.harman.demo.pizzaria.IntegrationTest;
import com.harman.demo.pizzaria.domain.Topping;
import com.harman.demo.pizzaria.repository.ToppingRepository;
import com.harman.demo.pizzaria.service.criteria.ToppingCriteria;
import com.harman.demo.pizzaria.service.dto.ToppingDTO;
import com.harman.demo.pizzaria.service.mapper.ToppingMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ToppingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ToppingResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/toppings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ToppingRepository toppingRepository;

    @Autowired
    private ToppingMapper toppingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restToppingMockMvc;

    private Topping topping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topping createEntity(EntityManager em) {
        Topping topping = new Topping().name(DEFAULT_NAME).price(DEFAULT_PRICE).description(DEFAULT_DESCRIPTION);
        return topping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topping createUpdatedEntity(EntityManager em) {
        Topping topping = new Topping().name(UPDATED_NAME).price(UPDATED_PRICE).description(UPDATED_DESCRIPTION);
        return topping;
    }

    @BeforeEach
    public void initTest() {
        topping = createEntity(em);
    }

    @Test
    @Transactional
    void createTopping() throws Exception {
        int databaseSizeBeforeCreate = toppingRepository.findAll().size();
        // Create the Topping
        ToppingDTO toppingDTO = toppingMapper.toDto(topping);
        restToppingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(toppingDTO)))
            .andExpect(status().isCreated());

        // Validate the Topping in the database
        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeCreate + 1);
        Topping testTopping = toppingList.get(toppingList.size() - 1);
        assertThat(testTopping.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTopping.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testTopping.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createToppingWithExistingId() throws Exception {
        // Create the Topping with an existing ID
        topping.setId(1L);
        ToppingDTO toppingDTO = toppingMapper.toDto(topping);

        int databaseSizeBeforeCreate = toppingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restToppingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(toppingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Topping in the database
        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = toppingRepository.findAll().size();
        // set the field null
        topping.setName(null);

        // Create the Topping, which fails.
        ToppingDTO toppingDTO = toppingMapper.toDto(topping);

        restToppingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(toppingDTO)))
            .andExpect(status().isBadRequest());

        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = toppingRepository.findAll().size();
        // set the field null
        topping.setPrice(null);

        // Create the Topping, which fails.
        ToppingDTO toppingDTO = toppingMapper.toDto(topping);

        restToppingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(toppingDTO)))
            .andExpect(status().isBadRequest());

        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllToppings() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList
        restToppingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topping.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTopping() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get the topping
        restToppingMockMvc
            .perform(get(ENTITY_API_URL_ID, topping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topping.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getToppingsByIdFiltering() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        Long id = topping.getId();

        defaultToppingShouldBeFound("id.equals=" + id);
        defaultToppingShouldNotBeFound("id.notEquals=" + id);

        defaultToppingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultToppingShouldNotBeFound("id.greaterThan=" + id);

        defaultToppingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultToppingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllToppingsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where name equals to DEFAULT_NAME
        defaultToppingShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the toppingList where name equals to UPDATED_NAME
        defaultToppingShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToppingsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where name not equals to DEFAULT_NAME
        defaultToppingShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the toppingList where name not equals to UPDATED_NAME
        defaultToppingShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToppingsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where name in DEFAULT_NAME or UPDATED_NAME
        defaultToppingShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the toppingList where name equals to UPDATED_NAME
        defaultToppingShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToppingsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where name is not null
        defaultToppingShouldBeFound("name.specified=true");

        // Get all the toppingList where name is null
        defaultToppingShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllToppingsByNameContainsSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where name contains DEFAULT_NAME
        defaultToppingShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the toppingList where name contains UPDATED_NAME
        defaultToppingShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToppingsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where name does not contain DEFAULT_NAME
        defaultToppingShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the toppingList where name does not contain UPDATED_NAME
        defaultToppingShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllToppingsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where price equals to DEFAULT_PRICE
        defaultToppingShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the toppingList where price equals to UPDATED_PRICE
        defaultToppingShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllToppingsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where price not equals to DEFAULT_PRICE
        defaultToppingShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the toppingList where price not equals to UPDATED_PRICE
        defaultToppingShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllToppingsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultToppingShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the toppingList where price equals to UPDATED_PRICE
        defaultToppingShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllToppingsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where price is not null
        defaultToppingShouldBeFound("price.specified=true");

        // Get all the toppingList where price is null
        defaultToppingShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllToppingsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where price is greater than or equal to DEFAULT_PRICE
        defaultToppingShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the toppingList where price is greater than or equal to UPDATED_PRICE
        defaultToppingShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllToppingsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where price is less than or equal to DEFAULT_PRICE
        defaultToppingShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the toppingList where price is less than or equal to SMALLER_PRICE
        defaultToppingShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllToppingsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where price is less than DEFAULT_PRICE
        defaultToppingShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the toppingList where price is less than UPDATED_PRICE
        defaultToppingShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllToppingsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where price is greater than DEFAULT_PRICE
        defaultToppingShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the toppingList where price is greater than SMALLER_PRICE
        defaultToppingShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllToppingsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where description equals to DEFAULT_DESCRIPTION
        defaultToppingShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the toppingList where description equals to UPDATED_DESCRIPTION
        defaultToppingShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToppingsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where description not equals to DEFAULT_DESCRIPTION
        defaultToppingShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the toppingList where description not equals to UPDATED_DESCRIPTION
        defaultToppingShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToppingsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultToppingShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the toppingList where description equals to UPDATED_DESCRIPTION
        defaultToppingShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToppingsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where description is not null
        defaultToppingShouldBeFound("description.specified=true");

        // Get all the toppingList where description is null
        defaultToppingShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllToppingsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where description contains DEFAULT_DESCRIPTION
        defaultToppingShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the toppingList where description contains UPDATED_DESCRIPTION
        defaultToppingShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllToppingsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        // Get all the toppingList where description does not contain DEFAULT_DESCRIPTION
        defaultToppingShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the toppingList where description does not contain UPDATED_DESCRIPTION
        defaultToppingShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultToppingShouldBeFound(String filter) throws Exception {
        restToppingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topping.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restToppingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultToppingShouldNotBeFound(String filter) throws Exception {
        restToppingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restToppingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTopping() throws Exception {
        // Get the topping
        restToppingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTopping() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        int databaseSizeBeforeUpdate = toppingRepository.findAll().size();

        // Update the topping
        Topping updatedTopping = toppingRepository.findById(topping.getId()).get();
        // Disconnect from session so that the updates on updatedTopping are not directly saved in db
        em.detach(updatedTopping);
        updatedTopping.name(UPDATED_NAME).price(UPDATED_PRICE).description(UPDATED_DESCRIPTION);
        ToppingDTO toppingDTO = toppingMapper.toDto(updatedTopping);

        restToppingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, toppingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toppingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Topping in the database
        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeUpdate);
        Topping testTopping = toppingList.get(toppingList.size() - 1);
        assertThat(testTopping.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTopping.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTopping.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingTopping() throws Exception {
        int databaseSizeBeforeUpdate = toppingRepository.findAll().size();
        topping.setId(count.incrementAndGet());

        // Create the Topping
        ToppingDTO toppingDTO = toppingMapper.toDto(topping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToppingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, toppingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toppingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topping in the database
        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTopping() throws Exception {
        int databaseSizeBeforeUpdate = toppingRepository.findAll().size();
        topping.setId(count.incrementAndGet());

        // Create the Topping
        ToppingDTO toppingDTO = toppingMapper.toDto(topping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToppingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(toppingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topping in the database
        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTopping() throws Exception {
        int databaseSizeBeforeUpdate = toppingRepository.findAll().size();
        topping.setId(count.incrementAndGet());

        // Create the Topping
        ToppingDTO toppingDTO = toppingMapper.toDto(topping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToppingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(toppingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Topping in the database
        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateToppingWithPatch() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        int databaseSizeBeforeUpdate = toppingRepository.findAll().size();

        // Update the topping using partial update
        Topping partialUpdatedTopping = new Topping();
        partialUpdatedTopping.setId(topping.getId());

        partialUpdatedTopping.description(UPDATED_DESCRIPTION);

        restToppingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopping))
            )
            .andExpect(status().isOk());

        // Validate the Topping in the database
        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeUpdate);
        Topping testTopping = toppingList.get(toppingList.size() - 1);
        assertThat(testTopping.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTopping.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testTopping.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateToppingWithPatch() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        int databaseSizeBeforeUpdate = toppingRepository.findAll().size();

        // Update the topping using partial update
        Topping partialUpdatedTopping = new Topping();
        partialUpdatedTopping.setId(topping.getId());

        partialUpdatedTopping.name(UPDATED_NAME).price(UPDATED_PRICE).description(UPDATED_DESCRIPTION);

        restToppingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopping))
            )
            .andExpect(status().isOk());

        // Validate the Topping in the database
        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeUpdate);
        Topping testTopping = toppingList.get(toppingList.size() - 1);
        assertThat(testTopping.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTopping.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTopping.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingTopping() throws Exception {
        int databaseSizeBeforeUpdate = toppingRepository.findAll().size();
        topping.setId(count.incrementAndGet());

        // Create the Topping
        ToppingDTO toppingDTO = toppingMapper.toDto(topping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToppingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, toppingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toppingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topping in the database
        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTopping() throws Exception {
        int databaseSizeBeforeUpdate = toppingRepository.findAll().size();
        topping.setId(count.incrementAndGet());

        // Create the Topping
        ToppingDTO toppingDTO = toppingMapper.toDto(topping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToppingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(toppingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topping in the database
        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTopping() throws Exception {
        int databaseSizeBeforeUpdate = toppingRepository.findAll().size();
        topping.setId(count.incrementAndGet());

        // Create the Topping
        ToppingDTO toppingDTO = toppingMapper.toDto(topping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restToppingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(toppingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Topping in the database
        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTopping() throws Exception {
        // Initialize the database
        toppingRepository.saveAndFlush(topping);

        int databaseSizeBeforeDelete = toppingRepository.findAll().size();

        // Delete the topping
        restToppingMockMvc
            .perform(delete(ENTITY_API_URL_ID, topping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Topping> toppingList = toppingRepository.findAll();
        assertThat(toppingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
