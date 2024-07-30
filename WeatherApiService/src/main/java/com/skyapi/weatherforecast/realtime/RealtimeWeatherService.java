package com.skyapi.weatherforecast.realtime;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;
import com.skyapi.weatherforecast.location.LocationService;
@Service
public class RealtimeWeatherService {
	private RealtimeWeatherRepository realtimeWeatherRepo;
	private LocationRepository locationRepo;
	public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepo, LocationRepository locationRepo) {
		super();
		this.realtimeWeatherRepo = realtimeWeatherRepo;
		this.locationRepo = locationRepo;
	}
	
	public RealtimeWeather getByLocation(Location location) throws LocationNotFoundException {
		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();
		
		RealtimeWeather realtimeWeather =  realtimeWeatherRepo.findByCountryCodeAndCity(countryCode, cityName);
    	
		if(realtimeWeather == null) {
			throw new LocationNotFoundException("No location found with the given country code and city name");			
		}
		
		return realtimeWeather;
	}
	
	public RealtimeWeather getByLocationCode(String code) throws LocationNotFoundException {
		
		RealtimeWeather realtimeWeather =  realtimeWeatherRepo.findByLocationCode(code);
    	
		if(realtimeWeather == null) {
			throw new LocationNotFoundException("No location found with the given location code");			
		}
		
		return realtimeWeather;
	}
	
	public RealtimeWeather update(RealtimeWeather realtimeWeather, String locationCode) throws LocationNotFoundException {
		Location location = locationRepo.findByCode(locationCode);
		
		if(location == null) {
			throw new LocationNotFoundException("No location found with the given location code");			
		}
		
		realtimeWeather.setLocation(location);
		realtimeWeather.setLastUpdated(new Date());
		
		if(location.getRealtimeWeather() == null) {
			location.setRealtimeWeather(realtimeWeather);
			Location updated = locationRepo.save(location);
			return updated.getRealtimeWeather();
		}
		
		return realtimeWeatherRepo.save(realtimeWeather);
	}
}
