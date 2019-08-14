package com.illud.transport.web.rest;

import com.illud.transport.TransportApp;

import com.illud.transport.domain.Ride;
import com.illud.transport.repository.RideRepository;
import com.illud.transport.repository.search.RideSearchRepository;
import com.illud.transport.service.RideService;
import com.illud.transport.service.dto.RideDTO;
import com.illud.transport.service.mapper.RideMapper;
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
 * Test class for the RideResource REST controller.
 *
 * @see RideResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransportApp.class)
public class RideResourceIntTest {

    private static final String DEFAULT_DRIVER_ID = "AAAAAAAAAA";
    private static final String UPDATED_DRIVER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RIDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_RIDER_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADDRESS_STARTING_POINT = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_STARTING_POINT = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_DESTINATION = "BBBBBBBBBB";

    private static final Double DEFAULT_FARE = 1D;
    private static final Double UPDATED_FARE = 2D;

    private static final Double DEFAULT_TOTAL_DISTANCE = 1D;
    private static final Double UPDATED_TOTAL_DISTANCE = 2D;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideMapper rideMapper;

    @Autowired
    private RideService rideService;

    /**
     * This repository is mocked in the com.illud.transport.repository.search test package.
     *
     * @see com.illud.transport.repository.search.RideSearchRepositoryMockConfiguration
     */
    @Autowired
    private RideSearchRepository mockRideSearchRepository;

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

    private MockMvc restRideMockMvc;

    private Ride ride;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RideResource rideResource = new RideResource(rideService);
        this.restRideMockMvc = MockMvcBuilders.standaloneSetup(rideResource)
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
    public static Ride createEntity(EntityManager em) {
        Ride ride = new Ride()
            .driverId(DEFAULT_DRIVER_ID)
            .riderId(DEFAULT_RIDER_ID)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .addressStartingPoint(DEFAULT_ADDRESS_STARTING_POINT)
            .addressDestination(DEFAULT_ADDRESS_DESTINATION)
            .fare(DEFAULT_FARE)
            .totalDistance(DEFAULT_TOTAL_DISTANCE);
        return ride;
    }

    @Before
    public void initTest() {
        ride = createEntity(em);
    }

