package com.backend.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.backend.api.model.Promoter;

public interface PromoterRepository extends MongoRepository<Promoter, String> {
	@Query("{ 'imc_number' : ?0 }")
	Promoter findPromoterByIMCNumber(Long imcNumber);
}