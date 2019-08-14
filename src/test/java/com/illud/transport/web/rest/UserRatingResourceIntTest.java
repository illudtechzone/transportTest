package com.illud.transport.web.rest;

import com.illud.transport.TransportApp;

import com.illud.transport.domain.UserRating;
import com.illud.transport.repository.UserRatingRepository;
import com.illud.transport.repository.search.UserRatingSearchRepository;
import com.illud.transport.service.UserRatingService;
import com.illud.transport.service.dto.UserRatingDTO;
import com.illud.transport.service.mapper.UserRatingMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Test class for the UserRatingResource REST controller.
 *
 * @see UserRatingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransportApp.class)
public class UserRatingResourceIntTest {

    private static final String DEFAULT_I_D_PCODE = "AAAAAAAAAA";
    private static final String UPDATED_I_D_PCODE = "BBBBBBBBBB";

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;

    private static final Instant DEFAULT_RATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private UserRatingRepository userRatingRepository;

    @Autowired
    private UserRatingMapper userRatingMapper;

    @Autowired
    private UserRatingService userRatingService;

    /**
     * This repository is mocked in the com.illud.transport.repository.search test package.
     *
     * @see com.illud.transport.repository.search.UserRatingSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserRatingSearchRepository mockUserRatingSearchRepository;

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

    private MockMvc restUserRatingMockMvc;

    private UserRating userRating;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserRatingResource userRatingResource = new UserRatingResource(userRatingService);
        this.restUserRatingMockMvc = MockMvcBuilders.standaloneSetup(userRatingResource)
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
    public static UserRating createEntity(EntityManager em) {
        UserRating userRating = new UserRating()
            .iDPcode(DEFAULT_I_D_PCODE)
            .rating(DEFAULT_RATING)
            .ratedOn(DEFAULT_RATED_ON);
        return userRating;
    }

    @Before
    public void initTest() {
        userRating = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserRating() throws Exception {
        int databaseSizeBeforeCreate = userRatingRepository.findAll().size();

        // Create the UserRating
        UserRatingDTO userRatingDTO = userRatingMapper.toDto(userRating);
        restUserRatingMockMvc.perform(post("/api/user-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRatingDTO)))
            .andExpect(status().isCreated());

        // Validate the UserRating in the database
        List<UserRating> userRatingList = userRatingRepository.findAll();
        assertThat(userRatingList).hasSize(databaseSizeBeforeCreate + 1);
        UserRating testUserRating = userRatingList.get(userRatingList.size() - 1);
        assertThat(testUserRating.getiDPcode()).isEqualTo(DEFAULT_I_D_PCODE);
        assertThat(testUserRating.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testUserRating.getRatedOn()).isEqualTo(DEFAULT_RATED_ON);

        // Validate the UserRating in Elasticsearch
        verify(mockUserRatingSearchRepository, times(1)).save(testUserRating);
    }

    @Test
    @Transactional
    public void createUserRatingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRatingRepository.findAll().size();

        // Create the UserRating with an existing ID
        userRating.setId(1L);
        UserRatingDTO userRatingDTO = userRatingMapper.toDto(userRating);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRatingMockMvc.perform(post("/api/user-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRatingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserRating in the database
        List<UserRating> userRatingList = userRatingRepository.findAll();
        assertThat(userRatingList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserRating in Elasticsearch
        verify(mockUserRatingSearchRepository, times(0)).save(userRating);
    }

    @Test
    @Transactional
    public void getAllUserRatings() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get all the userRatingList
        restUserRatingMockMvc.perform(get("/api/user-ratings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRating.getId().intValue())))
            .andExpect(jsonPath("$.[*].iDPcode").value(hasItem(DEFAULT_I_D_PCODE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].ratedOn").value(hasItem(DEFAULT_RATED_ON.toString())));
    }
    
    @Test
    @Transactional
    public void getUserRating() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        // Get the userRating
        restUserRatingMockMvc.perform(get("/api/user-ratings/{id}", userRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userRating.getId().intValue()))
            .andExpect(jsonPath("$.iDPcode").value(DEFAULT_I_D_PCODE.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.ratedOn").value(DEFAULT_RATED_ON.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserRating() throws Exception {
        // Get the userRating
        restUserRatingMockMvc.perform(get("/api/user-ratings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserRating() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        int databaseSizeBeforeUpdate = userRatingRepository.findAll().size();

        // Update the userRating
        UserRating updatedUserRating = userRatingRepository.findById(userRating.getId()).get();
        // Disconnect from session so that the updates on updatedUserRating are not directly saved in db
        em.detach(updatedUserRating);
        updatedUserRating
            .iDPcode(UPDATED_I_D_PCODE)
            .rating(UPDATED_RATING)
            .ratedOn(UPDATED_RATED_ON);
        UserRatingDTO userRatingDTO = userRatingMapper.toDto(updatedUserRating);

        restUserRatingMockMvc.perform(put("/api/user-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRatingDTO)))
            .andExpect(status().isOk());

        // Validate the UserRating in the database
        List<UserRating> userRatingList = userRatingRepository.findAll();
        assertThat(userRatingList).hasSize(databaseSizeBeforeUpdate);
        UserRating testUserRating = userRatingList.get(userRatingList.size() - 1);
        assertThat(testUserRating.getiDPcode()).isEqualTo(UPDATED_I_D_PCODE);
        assertThat(testUserRating.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testUserRating.getRatedOn()).isEqualTo(UPDATED_RATED_ON);

        // Validate the UserRating in Elasticsearch
        verify(mockUserRatingSearchRepository, times(1)).save(testUserRating);
    }

    @Test
    @Transactional
    public void updateNonExistingUserRating() throws Exception {
        int databaseSizeBeforeUpdate = userRatingRepository.findAll().size();

        // Create the UserRating
        UserRatingDTO userRatingDTO = userRatingMapper.toDto(userRating);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRatingMockMvc.perform(put("/api/user-ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRatingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserRating in the database
        List<UserRating> userRatingList = userRatingRepository.findAll();
        assertThat(userRatingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserRating in Elasticsearch
        verify(mockUserRatingSearchRepository, times(0)).save(userRating);
    }

    @Test
    @Transactional
    public void deleteUserRating() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);

        int databaseSizeBeforeDelete = userRatingRepository.findAll().size();

        // Delete the userRating
        restUserRatingMockMvc.perform(delete("/api/user-ratings/{id}", userRating.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserRating> userRatingList = userRatingRepository.findAll();
        assertThat(userRatingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserRating in Elasticsearch
        verify(mockUserRatingSearchRepository, times(1)).deleteById(userRating.getId());
    }

    @Test
    @Transactional
    public void searchUserRating() throws Exception {
        // Initialize the database
        userRatingRepository.saveAndFlush(userRating);
        when(mockUserRatingSearchRepository.search(queryStringQuery("id:" + userRating.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userRating), PageRequest.of(0, 1), 1));
        // Search the userRating
        restUserRatingMockMvc.perform(get("/api/_search/user-ratings?query=id:" + userRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRating.getId().intValue())))
            .andExpect(jsonPath("$.[*].iDPcode").value(hasItem(DEFAULT_I_D_PCODE)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].ratedOn").value(hasItem(DEFAULT_RATED_ON.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRating.class);
        UserRating userRating1 = new UserRating();
        userRating1.setId(1L);
        UserRating userRating2 = new UserRating();
        userRating2.setId(userRating1.getId());
        assertThat(userRating1).isEqualTo(userRating2);
        userRating2.setId(2L);
        assertThat(userRating1).isNotEqualTo(userRating2);
        userRating1.setId(null);
        assertThat(userRating1).isNotEqualTo(userRating2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRatingDTO.class);
        UserRatingDTO userRatingDTO1 = new UserRatingDTO();
        userRatingDTO1.setId(1L);
        UserRatingDTO userRatingDTO2 = new UserRatingDTO();
        assertThat(userRatingDTO1).isNotEqualTo(userRatingDTO2);
        userRatingDTO2.setId(userRatingDTO1.getId());
        assertThat(userRatingDTO1).isEqualTo(userRatingDTO2);
        userRatingDTO2.setId(2L);
        assertThat(userRatingDTO1).isNotEqualTo(userRatingDTO2);
        userRatingDTO1.setId(null);
        assertThat(userRatingDTO1).isNotEqualTo(userRatingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userRatingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userRatingMapper.fromId(null)).isNull();
    }
}