    @Test
    @Transactional
    public void createRide() throws Exception {
        int databaseSizeBeforeCreate = rideRepository.findAll().size();

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);
        restRideMockMvc.perform(post("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isCreated());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeCreate + 1);
        Ride testRide = rideList.get(rideList.size() - 1);
        assertThat(testRide.getDriverId()).isEqualTo(DEFAULT_DRIVER_ID);
        assertThat(testRide.getRiderId()).isEqualTo(DEFAULT_RIDER_ID);
        assertThat(testRide.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testRide.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testRide.getAddressStartingPoint()).isEqualTo(DEFAULT_ADDRESS_STARTING_POINT);
        assertThat(testRide.getAddressDestination()).isEqualTo(DEFAULT_ADDRESS_DESTINATION);
        assertThat(testRide.getFare()).isEqualTo(DEFAULT_FARE);
        assertThat(testRide.getTotalDistance()).isEqualTo(DEFAULT_TOTAL_DISTANCE);

        // Validate the Ride in Elasticsearch
        verify(mockRideSearchRepository, times(1)).save(testRide);
    }

    @Test
    @Transactional
    public void createRideWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rideRepository.findAll().size();

        // Create the Ride with an existing ID
        ride.setId(1L);
        RideDTO rideDTO = rideMapper.toDto(ride);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRideMockMvc.perform(post("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeCreate);

        // Validate the Ride in Elasticsearch
        verify(mockRideSearchRepository, times(0)).save(ride);
    }

    @Test
    @Transactional
    public void getAllRides() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList
        restRideMockMvc.perform(get("/api/rides?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ride.getId().intValue())))
            .andExpect(jsonPath("$.[*].driverId").value(hasItem(DEFAULT_DRIVER_ID.toString())))
            .andExpect(jsonPath("$.[*].riderId").value(hasItem(DEFAULT_RIDER_ID.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].addressStartingPoint").value(hasItem(DEFAULT_ADDRESS_STARTING_POINT.toString())))
            .andExpect(jsonPath("$.[*].addressDestination").value(hasItem(DEFAULT_ADDRESS_DESTINATION.toString())))
            .andExpect(jsonPath("$.[*].fare").value(hasItem(DEFAULT_FARE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalDistance").value(hasItem(DEFAULT_TOTAL_DISTANCE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get the ride
        restRideMockMvc.perform(get("/api/rides/{id}", ride.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ride.getId().intValue()))
            .andExpect(jsonPath("$.driverId").value(DEFAULT_DRIVER_ID.toString()))
            .andExpect(jsonPath("$.riderId").value(DEFAULT_RIDER_ID.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.addressStartingPoint").value(DEFAULT_ADDRESS_STARTING_POINT.toString()))
            .andExpect(jsonPath("$.addressDestination").value(DEFAULT_ADDRESS_DESTINATION.toString()))
            .andExpect(jsonPath("$.fare").value(DEFAULT_FARE.doubleValue()))
            .andExpect(jsonPath("$.totalDistance").value(DEFAULT_TOTAL_DISTANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRide() throws Exception {
        // Get the ride
        restRideMockMvc.perform(get("/api/rides/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        int databaseSizeBeforeUpdate = rideRepository.findAll().size();

        // Update the ride
        Ride updatedRide = rideRepository.findById(ride.getId()).get();
        // Disconnect from session so that the updates on updatedRide are not directly saved in db
        em.detach(updatedRide);
        updatedRide
            .driverId(UPDATED_DRIVER_ID)
            .riderId(UPDATED_RIDER_ID)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .addressStartingPoint(UPDATED_ADDRESS_STARTING_POINT)
            .addressDestination(UPDATED_ADDRESS_DESTINATION)
            .fare(UPDATED_FARE)
            .totalDistance(UPDATED_TOTAL_DISTANCE);
        RideDTO rideDTO = rideMapper.toDto(updatedRide);

        restRideMockMvc.perform(put("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isOk());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
        Ride testRide = rideList.get(rideList.size() - 1);
        assertThat(testRide.getDriverId()).isEqualTo(UPDATED_DRIVER_ID);
        assertThat(testRide.getRiderId()).isEqualTo(UPDATED_RIDER_ID);
        assertThat(testRide.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testRide.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testRide.getAddressStartingPoint()).isEqualTo(UPDATED_ADDRESS_STARTING_POINT);
        assertThat(testRide.getAddressDestination()).isEqualTo(UPDATED_ADDRESS_DESTINATION);
        assertThat(testRide.getFare()).isEqualTo(UPDATED_FARE);
        assertThat(testRide.getTotalDistance()).isEqualTo(UPDATED_TOTAL_DISTANCE);

        // Validate the Ride in Elasticsearch
        verify(mockRideSearchRepository, times(1)).save(testRide);
    }

    @Test
    @Transactional
    public void updateNonExistingRide() throws Exception {
        int databaseSizeBeforeUpdate = rideRepository.findAll().size();

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRideMockMvc.perform(put("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ride in Elasticsearch
        verify(mockRideSearchRepository, times(0)).save(ride);
    }

    @Test
    @Transactional
    public void deleteRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        int databaseSizeBeforeDelete = rideRepository.findAll().size();

        // Delete the ride
        restRideMockMvc.perform(delete("/api/rides/{id}", ride.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Ride in Elasticsearch
        verify(mockRideSearchRepository, times(1)).deleteById(ride.getId());
    }

    @Test
    @Transactional
    public void searchRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);
        when(mockRideSearchRepository.search(queryStringQuery("id:" + ride.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(ride), PageRequest.of(0, 1), 1));
        // Search the ride
        restRideMockMvc.perform(get("/api/_search/rides?query=id:" + ride.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ride.getId().intValue())))
            .andExpect(jsonPath("$.[*].driverId").value(hasItem(DEFAULT_DRIVER_ID)))
            .andExpect(jsonPath("$.[*].riderId").value(hasItem(DEFAULT_RIDER_ID)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].addressStartingPoint").value(hasItem(DEFAULT_ADDRESS_STARTING_POINT)))
            .andExpect(jsonPath("$.[*].addressDestination").value(hasItem(DEFAULT_ADDRESS_DESTINATION)))
            .andExpect(jsonPath("$.[*].fare").value(hasItem(DEFAULT_FARE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalDistance").value(hasItem(DEFAULT_TOTAL_DISTANCE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ride.class);
        Ride ride1 = new Ride();
        ride1.setId(1L);
        Ride ride2 = new Ride();
        ride2.setId(ride1.getId());
        assertThat(ride1).isEqualTo(ride2);
        ride2.setId(2L);
        assertThat(ride1).isNotEqualTo(ride2);
        ride1.setId(null);
        assertThat(ride1).isNotEqualTo(ride2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RideDTO.class);
        RideDTO rideDTO1 = new RideDTO();
        rideDTO1.setId(1L);
        RideDTO rideDTO2 = new RideDTO();
        assertThat(rideDTO1).isNotEqualTo(rideDTO2);
        rideDTO2.setId(rideDTO1.getId());
        assertThat(rideDTO1).isEqualTo(rideDTO2);
        rideDTO2.setId(2L);
        assertThat(rideDTO1).isNotEqualTo(rideDTO2);
        rideDTO1.setId(null);
        assertThat(rideDTO1).isNotEqualTo(rideDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rideMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rideMapper.fromId(null)).isNull();
    }
}
