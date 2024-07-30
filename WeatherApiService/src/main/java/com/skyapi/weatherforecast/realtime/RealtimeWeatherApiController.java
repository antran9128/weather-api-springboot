package com.skyapi.weatherforecast.realtime;

import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeolocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherApiController {
	private GeolocationService geolocationService;
	private RealtimeWeatherService realtimeWeatherService;
	private ModelMapper modelMapper;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherApiController.class);
	
	
	public RealtimeWeatherApiController(GeolocationService geolocationService,
			RealtimeWeatherService realtimeWeatherService, ModelMapper modelMapper) {
		super();
		this.geolocationService = geolocationService;
		this.realtimeWeatherService = realtimeWeatherService;
		this.modelMapper = modelMapper;
	}



	@GetMapping
	public ResponseEntity<?> getRealtimeWeatherByIpAddress(HttpServletRequest request){
		String ipAddress = CommonUtility.getIpAddress(request);
		
		try {
			Location location = geolocationService.getLocation(ipAddress);
			RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(location);
			RealtimeWeatherDTO realtimeWeatherDTO = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
			return ResponseEntity.ok(realtimeWeatherDTO);
		} catch (GeolocationException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.badRequest().build();
			
		} catch (LocationNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/{code}")
	public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable String code){
		
		try {		
			RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(code);
			return ResponseEntity.ok(entity2DTO(realtimeWeather));
		} catch (LocationNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@PutMapping("/{code}")
	public ResponseEntity<?> updateRealtimeWeather(@PathVariable String code, @RequestBody @Valid RealtimeWeather realtimeWeather){
		//realtimeWeather.setLocationCode(code);
		try {
			RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(realtimeWeather, code);
			return ResponseEntity.ok(entity2DTO(updatedRealtimeWeather));
		} catch (LocationNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.notFound().build();
		
		}
		
	}
	
	private RealtimeWeatherDTO entity2DTO(RealtimeWeather realtimeWeather) {
		return modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);			
		
	}
	
}
