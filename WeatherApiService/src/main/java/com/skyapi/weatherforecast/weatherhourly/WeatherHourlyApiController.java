package com.skyapi.weatherforecast.weatherhourly;

import java.util.List;

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
import com.skyapi.weatherforecast.common.WeatherHourly;
import com.skyapi.weatherforecast.location.LocationNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/hourly")
public class WeatherHourlyApiController {
	private WeatherHourlyService weatherHourlyService;
	private GeolocationService geolocationService;
	public WeatherHourlyApiController(WeatherHourlyService weatherHourlyService,
			GeolocationService geolocationService) {
		super();
		this.weatherHourlyService = weatherHourlyService;
		this.geolocationService = geolocationService;
	}
	
	@GetMapping
	public ResponseEntity<?> listHourlyForecastByIpAddress(HttpServletRequest request){
		String ipAddress = CommonUtility.getIpAddress(request);
		
		int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
		
		try {
			Location locationFromIp = geolocationService.getLocation(ipAddress);
			List<WeatherHourly> hourlyForecast = weatherHourlyService.getByLocation(locationFromIp, currentHour);
			
			if(hourlyForecast.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			
			return ResponseEntity.ok(hourlyForecast);
		}catch(GeolocationException ex) {
			return ResponseEntity.badRequest().build();
		}catch(LocationNotFoundException ex) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/{code}")
	public ResponseEntity<?> listHourlyForcastByLocationCode(HttpServletRequest request, @PathVariable String code){
		int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
		try {
			List<WeatherHourly> hourlyForecast = weatherHourlyService.getByLocationCode(code, currentHour);
			if(hourlyForecast.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			
			return ResponseEntity.ok(hourlyForecast);
		} catch (LocationNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
//	@PutMapping("/{code}")
//	public ResponseEntity<?> updateHourlyForcastByLocationCode(@PathVariable("code") String code,
//			@RequestBody List<WeatherHourlyDTO> listDTO){
//		
//	}
}
