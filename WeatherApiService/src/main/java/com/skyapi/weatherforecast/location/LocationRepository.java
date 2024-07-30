package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.common.Location;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LocationRepository extends CrudRepository<Location, String> {
	
	@Query("SELECT l FROM Location l WHERE l.trashed = false")
	List<Location> findUntrashed();
	
	@Query("select l from Location l where l.trashed = false and l.code = ?1")
	Location  findByCode(String code);
	@Modifying
	@Query("update Location l set l.trashed = true where l.code = ?1")
	void trashByCode(String code);
	
	@Query("select l from Location l where l.countryCode = ?1 and l.cityName = ?2 and l.trashed = false")
	Location findByCountryCodeAndCityName(String countryCode, String cityName);
}
