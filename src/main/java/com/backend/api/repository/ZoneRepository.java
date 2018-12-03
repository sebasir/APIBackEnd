package com.backend.api.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.backend.api.model.Zone;

public interface ZoneRepository extends MongoRepository<Zone, String> {
	@Query("{ 'codigo' : ?0 }")
	Zone findZoneByCode(Long code);

	@Query("{ '_id' : ?0 }")
	Zone findZoneById(String id);

	@Query("{ 'nivel' : ?0, 'padre' : ?1}")
	List<Zone> findZonesByLevelParent(String zoneLevel, ObjectId parentZone);
}