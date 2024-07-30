package com.skyapi.weatherforecast.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.Location;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationApiController.class)
class LocationApiControllerTests {
	private static final String END_POINT_PATH = "/v1/locations";
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper mapper;
	@MockBean LocationService service;
	
	@Test
	public void testAddShouldReturn400BadRequest() throws Exception {
		Location location = new Location();
		
		String bodyContent = mapper.writeValueAsString(location);
		
		mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
		.andExpect(status().isBadRequest()).andDo(print());
		
	}
	
	@Test
	public void testAddShouldReturn200Created() throws Exception {
		Location location = new Location("NYC_USA", "New York City", "New York", "United State", "US", true, false);
		
		Mockito.when(service.add(location)).thenReturn(location);
		
		String bodyContent = mapper.writeValueAsString(location);
		
		mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
		.andExpect(status().isCreated())
		.andExpect(content().contentType("application/json"))
		.andDo(print());
	}


	@Test
	public void testListShouldReturn204NoContent() throws Exception {
		Mockito.when(service.list()).thenReturn(Collections.emptyList());

		mockMvc.perform(get(END_POINT_PATH))
				.andExpect(status().isNoContent())
				.andDo(print());

	}
	@Test
	public void testListShouldReturn200OK() throws Exception {
		Location location1 = new Location("NYC_USA", "New York City", "New York", "United State", "US", true, false);
		Location location2 = new Location("LACA_USA", "Los Angeles", "California", "United State", "US", true, false);

		Mockito.when(service.list()).thenReturn(List.of(location1, location2));

		mockMvc.perform(get(END_POINT_PATH))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$[0].code", is("NYC_USA")))
				.andExpect(jsonPath("$[0].city_name", is("NYC_USA")))
				.andDo(print());
	}

	@Test
	public void testGetShouldReturn405MethodNotAllowed() throws Exception {
		String requestURI = END_POINT_PATH + "/ABCD";

		mockMvc.perform(post(requestURI))
				.andExpect(status().isMethodNotAllowed())
				.andDo(print());
	}

	@Test
	public void testShouldReturn404NotFound() throws Exception {
		String requestURI = END_POINT_PATH + "/ABCD";

		mockMvc.perform(get(requestURI))
				.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void testShouldReturn200OK() throws Exception {
		String code = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + code;

		Location location1 = new Location("NYC_USA", "New York City", "New York", "United State", "US", true, false);
		Mockito.when(service.get(code)).thenReturn(location1);
		mockMvc.perform(get(requestURI))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {
		Location location1 = new Location("HEHHE", "New York City", "New York", "United State", "US", true, false);


		Mockito.when(service.update(location1)).thenThrow(new LocationNotFoundException("No location found"));
		String bodyContent = mapper.writeValueAsString(location1);

		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testUpdateShouldReturn400BadRequest() throws Exception {
		Location location = new Location();

		Mockito.when(service.update(location)).thenReturn(location);
		String bodyContent = mapper.writeValueAsString(location);

		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testUpdateShouldReturn200OK() throws Exception {
		Location location = new Location("NYC_USA", "New York City", "New York", "United State", "US", true, false);

		Mockito.when(service.update(location)).thenReturn(location);
		String bodyContent = mapper.writeValueAsString(location);

		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testDeleteShouldReturn404NotFound() throws Exception {
		String code = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + code;

		Mockito.doThrow(LocationNotFoundException.class).when(service).delete(code);

		mockMvc.perform(delete(requestURI))
				.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void testDeleteShouldReturn204NoContent() throws Exception {
		String code = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + code;
		Mockito.doNothing().when(service).delete(code);
		mockMvc.perform(delete(requestURI))
				.andExpect(status().isNoContent())
				.andDo(print());
	}

	@Test
	public void testValidateRequestBodyLocationCode() throws Exception {
		Location location = new Location();
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);

		String bodyContent = mapper.writeValueAsString(location);

		mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.errors[0]", is("Location code cannot be null")))
				.andDo(print());
	}

	@Test
	public void testValidateRequestBodyLocationLength() throws Exception {
		Location location = new Location();
		location.setCode("");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);

		String bodyContent = mapper.writeValueAsString(location);

		mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.errors[0]", is("Location code must have 3-128 characters")))
				.andDo(print());
	}

	@Test
	public void testValidateRequestBodyAllFieldsInvalid() throws Exception {
		Location location = new Location();
		location.setRegionName("");

		String bodyContent = mapper.writeValueAsString(location);

		MvcResult mvcResult = mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json"))
				.andDo(print())
				.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString();

		assertThat(responseBody).contains("Location code must not be null");
		assertThat(responseBody).contains("City name must not be null");
		assertThat(responseBody).contains("Region name must have 3-128 characters");
		assertThat(responseBody).contains("Country name must not be null");
		assertThat(responseBody).contains("Country code must not be null");
	}
}

