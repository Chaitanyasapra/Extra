package com.restapiproject.hotelManagement.controller;
 
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
import java.math.BigDecimal;
import java.util.Arrays;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapiproject.hotelManagement.dto.HotelDto;
import com.restapiproject.hotelManagement.model.Hotel;
import com.restapiproject.hotelManagement.service.HotelService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
 
public class HotelControllerTest {
 
	private MockMvc mockMvc;
	@Mock
	private HotelService hotelService;
	@InjectMocks
	private HotelController hotelController;
	private Hotel hotel1;
	private Hotel hotel2;
	private ObjectMapper objectMapper = new ObjectMapper();
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(hotelController).build();
		hotel1= new Hotel(1L, "Hotel A","Address A",10,5,new BigDecimal("100.00"));
		hotel2= new Hotel(2L, "Hotel B","Address B",20,10,new BigDecimal("200.00"));
	}
	@Test
	void testGetAllHotels() throws Exception {
		when(hotelService.getAllHotels()).thenReturn(Arrays.asList(hotel1, hotel2));
		mockMvc.perform(get("/api/hotels"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.size()").value(2))
		       .andExpect(jsonPath("$[0].name").value("Hotel A"))
		       .andExpect(jsonPath("$[1].name").value("Hotel B"));
		verify(hotelService, times(1)).getAllHotels();
	}
	@Test
	void testGetHotelById() throws Exception {
		when(hotelService.getHotelById(1L)).thenReturn(hotel1);
		mockMvc.perform(get("/api/hotels/1"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.name").value("Hotel A"))
		       .andExpect(jsonPath("$.total_rooms").value(10));
		verify(hotelService, times(1)).getHotelById(1L);
	}
	//post
	@Test
	void testCreateHotel() throws Exception{
		HotelDto dto = new HotelDto(null, "Hotel C","Address C",15,8, new BigDecimal("150.00"));
		Hotel createdHotel = new Hotel(3L,"Hotel C","Address C",15,8, new BigDecimal("150.00"));
		when(hotelService.createHotel(any(Hotel.class))).thenReturn(createdHotel);
		mockMvc.perform(post("/api/hotels")
	       .contentType(MediaType.APPLICATION_JSON)
	       .content(objectMapper.writeValueAsString(dto)))
	       .andExpect(status().isCreated())
	       .andExpect(jsonPath("$.id").value(3))
		    .andExpect(jsonPath("$.name").value("Hotel C"));
	verify(hotelService, times(1)).createHotel(any(Hotel.class));
}
	//put
		@Test
		void testUpdateHotel() throws Exception{
			HotelDto dto = new HotelDto(null, "Hotel A updated","Address A updated",12,9, new BigDecimal("120.00"));
			Hotel updatedHotel = new Hotel(1L,"Hotel A updated","Address A updated",12,9, new BigDecimal("120.00"));
			when(hotelService.updateHotel(eq(1L), any(Hotel.class))).thenReturn(updatedHotel);
			mockMvc.perform(put("/api/hotels/1")
		       .contentType(MediaType.APPLICATION_JSON)
		       .content(objectMapper.writeValueAsString(dto)))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.name").value("Hotel A updated"))
			    .andExpect(jsonPath("$.available_rooms").value(9));
		verify(hotelService, times(1)).updateHotel(eq(1L), any(Hotel.class));
	}
		@Test
		void testDeleteHotel() throws Exception{
			doNothing().when(hotelService).deleteHotel(1L);
			mockMvc.perform(delete("/api/hotels/1"))
			 .andExpect(status().isNoContent());
			verify(hotelService, times(1)).deleteHotel(eq(1L));
		}
	}	