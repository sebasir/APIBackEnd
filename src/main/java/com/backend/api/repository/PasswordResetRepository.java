package com.backend.api.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.backend.api.model.PasswordReset;

public interface PasswordResetRepository extends MongoRepository<PasswordReset, String> {
	@Query("{ 'usuario' : ?0, 'estado' : ?1 }")
	PasswordReset findByUserState(ObjectId usuario, String estado);

	@Query("{ 'password' : ?0, 'estado' : ?1 }")
	PasswordReset findByPasswordState(String password, String estado);
}