package com.restapiproject.hotelManagement.service;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;

import com.restapiproject.hotelManagement.exception.ResourceNotFoundException;
import com.restapiproject.hotelManagement.model.Hotel;
import com.restapiproject.hotelManagement.repository.HotelDao;

public class HotelServiceImplTest {
	
	@Mock
	private HotelDao hotelDao;
	
	@InjectMocks
	private HotelServiceImpl hotelService;
	private Hotel hotel1;
	private Hotel hotel2;
	
	@BeforeEach //run before each test
	void setup() {
		MockitoAnnotations.openMocks(this); //initalizing mockito annotations: @Mock, @InjectMocks in the test instance
											//@Mock, @InjectMocks in the test instance
		
		hotel1 = new Hotel(1L, "Hotel A", "Address A", 10, 5, new BigDecimal("1000.0"));
		hotel2 = new Hotel(2L, "Hotel B", "Address B", 10, 10, new BigDecimal("2000.0"));
	}
	
	@Test
	void testCreateHotel() {
		when(hotelDao.save(hotel1)).thenReturn(hotel1);
		Hotel created = hotelService.createHotel(hotel1);
		assertNotNull(created);
		assertEquals("Hotel A", created.getName());
		verify(hotelDao, times(1)).save(hotel1);
		
	}
	
	@Test 
	void testGetHotelById_Found() {
		when(hotelDao.findById(1L)).thenReturn(Optional.of(hotel1));
		Hotel found = hotelService.getHotelById(1L);
		assertNotNull(found);
		assertEquals("Hotel A", found.getName());
		verify(hotelDao, times(1)).findById(1L);
	}
	
	@Test 
	void testGetHotelById_NotFound() {
		when(hotelDao.findById(3L)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> hotelService.getHotelById(3L));
		verify(hotelDao, times(1)).findById(3L);
	}
	
	@Test
	void testGetAllHotels() {
		when(hotelDao.findAll()).thenReturn(Arrays.asList(hotel1, hotel2));
		List<Hotel> hotels = hotelService.getAllHotels();
		assertEquals(2, hotels.size());
		verify(hotelDao, times(1)).findAll();
	}
	
	@Test
	void testUpdateHotel_Success() {
		when(hotelDao.findById(1L)).thenReturn(Optional.of(hotel1));
		when(hotelDao.update(any(Hotel.class))).thenReturn(1);
		
		Hotel updateHotel = new Hotel(null, "Hotel A updated", "Address updated", 15, 8, new BigDecimal("150.00"));
		Hotel result = hotelService.updateHotel(1L, updateHotel);
		
		assertAll("Update hotel properties",
				() -> assertEquals("Hotel A updated", result.getName()),
				() -> assertEquals("Address updated", result.getAddress()),
				() -> assertEquals(15, result.getTotal_rooms()),
				() -> assertEquals(8, result.getAvailable_rooms()),
				() -> assertEquals(new BigDecimal("150.00"), result.getPrice_per_night())
				);
		verify(hotelDao, times(1)).update(any(Hotel.class));
	}
	
	@Test
	void testUpdateHotel_Failure() {
		when(hotelDao.findById(1L)).thenReturn(Optional.of(hotel1));
		when(hotelDao.update(any(Hotel.class))).thenReturn(0);
		
		Hotel updateHotel = new Hotel(null, "Hotel A updated", "Address updated", 15, 8, new BigDecimal("150.00"));
		
		RuntimeException exception = assertThrows(RuntimeException.class, () -> hotelService.updateHotel(1L, updateHotel));
		assertEquals("Updation failed for id: 1", exception.getMessage());
		verify(hotelDao, times(1)).update(any(Hotel.class));
	}
		
	@Test
	void testDeleteHotel_Success() {
		
		when(hotelDao.findById(1L)).thenReturn(Optional.of(hotel1));
		when(hotelDao.deleteById(1L)).thenReturn(1);
		
		assertDoesNotThrow(()->hotelService.deleteHotel(1l));
		verify(hotelDao, times(1)).deleteById(1L);
	}
	
	@Test
	void testDeleteHotel_Failure() {
		when(hotelDao.findById(1L)).thenReturn(Optional.of(hotel1));
		when(hotelDao.deleteById(1L)).thenReturn(0);
		
		RuntimeException exception = assertThrows(RuntimeException.class, ()->hotelService.deleteHotel(1l));
		assertEquals("delete failed for hotel id 1", exception.getMessage());
		verify(hotelDao, times(1)).deleteById(1L);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"Hotel A", "Hotel B", "Hotel C"})
	void testParameterizedHotelNames(String name) {
		Hotel hotel = new Hotel();
		hotel.setName(name);
		assertNotNull(hotel.getName());
		assertTrue(hotel.getName().startsWith("Hotel"));
	}
	
}
