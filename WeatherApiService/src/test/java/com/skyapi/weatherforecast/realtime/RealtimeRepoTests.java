package com.skyapi.weatherforecast.realtime;

import com.skyapi.weatherforecast.common.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RealtimeRepoTests {
    @Autowired
    private RealtimeWeatherRepository repo;

    @Test
    public void testUpdate(){
        String locationCode = "NYC_USA";

        RealtimeWeather realtimeWeather = repo.findById(locationCode).get();

        realtimeWeather.setTemperature(-2);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setPrecipitation(42);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(12);
        realtimeWeather.setLastUpdated(new Date());

        RealtimeWeather updatedLocation = repo.save(realtimeWeather);

        assertThat(updatedLocation.getHumidity()).isEqualTo(32);
    }

    @Test
    public void testFindByCountryCodeAndCityNotFound() {
    	String countryCode = "JP";
    	String cityName = "Tokyo";
    	
    	RealtimeWeather realtimeWeather =  repo.findByCountryCodeAndCity(countryCode, cityName);
    	assertThat(realtimeWeather).isNull();    	
    }
    
    @Test
    public void testFindByCountryCodeAndCityFound() {
    	String countryCode = "US";
    	String cityName = "New York City";
    	
    	RealtimeWeather realtimeWeather =  repo.findByCountryCodeAndCity(countryCode, cityName);
    	assertThat(realtimeWeather).isNotNull();    	
    }
    
    @Test 
    public void testFindByLocationNotFound() {
    	String locationCode = "Hanoi";
    	RealtimeWeather realtimeWeather =  repo.findByLocationCode(locationCode);
    	assertThat(realtimeWeather).isNull();  
    }
    
    @Test 
    public void testFindByTrashedLocationNotFound() {
    	String locationCode = "US_NYC";
    	RealtimeWeather realtimeWeather =  repo.findByLocationCode(locationCode);
    	assertThat(realtimeWeather).isNull();  
    }
    
    @Test 
    public void testFindByLocationFound() {
    	String locationCode = "NYC_USA";
    	RealtimeWeather realtimeWeather =  repo.findByLocationCode(locationCode);
    	assertThat(realtimeWeather).isNotNull(); 
    }
}
