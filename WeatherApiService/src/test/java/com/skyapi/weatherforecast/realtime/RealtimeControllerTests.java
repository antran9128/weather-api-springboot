package com.skyapi.weatherforecast.realtime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.GeolocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
@WebMvcTest(RealtimeWeatherApiController.class)
public class RealtimeControllerTests {
	private static final String END_POINT_PATH = "/v1/realtime";
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper mapper;
	@MockBean RealtimeWeatherService realtimeWeatherService;
	@MockBean GeolocationService geolocationService;
	
	@Test
	public void testGetShouldReturnStatus400BadRequest() throws Exception, GeolocationException {
		Mockito.when(geolocationService.getLocation(Mockito.anyString()))
		.thenThrow(GeolocationException.class);
		
		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isBadRequest()).andDo(print());
	}
	
	@Test
	public void testGetShouldReturnStatus404NotFound() throws GeolocationException,LocationNotFoundException, Exception{
		Location location = new Location();
		Mockito.when(geolocationService.getLocation(Mockito.anyString()))
		.thenReturn(location);
		
		Mockito.when(realtimeWeatherService.getByLocation(location))
		.thenThrow(LocationNotFoundException.class);
		
		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isNotFound()).andDo(print());
	}
	
	
	@Test
	public void testGetShouldReturnStatus200Ok() throws GeolocationException,LocationNotFoundException, Exception{
		Location location = new Location();
		location.setCode("SFCA_USA");
		location.setCityName("San Francisco");
		location.setRegionName("California");
		location.setCountryName("US");
		location.setCountryCode("US");
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();

        realtimeWeather.setTemperature(-2);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setPrecipitation(42);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(12);
        realtimeWeather.setLastUpdated(new Date());
        realtimeWeather.setLocation(location);
        
        location.setRealtimeWeather(realtimeWeather);
        
        
		Mockito.when(geolocationService.getLocation(Mockito.anyString()))
		.thenReturn(location);
		
		Mockito.when(realtimeWeatherService.getByLocation(location))
		.thenReturn(realtimeWeather);
		
		String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();
		
		mockMvc.perform(get(END_POINT_PATH))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.location", is(expectedLocation)))
		.andDo(print()); 
	}
	
	@Test
	public void testGetByLocationCodeShouldReturnStatus404NotFound() throws Exception {
		String code = "HN";
		
		Mockito.when(realtimeWeatherService.getByLocationCode(code))
		.thenThrow(LocationNotFoundException.class);
		
		String path = END_POINT_PATH + "/" + code;
		
		mockMvc.perform(get(path))
		.andExpect(status().isNotFound())
		.andDo(print()); 
	}
	
	@Test
	public void testGetByLocationCodeShouldReturnStatus200OK() throws Exception {
		String code = "SFCA_USA";
		Location location = new Location();
		location.setCode("SFCA_USA");
		location.setCityName("San Francisco");
		location.setRegionName("California");
		location.setCountryName("US");
		location.setCountryCode("US");
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();

        realtimeWeather.setTemperature(-2);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setPrecipitation(42);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(12);
        realtimeWeather.setLastUpdated(new Date());
        realtimeWeather.setLocation(location);
        
        location.setRealtimeWeather(realtimeWeather);
        
		Mockito.when(realtimeWeatherService.getByLocationCode(code))
		.thenReturn(realtimeWeather);
		
		String path = END_POINT_PATH + "/" + code;
		String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();
		
		mockMvc.perform(get(path))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.location", is(expectedLocation)))
		.andDo(print()); 
	}
	
	@Test
	public void testUpdateShouldReturn400BadRequest() throws Exception {
		String code = "SFCA_USA";
		String path = END_POINT_PATH + "/" + code;
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();

        realtimeWeather.setTemperature(120);
        realtimeWeather.setHumidity(132);
        realtimeWeather.setPrecipitation(100);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(500);
        
        String bodyContent = mapper.writeValueAsString(realtimeWeather);
        
        mockMvc.perform(put(path).contentType("application/json").content(bodyContent))
		.andExpect(status().isBadRequest())
		.andDo(print()); 
	}
	
	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {
		String code = "SFCA_USA";
		String path = END_POINT_PATH + "/" + code;
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();

        realtimeWeather.setTemperature(12);
        realtimeWeather.setHumidity(13);
        realtimeWeather.setPrecipitation(88);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(5);
        
        Mockito.when(realtimeWeatherService.update(realtimeWeather, code))
		.thenThrow(LocationNotFoundException.class);
        
        String bodyContent = mapper.writeValueAsString(realtimeWeather);
        
        mockMvc.perform(put(path).contentType("application/json").content(bodyContent))
		.andExpect(status().isNotFound())
		.andDo(print()); 
	}
	
	@Test
	public void testUpdateShouldReturn200Ok() throws Exception {
		String code = "SFCA_USA";
		String path = END_POINT_PATH + "/" + code;
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();

        realtimeWeather.setTemperature(12);
        realtimeWeather.setHumidity(13);
        realtimeWeather.setPrecipitation(88);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(5);
        realtimeWeather.setLastUpdated(new Date());
        
        Location location = new Location();
		location.setCode(code);
		location.setCityName("San Francisco");
		location.setRegionName("California");
		location.setCountryName("US");
		location.setCountryCode("US");
		
		realtimeWeather.setLocation(location);
		location.setRealtimeWeather(realtimeWeather);
		
		
        Mockito.when(realtimeWeatherService.update(realtimeWeather, code))
		.thenReturn(realtimeWeather);
        
        
        String bodyContent = mapper.writeValueAsString(realtimeWeather);
        String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();
		
        mockMvc.perform(put(path).contentType("application/json").content(bodyContent))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.location", is(expectedLocation)))
		.andDo(print()); 
	}
	
	
	
	
}
