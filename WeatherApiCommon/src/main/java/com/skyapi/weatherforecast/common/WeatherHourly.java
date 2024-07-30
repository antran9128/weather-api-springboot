package com.skyapi.weatherforecast.common;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "weather_hourly")
public class WeatherHourly {
	@EmbeddedId
	private WeatherHourlyId id = new WeatherHourlyId();
	
	@Range(min = -50, max = 50, message = "Temperature must be in the range of -50 to 50 Celsius degree")
    private int temperature;
	
	@Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
    private int precipitation;
	
	@Column(length = 50)
	@NotBlank(message = "Status must not be empty")
    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
	private String status;
	

	public WeatherHourly() {
		super();
	}

	

	public WeatherHourly(WeatherHourlyId id,
			int temperature,
			int precipitation,
			String status) {
		super();
		this.id = id;
		this.temperature = temperature;
		this.precipitation = precipitation;
		this.status = status;
	}
	
		
	public WeatherHourlyId getId() {
		return id;
	}

	public void setId(WeatherHourlyId id) {
		this.id = id;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(int precipitation) {
		this.precipitation = precipitation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public WeatherHourly temperature(int temp) {
		setTemperature(temp);
		return this;
	}
	
	public WeatherHourly id(Location location, int hour) {
		this.id.setHourOfDay(hour);
		this.id.setLocation(location);
		return this;
	}
	
	public WeatherHourly precipitation(int recipitation) {
		setPrecipitation(recipitation);
		return this;
	}
	
	
	public WeatherHourly status(String status) {
		setStatus(status);
		return this;
	}
	
	public WeatherHourly location(Location location) {
		this.id.setLocation(location);
		return this;
	}
	
	public WeatherHourly hourOfDay(int hour) {
		this.id.setHourOfDay(hour);
		return this;
	}
}
