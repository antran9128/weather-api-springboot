package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.common.Location;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/locations")
public class LocationApiController {
	private LocationService service;

	public LocationApiController(LocationService service) {
		super();
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<Location> addLocation(@RequestBody @Valid Location location){
		Location addedLocation = service.add(location);
		URI uri = URI.create("/v1/locations/" + addedLocation.getCode());
		return ResponseEntity.created(uri).body(addedLocation);
	}
	
	@GetMapping
	public ResponseEntity<?> listLocations(){
		List<Location> locations = service.list();
		
		if(locations.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(locations);
	}


	@GetMapping("/{code}")
	public ResponseEntity<?> getLocation(@PathVariable("code") String code){
		Location location = service.get(code);

		if(location == null){
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(location);
	}

	@PutMapping
	public ResponseEntity<Location> updateLocation(@RequestBody @Valid Location locationInRequest){
		try {
			Location updateLocation = service.update(locationInRequest);
			return ResponseEntity.ok(updateLocation);
		} catch (LocationNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteLocation(@PathVariable("code") String code){
		try {
			service.delete(code);
			return ResponseEntity.noContent().build();
		} catch (LocationNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
