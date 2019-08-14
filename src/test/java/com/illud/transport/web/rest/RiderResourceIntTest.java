package com.illud.transport.web.rest;

import com.illud.transport.TransportApp;

import com.illud.transport.domain.Rider;
import com.illud.transport.repository.RiderRepository;
import com.illud.transport.repository.search.RiderSearchRepository;
import com.illud.transport.service.RiderService;
import com.illud.transport.service.dto.RiderDTO;
import com.illud.transport.service.mapper.RiderMapper;
import com.illud.transport.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.illud.transport.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RiderResource REST controller.
 *
 * @see RiderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransportApp.class)
public class RiderResourceIntTest {

    private static final String DEFAULT_I_D_PCODE = "AAAAAAAAAA";
    private static final String UPDATED_I_D_PCODE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILENUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILENUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private RiderMapper riderMapper;

    @Autowired
    private RiderService riderService;

    /**
     * This repository is mocked in the com.illud.transport.repository.search test package.
     *
     * @see com.illud.transport.repository.search.RiderSearchRepositoryMockConfiguration
     */
    @Autowired
    private RiderSearchRepository mockRiderSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restRiderMockMvc;

    private Rider rider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RiderResource riderResource = new RiderResource(riderService);
        this.restRiderMockMvc = MockMvcBuilders.standaloneSetup(riderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rider createEntity(EntityManager em) {
        Rider rider = new Rider()
            .iDPcode(DEFAULT_I_D_PCODE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .mobilenumber(DEFAULT_MOBILENUMBER)
            .email(DEFAULT_EMAIL);
        return rider;
    }

    @Before
    public void initTest() {
        rider = createEntity(em);
    }

    @Test
    @Transactional
    public void createRider() throws Exception {
        int databaseSizeBeforeCreate = riderRepository.findAll().size();

        // Create the Rider
        RiderDTO riderDTO = riderMapper.toDto(rider);
        restRiderMockMvc.perform(post("/api/riders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(riderDTO)))
            .andExpect(status().isCreated());

        // Validate the Rider in the database
        List<Rider> riderList = riderRepository.findAll();
        assertThat(riderList).hasSize(databaseSizeBeforeCreate + 1);
        Rider testRider = riderList.get(riderList.size() - 1);
        assertThat(testRider.getiDPcode()).isEqualTo(DEFAULT_I_D_PCODE);
        assertThat(testRider.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testRider.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testRider.getMobilenumber()).isEqualTo(DEFAULT_MOBILENUMBER);
        assertThat(testRider.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the Rider in Elasticsearch
        verify(mockRiderSearchRepository, times(1)).save(testRider);
    }

    @Test
    @Transactional
    public void createRiderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = riderRepository.findAll().size();

        // Create the Rider with an existing ID
        rider.setId(1L);
        RiderDTO riderDTO = riderMapper.toDto(rider);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRiderMockMvc.perform(post("/api/riders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(riderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rider in the database
        List<Rider> riderList = riderRepository.findAll();
        assertThat(riderList).hasSize(databaseSizeBeforeCreate);

        // Validate the Rider in Elasticsearch
        verify(mockRiderSearchRepository, times(0)).save(rider);
    }

    @Test
    @Transactional
    public void getAllRiders() throws Exception {
        // Initialize the database
        riderRepository.saveAndFlush(rider);

        // Get all the riderList
        restRiderMockMvc.perform(get("/api/riders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rider.getId().intValue())))
            .andExpect(jsonPath("$.[*].iDPcode").value(hasItem(DEFAULT_I_D_PCODE.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].mobilenumber").value(hasItem(DEFAULT_MOBILENUMBER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
    
    @Test
    @Transactional
    public void getRider() throws Exception {
        // Initialize the database
        riderRepository.saveAndFlush(rider);

        // Get the rider
        restRiderMockMvc.perform(get("/api/riders/{id}", rider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rider.getId().intValue()))
            .andExpect(jsonPath("$.iDPcode").value(DEFAULT_I_D_PCODE.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.mobilenumber").value(DEFAULT_MOBILENUMBER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRider() throws Exception {
        // Get the rider
        restRiderMockMvc.perform(get("/api/riders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRider() throws Exception {
        // Initialize the database
        riderRepository.saveAndFlush(rider);

        int databaseSizeBeforeUpdate = riderRepository.findAll().size();

        // Update the rider
        Rider updatedRider = riderRepository.findById(rider.getId()).get();
        // Disconnect from session so that the updates on updatedRider are not directly saved in db
        em.detach(updatedRider);
        updatedRider
            .iDPcode(UPDATED_I_D_PCODE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .mobilenumber(UPDATED_MOBILENUMBER)
            .email(UPDATED_EMAIL);
        RiderDTO riderDTO = riderMapper.toDto(updatedRider);

        restRiderMockMvc.perform(put("/api/riders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(riderDTO)))
            .andExpect(status().isOk());

        // Validate the Rider in the database
        List<Rider> riderList = riderRepository.findAll();
        assertThat(riderList).hasSize(databaseSizeBeforeUpdate);
        Rider testRider = riderList.get(riderList.size() - 1);
        assertThat(testRider.getiDPcode()).isEqualTo(UPDATED_I_D_PCODE);
        assertThat(testRider.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testRider.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testRider.getMobilenumber()).isEqualTo(UPDATED_MOBILENUMBER);
        assertThat(testRider.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the Rider in Elasticsearch
        verify(mockRiderSearchRepository, times(1)).save(testRider);
    }

    @Test
    @Transactional
    public void updateNonExistingRider() throws Exception {
        int databaseSizeBeforeUpdate = riderRepository.findAll().size();

        // Create the Rider
        RiderDTO riderDTO = riderMapper.toDto(rider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRiderMockMvc.perform(put("/api/riders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(riderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rider in the database
        List<Rider> riderList = riderRepository.findAll();
        assertThat(riderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Rider in Elasticsearch
        verify(mockRiderSearchRepository, times(0)).save(rider);
    }

    @Test
    @Transactional
    public void deleteRider() throws Exception {
        // Initialize the database
        riderRepository.saveAndFlush(rider);

        int databaseSizeBeforeDelete = riderRepository.findAll().size();

        // Delete the rider
        restRiderMockMvc.perform(delete("/api/riders/{id}", rider.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rider> riderList = riderRepository.findAll();
        assertThat(riderList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Rider in Elasticsearch
        verify(mockRiderSearchRepository, times(1)).deleteById(rider.getId());
    }

    @Test
    @Transactional
    public void searchRider() throws Exception {
        // Initialize the database
        riderRepository.saveAndFlush(rider);
        when(mockRiderSearchRepository.search(queryStringQuery("id:" + rider.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rider), PageRequest.of(0, 1), 1));
        // Search the rider
        restRiderMockMvc.perform(get("/api/_search/riders?query=id:" + rider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rider.getId().intValue())))
            .andExpect(jsonPath("$.[*].iDPcode").value(hasItem(DEFAULT_I_D_PCODE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].mobilenumber").value(hasItem(DEFAULT_MOBILENUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rider.class);
        Rider rider1 = new Rider();
        rider1.setId(1L);
        Rider rider2 = new Rider();
        rider2.setId(rider1.getId());
        assertThat(rider1).isEqualTo(rider2);
        rider2.setId(2L);
        assertThat(rider1).isNotEqualTo(rider2);
        rider1.setId(null);
        assertThat(rider1).isNotEqualTo(rider2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RiderDTO.class);
        RiderDTO riderDTO1 = new RiderDTO();
        riderDTO1.setId(1L);
        RiderDTO riderDTO2 = new RiderDTO();
        assertThat(riderDTO1).isNotEqualTo(riderDTO2);
        riderDTO2.setId(riderDTO1.getId());
        assertThat(riderDTO1).isEqualTo(riderDTO2);
        riderDTO2.setId(2L);
        assertThat(riderDTO1).isNotEqualTo(riderDTO2);
        riderDTO1.setId(null);
        assertThat(riderDTO1).isNotEqualTo(riderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(riderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(riderMapper.fromId(null)).isNull();
    }
}
