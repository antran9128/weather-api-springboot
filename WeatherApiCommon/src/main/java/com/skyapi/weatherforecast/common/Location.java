package com.skyapi.weatherforecast.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "locations")
public class Location {
	@Column(length = 12, nullable = false, unique = true)
	@Id
	@NotNull(message = "Location code must not be null")
	@Length(min = 3, max = 12, message = "Location code must have 3-128 characters")
	private String code;
	
	@Column(length = 128, nullable = false)
	@NotNull(message = "City name must not be null")
	@Length(min = 3, max = 128, message = "City name must have 3-128 characters")
	@JsonProperty("city_name")
	private String cityName;
	
	@Column(length = 128)
	@NotNull(message = "Region name must not be null")
	@Length(min = 3, max = 128, message = "Region name must have 3-128 characters")
	@JsonProperty("region_name")
	private String regionName;
	
	@Column(length = 64, nullable = false)
	@NotNull(message = "Country name must not be null")
	@Length(min = 3, max = 64, message = "Country name must have 3-64 characters")
	@JsonProperty("country_name")
	private String countryName;
	
	@Column(length = 2, nullable = false)
	@NotNull(message = "Country code must not be null")
	@Length(min = 2, max = 2, message = "City name must have 2 characters")
	@JsonProperty("country_code")
	private String countryCode;
	
	private boolean enabled;
	
	@OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	@JsonIgnore
	private RealtimeWeather realtimeWeather;
	
	@OneToMany(mappedBy = "id.location", cascade = CascadeType.ALL)
	private List<WeatherHourly> listHourlyWeather = new ArrayList<>();

	@JsonIgnore
	private boolean trashed;
	
	public Location() {
		super();
	}
		
	
	public Location(String code, String cityName, String regionName, String countryName, String countryCode,
			boolean enabled, boolean trashed) {
		super();
		this.code = code;
		this.cityName = cityName;
		this.regionName = regionName;
		this.countryName = countryName;
		this.countryCode = countryCode;
		this.enabled = enabled;
		this.trashed = trashed;
	}

	public Location(String cityName, String regionName, String countryName, String countryCode) {
		this.cityName = cityName;
		this.regionName = regionName;
		this.countryName = countryName;
		this.countryCode = countryCode;
	}

	public RealtimeWeather getRealtimeWeather() {
		return realtimeWeather;
	}

	public void setRealtimeWeather(RealtimeWeather realtimeWeather) {
		this.realtimeWeather = realtimeWeather;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isTrashed() {
		return trashed;
	}
	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}
	
	
	public List<WeatherHourly> getListHourlyWeather() {
		return listHourlyWeather;
	}


	public void setListHourlyWeather(List<WeatherHourly> listHourlyWeather) {
		this.listHourlyWeather = listHourlyWeather;
	}


	@Override
	public int hashCode() {
		return Objects.hash(code);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(code, other.code);
	}


	@Override
	public String toString() {
		return cityName + ", " + (regionName != null ? regionName : "")  + ", " + countryName;
	}


	
	
	
}
