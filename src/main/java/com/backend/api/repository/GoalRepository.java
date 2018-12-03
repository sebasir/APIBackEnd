package com.backend.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.backend.api.model.Goal;
import com.backend.api.model.GoalType;

public interface GoalRepository  extends MongoRepository<Goal, String>{
	@Query("{}")
	List<GoalType> findAllGoalTypes();

}
