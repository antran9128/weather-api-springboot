package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.common.Location;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocationService {
	private LocationRepository repo;

	public LocationService(LocationRepository repo) {
		super();
		this.repo = repo;
	}

	public Location add(Location location) {
		return repo.save(location);
	}

	public List<Location> list() {
		return (List<Location>) repo.findUntrashed();
	}

	public Location get(String code){
		return repo.findByCode(code);
	}

	public Location update(Location locationInRequest) throws LocationNotFoundException {
		String code = locationInRequest.getCode();

		Location locationInDB = repo.findByCode(code);

		if(locationInDB == null){
			throw new LocationNotFoundException("No location found with the given code: " + code);
		}
		boolean trashed = locationInDB.isTrashed();
		locationInDB = locationInRequest;
		locationInDB.setTrashed(trashed);

		return repo.save(locationInDB);
	}

	public void delete(String code) throws LocationNotFoundException {
		Location location = repo.findByCode(code);
		if(location == null){
			throw new LocationNotFoundException("No location found with the given code: " + code);
		}

		repo.trashByCode(code);
	}
}
