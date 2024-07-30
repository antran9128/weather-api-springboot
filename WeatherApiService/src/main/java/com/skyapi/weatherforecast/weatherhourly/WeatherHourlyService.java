package com.skyapi.weatherforecast.weatherhourly;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.WeatherHourly;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

@Service
public class WeatherHourlyService {
	private WeatherHourlyRepository weatherHourlyRepository;
	private LocationRepository locationRepository;

	public WeatherHourlyService(WeatherHourlyRepository weatherHourlyRepository,
			LocationRepository locationRepository) {
		super();
		this.weatherHourlyRepository = weatherHourlyRepository;
		this.locationRepository = locationRepository;
	}

	public List<WeatherHourly> getByLocation(Location location, int currentHour) throws LocationNotFoundException {
		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();

		Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

		if (locationInDB == null) {
			throw new LocationNotFoundException("No location found with the given country code and city name");
		}

		return weatherHourlyRepository.findByLocationCode(locationInDB.getCode(), currentHour);
	}

	public List<WeatherHourly> getByLocationCode(String locationCode, int currentHour) throws LocationNotFoundException {
		Location locationInDB = locationRepository.findByCode(locationCode);

		if (locationInDB == null) {
			throw new LocationNotFoundException("No location found with the given country code and city name");
		}

		return weatherHourlyRepository.findByLocationCode(locationInDB.getCode(), currentHour);
	}
	
	public List<WeatherHourly> updateByLocationCode(String locationCode, List<WeatherHourly> hourlyForcastInRequest){
		return Collections.EMPTY_LIST;
	}

}
