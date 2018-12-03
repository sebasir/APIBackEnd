package com.backend.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.backend.api.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	@Query("{ 'imc_number' : ?0 }")
	List<User> findUsersByIMCNumber(Long imcNumber);

	@Query("{ 'imc_number' : ?0, 'rol': ?1 }")
	User findUserByIMCNumberRol(Long imcNumber, String rol);
}