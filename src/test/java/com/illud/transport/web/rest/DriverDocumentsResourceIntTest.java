package com.illud.transport.web.rest;

import com.illud.transport.TransportApp;

import com.illud.transport.domain.DriverDocuments;
import com.illud.transport.repository.DriverDocumentsRepository;
import com.illud.transport.repository.search.DriverDocumentsSearchRepository;
import com.illud.transport.service.DriverDocumentsService;
import com.illud.transport.service.dto.DriverDocumentsDTO;
import com.illud.transport.service.mapper.DriverDocumentsMapper;
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
 * Test class for the DriverDocumentsResource REST controller.
 *
 * @see DriverDocumentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransportApp.class)
public class DriverDocumentsResourceIntTest {

    @Autowired
    private DriverDocumentsRepository driverDocumentsRepository;

    @Autowired
    private DriverDocumentsMapper driverDocumentsMapper;

    @Autowired
    private DriverDocumentsService driverDocumentsService;

    /**
     * This repository is mocked in the com.illud.transport.repository.search test package.
     *
     * @see com.illud.transport.repository.search.DriverDocumentsSearchRepositoryMockConfiguration
     */
    @Autowired
    private DriverDocumentsSearchRepository mockDriverDocumentsSearchRepository;

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

    private MockMvc restDriverDocumentsMockMvc;

    private DriverDocuments driverDocuments;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DriverDocumentsResource driverDocumentsResource = new DriverDocumentsResource(driverDocumentsService);
        this.restDriverDocumentsMockMvc = MockMvcBuilders.standaloneSetup(driverDocumentsResource)
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
    public static DriverDocuments createEntity(EntityManager em) {
        DriverDocuments driverDocuments = new DriverDocuments();
        return driverDocuments;
    }

    @Before
    public void initTest() {
        driverDocuments = createEntity(em);
    }

