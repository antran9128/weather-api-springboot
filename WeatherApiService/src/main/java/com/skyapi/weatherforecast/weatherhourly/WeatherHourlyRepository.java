package com.skyapi.weatherforecast.weatherhourly;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforecast.common.WeatherHourly;
import com.skyapi.weatherforecast.common.WeatherHourlyId;

public interface WeatherHourlyRepository extends CrudRepository<WeatherHourly, WeatherHourlyId> {
	@Query("""
			select h from WeatherHourly h where
			h.id.location.code = ?1 and h.id.hourOfDay = ?2 and h.id.location.trashed = false
			""")
	public List<WeatherHourly> findByLocationCode(String locationCode, int currentHour);
	
}
