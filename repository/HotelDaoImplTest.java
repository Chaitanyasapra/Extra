package com.restapiproject.hotelManagement.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import com.restapiproject.hotelManagement.model.Hotel;
import com.restapiproject.hotelManagement.util.HotelMapper;

public class HotelDaoImplTest {
	
	//mock the jdbc template- unit test with no database
	//mockito is a java mocking framework
	//mockito allows developer to create and manage mock objects
	//simulate the behavior of real objects
	
	//@Mock -> jdbcTemplate -> 
	//InjectMocks -> construct the dao and inject the mock- jdbcTemplate
	
	@Mock
	private JdbcTemplate jdbcTemplate;
	
	@InjectMocks //create instance of hotelDaoImpl and inject the mock- jdbcTemplate
	private HotelDaoImpl hotelDao;
	
	private Hotel hotel1;
	private Hotel hotel2;
	
	@BeforeEach //run before each test
	void setup() {
		MockitoAnnotations.openMocks(this); //initalizing mockito annotations: @Mock, @InjectMocks in the test instance
											//@Mock, @InjectMocks in the test instance
		
		hotel1 = new Hotel(1L, "Hotel A", "Address A", 10, 5, new BigDecimal("1000.0"));
		hotel2 = new Hotel(2L, "Hotel B", "Address B", 10, 10, new BigDecimal("2000.0"));
	}
	
	//----- CRUD Test -----
	
	@Test
	void testSave() {
		doAnswer(invocation -> {
			KeyHolder keyHolder = invocation.getArgument(1);
			keyHolder.getKeyList().add(Map.of("GENERATED_KEY", 1L));
			return 1;
			
		}).when(jdbcTemplate).update(any(), any(KeyHolder.class));
		
		Hotel result = hotelDao.save(hotel1);
		assertNotNull(result);
	}
	
//	returned obejct is not null
//	call jdbcTemplate -> update -> 
//	real jdbc -> execute insert -> fill the KeyHolder -> with generated PK
//	-> getKey() -> getting that key- set it on hotel instance
//	doAnswer -> intercept the mock call -> tells Mockito to run provided lambda function
//	invocation -> object representing the intercepted method call
//	getArgument(1) -> fetch the second argument
	
	@Test
	void testFindById_Found() {
		when(jdbcTemplate.query(anyString(), any(HotelMapper.class), eq(1L)))
		.thenReturn(Arrays.asList(hotel1));
		Optional<Hotel> result = hotelDao.findById(1L);
		assertTrue(result.isPresent()); //confirm dao returned a non empty optional
		assertEquals("Hotel A", result.get().getName()); //matching for hotel name
	}
	
	@Test
	void testFindById_NotFound() {
		when(jdbcTemplate.query(anyString(), any(HotelMapper.class), eq(3L)))
		.thenReturn(Arrays.asList(hotel1));
		Optional<Hotel> result = hotelDao.findById(31L);
		assertFalse(result.isPresent()); //confirm dao returned a non empty optional
	}

	@Test
	void testFindAll() {
		when(jdbcTemplate.query(anyString(), any(HotelMapper.class)))
		.thenReturn(Arrays.asList(hotel1, hotel2));
		List<Hotel> result = hotelDao.findAll();
		
		assertEquals(2, result.size()); //matching for hotel name
	}
	
	@Test
	void testUpdate() {
		when(jdbcTemplate.update(anyString(),
				anyString(),anyString(),anyInt(),anyInt(), any(BigDecimal.class), anyLong()))
		        .thenReturn(1); // 1 row affected
		int rows = hotelDao.update(hotel1);
		assertEquals(1,rows);
	}
	
	@Test
	void testDelete() {
		when(jdbcTemplate.update(anyString(), anyLong()))
		.thenReturn(1); //1 row 
		int rows = hotelDao.deleteById(1L);
		assertEquals(1, rows);
	}
	
	@ParameterizedTest //we are providing 3 values, it will run the test multiple times
	@ValueSource(strings = {"Hotel X", "Hotel Y", "Hotel Z"})
	void testParameterizedHotelNames(String name) {
		Hotel hotel = new Hotel();
		hotel.setName(name);
		assertNotNull(hotel.getName());
		assertTrue(hotel.getName().startsWith("Hotel"));
		
		//for each name- create a new Hotel, set the name, verify the name is not null and start with hotel
	}
	
	//Skip test

	@Disabled("Example- skipped dao test")
	@Test
	void testDisableExample() {
		fail("This DAO test is disabled and skipped");
		//test was enabled- it would have been failed
		//learning how to skip test
	}
	
	

}