    @Test
    @Transactional
    public void createDriverDocuments() throws Exception {
        int databaseSizeBeforeCreate = driverDocumentsRepository.findAll().size();

        // Create the DriverDocuments
        DriverDocumentsDTO driverDocumentsDTO = driverDocumentsMapper.toDto(driverDocuments);
        restDriverDocumentsMockMvc.perform(post("/api/driver-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverDocumentsDTO)))
            .andExpect(status().isCreated());

        // Validate the DriverDocuments in the database
        List<DriverDocuments> driverDocumentsList = driverDocumentsRepository.findAll();
        assertThat(driverDocumentsList).hasSize(databaseSizeBeforeCreate + 1);
        DriverDocuments testDriverDocuments = driverDocumentsList.get(driverDocumentsList.size() - 1);

        // Validate the DriverDocuments in Elasticsearch
        verify(mockDriverDocumentsSearchRepository, times(1)).save(testDriverDocuments);
    }

    @Test
    @Transactional
    public void createDriverDocumentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = driverDocumentsRepository.findAll().size();

        // Create the DriverDocuments with an existing ID
        driverDocuments.setId(1L);
        DriverDocumentsDTO driverDocumentsDTO = driverDocumentsMapper.toDto(driverDocuments);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDriverDocumentsMockMvc.perform(post("/api/driver-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverDocumentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DriverDocuments in the database
        List<DriverDocuments> driverDocumentsList = driverDocumentsRepository.findAll();
        assertThat(driverDocumentsList).hasSize(databaseSizeBeforeCreate);

        // Validate the DriverDocuments in Elasticsearch
        verify(mockDriverDocumentsSearchRepository, times(0)).save(driverDocuments);
    }

    @Test
    @Transactional
    public void getAllDriverDocuments() throws Exception {
        // Initialize the database
        driverDocumentsRepository.saveAndFlush(driverDocuments);

        // Get all the driverDocumentsList
        restDriverDocumentsMockMvc.perform(get("/api/driver-documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driverDocuments.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getDriverDocuments() throws Exception {
        // Initialize the database
        driverDocumentsRepository.saveAndFlush(driverDocuments);

        // Get the driverDocuments
        restDriverDocumentsMockMvc.perform(get("/api/driver-documents/{id}", driverDocuments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(driverDocuments.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDriverDocuments() throws Exception {
        // Get the driverDocuments
        restDriverDocumentsMockMvc.perform(get("/api/driver-documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDriverDocuments() throws Exception {
        // Initialize the database
        driverDocumentsRepository.saveAndFlush(driverDocuments);

        int databaseSizeBeforeUpdate = driverDocumentsRepository.findAll().size();

        // Update the driverDocuments
        DriverDocuments updatedDriverDocuments = driverDocumentsRepository.findById(driverDocuments.getId()).get();
        // Disconnect from session so that the updates on updatedDriverDocuments are not directly saved in db
        em.detach(updatedDriverDocuments);
        DriverDocumentsDTO driverDocumentsDTO = driverDocumentsMapper.toDto(updatedDriverDocuments);

        restDriverDocumentsMockMvc.perform(put("/api/driver-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverDocumentsDTO)))
            .andExpect(status().isOk());

        // Validate the DriverDocuments in the database
        List<DriverDocuments> driverDocumentsList = driverDocumentsRepository.findAll();
        assertThat(driverDocumentsList).hasSize(databaseSizeBeforeUpdate);
        DriverDocuments testDriverDocuments = driverDocumentsList.get(driverDocumentsList.size() - 1);

        // Validate the DriverDocuments in Elasticsearch
        verify(mockDriverDocumentsSearchRepository, times(1)).save(testDriverDocuments);
    }

    @Test
    @Transactional
    public void updateNonExistingDriverDocuments() throws Exception {
        int databaseSizeBeforeUpdate = driverDocumentsRepository.findAll().size();

        // Create the DriverDocuments
        DriverDocumentsDTO driverDocumentsDTO = driverDocumentsMapper.toDto(driverDocuments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverDocumentsMockMvc.perform(put("/api/driver-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverDocumentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DriverDocuments in the database
        List<DriverDocuments> driverDocumentsList = driverDocumentsRepository.findAll();
        assertThat(driverDocumentsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DriverDocuments in Elasticsearch
        verify(mockDriverDocumentsSearchRepository, times(0)).save(driverDocuments);
    }

    @Test
    @Transactional
    public void deleteDriverDocuments() throws Exception {
        // Initialize the database
        driverDocumentsRepository.saveAndFlush(driverDocuments);

        int databaseSizeBeforeDelete = driverDocumentsRepository.findAll().size();

        // Delete the driverDocuments
        restDriverDocumentsMockMvc.perform(delete("/api/driver-documents/{id}", driverDocuments.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DriverDocuments> driverDocumentsList = driverDocumentsRepository.findAll();
        assertThat(driverDocumentsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DriverDocuments in Elasticsearch
        verify(mockDriverDocumentsSearchRepository, times(1)).deleteById(driverDocuments.getId());
    }

    @Test
    @Transactional
    public void searchDriverDocuments() throws Exception {
        // Initialize the database
        driverDocumentsRepository.saveAndFlush(driverDocuments);
        when(mockDriverDocumentsSearchRepository.search(queryStringQuery("id:" + driverDocuments.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(driverDocuments), PageRequest.of(0, 1), 1));
        // Search the driverDocuments
        restDriverDocumentsMockMvc.perform(get("/api/_search/driver-documents?query=id:" + driverDocuments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driverDocuments.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DriverDocuments.class);
        DriverDocuments driverDocuments1 = new DriverDocuments();
        driverDocuments1.setId(1L);
        DriverDocuments driverDocuments2 = new DriverDocuments();
        driverDocuments2.setId(driverDocuments1.getId());
        assertThat(driverDocuments1).isEqualTo(driverDocuments2);
        driverDocuments2.setId(2L);
        assertThat(driverDocuments1).isNotEqualTo(driverDocuments2);
        driverDocuments1.setId(null);
        assertThat(driverDocuments1).isNotEqualTo(driverDocuments2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DriverDocumentsDTO.class);
        DriverDocumentsDTO driverDocumentsDTO1 = new DriverDocumentsDTO();
        driverDocumentsDTO1.setId(1L);
        DriverDocumentsDTO driverDocumentsDTO2 = new DriverDocumentsDTO();
        assertThat(driverDocumentsDTO1).isNotEqualTo(driverDocumentsDTO2);
        driverDocumentsDTO2.setId(driverDocumentsDTO1.getId());
        assertThat(driverDocumentsDTO1).isEqualTo(driverDocumentsDTO2);
        driverDocumentsDTO2.setId(2L);
        assertThat(driverDocumentsDTO1).isNotEqualTo(driverDocumentsDTO2);
        driverDocumentsDTO1.setId(null);
        assertThat(driverDocumentsDTO1).isNotEqualTo(driverDocumentsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(driverDocumentsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(driverDocumentsMapper.fromId(null)).isNull();
    }
}
