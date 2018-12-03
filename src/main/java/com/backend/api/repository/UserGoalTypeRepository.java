package com.backend.api.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.backend.api.model.UserGoalType;

public interface UserGoalTypeRepository  extends MongoRepository<UserGoalType, String>{
	@Query(" { 'usuario': ?0, 'periodo': ?1 } ")
	List<UserGoalType> findUserGoalTypesByUserPeriod(ObjectId usuario, Integer periodo);
}
