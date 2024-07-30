package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.common.WeatherHourly;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class LocationRepositoryTests {
	
	@Autowired
	private LocationRepository repo;
	
	@Test
	void testAddSuccess() {
		Location location = new Location();
		location.setCode("MBHM_IN");
		location.setCityName("Mumbai");
		location.setRegionName("Maharashtra");
		location.setCountryCode("IN");
		location.setCountryName("India");
		location.setEnabled(true);
		Location savedLocation = repo.save(location);
		assert(savedLocation != null);
		assert(savedLocation.getCode().equalsIgnoreCase("MBHM_IN"));
	}
	
	@Test
	void testListSuccess() {
		List<Location> locations = repo.findUntrashed();
		
		assertThat(locations).isNotEmpty();
	}
	
	@Test
	public void testGetNotFound(){
		String code = "ABCD";
		Location location = repo.findByCode(code);
		assertThat(location).isNull();
	}

	@Test
	public void testGetFound(){
		String code = "NYC_USA";
		Location location = repo.findByCode(code);
		assertThat(location).isNotNull();
		assertThat(location.getCode()).isEqualTo(code);
	}

	@Test
	public void testTrashSuccess(){
		String code = "NYC_USA";
		repo.trashByCode(code);
		Location location = repo.findByCode(code);
		assertThat(location).isNull();
	}

	@Test
	public void testAddRealtimeWeatherData(){
		String code = "NYC_USA";

		Location location = repo.findByCode(code);

		RealtimeWeather realtimeWeather = location.getRealtimeWeather();

		if(realtimeWeather == null){
			realtimeWeather = new RealtimeWeather();
			realtimeWeather.setLocation(location);
			location.setRealtimeWeather(realtimeWeather);
		}

		realtimeWeather.setTemperature(10);
		realtimeWeather.setHumidity(60);
		realtimeWeather.setPrecipitation(70);
		realtimeWeather.setStatus("Sunny");
		realtimeWeather.setWindSpeed(10);
		realtimeWeather.setLastUpdated(new Date());

		Location updatedLocation = repo.save(location);

		assertThat(updatedLocation.getRealtimeWeather().getLocationCode()).isEqualTo(code);
	}
	
	@Test
	public void testAddHourlyWeatherData() {
		Location location = repo.findById("MBHM_IN").get();
		List<WeatherHourly> listHourlyWeather = location.getListHourlyWeather();
		WeatherHourly forcast1 = new WeatherHourly().id(location, 8)
				                                    .temperature(20)
				                                    .precipitation(60)
				                                    .status("Cloudy");
		
		WeatherHourly forcast2 = new WeatherHourly().id(location, 9)
                .temperature(20)
                .precipitation(60)
                .status("Cloudy");
		
		listHourlyWeather.add(forcast1);
		listHourlyWeather.add(forcast2);
		
		Location updatedLocation = repo.save(location);
		
		assertThat(updatedLocation.getListHourlyWeather()).isNotEmpty();
		
	}
	
	@Test
	public void testFindByCountryCodeAndCityNotFound() {
		String countryCode = "BZ";
		String cityName = "City";
		
		Location location = repo.findByCountryCodeAndCityName(countryCode, cityName);
		assertThat(location).isNull();
	}
	
	@Test
	public void testFindByCountryCodeAndCityFound() {
		String countryCode = "IN";
		String cityName = "Mumbai";
		
		Location location = repo.findByCountryCodeAndCityName(countryCode, cityName);
		//assertThat(location).isNotNull();
		assertThat(location.getCountryCode()).isEqualTo(countryCode);
		assertThat(location.getCityName()).isEqualTo(cityName);
	}

}
