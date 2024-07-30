package com.skyapi.weatherforecast.weatherhourly;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.WeatherHourly;
import com.skyapi.weatherforecast.common.WeatherHourlyId;



@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class HourlyRepoTests {
	@Autowired
    private WeatherHourlyRepository repo;
	
	@Test
	public void testAdd() {
		String locationCode = "NYC_USA";
		int hourOfDay = 12;
		
		Location location = new Location();
		location.setCode(locationCode);
		
		
		WeatherHourly forcast1 = new WeatherHourly().id(location, hourOfDay)
                .temperature(22)
                .precipitation(90)
                .status("Cloudy");
		
		WeatherHourly updatedForcast = repo.save(forcast1);
		
		assertThat(updatedForcast.getId().getLocation().getCode()).isEqualTo(locationCode);
		assertThat(updatedForcast.getId().getHourOfDay()).isEqualTo(hourOfDay);
		
	}
	
	@Test
	public void testDelete() {
		Location location = new Location();
		location.setCode("NYC_USA");
		
		WeatherHourlyId id = new WeatherHourlyId(10, location);
		
		repo.deleteById(id);
		
		Optional<WeatherHourly> result = repo.findById(id);
		
		assertThat(result).isNotPresent();
		
	}
	
	@Test
	public void testFindByLocationCodeFound() {
		String locationCode = "MBHM_IN";
		int currentHour = 8;
		
		List<WeatherHourly> hourlyForeCast = repo.findByLocationCode(locationCode, currentHour);
		assertThat(hourlyForeCast).isNotEmpty();
	}
	
	@Test
	public void testFindByLocationCodeNotFound() {
		String locationCode = "MBHM_IN";
		int currentHour = 22;
		
		List<WeatherHourly> hourlyForeCast = repo.findByLocationCode(locationCode, currentHour);
		assertThat(hourlyForeCast).isEmpty();
	}
}
