package com.backend.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.backend.api.model.GoalType;

public interface GoalTypeRepository  extends MongoRepository<GoalType, String>{
	@Query("{}")
	List<GoalType> findAllGoalTypes();

}
