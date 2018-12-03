package com.backend.api.repository.mongoNative;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class MongoNativeQuery {

	@Autowired
	private MongoTemplate mongoTemplate;

	public Integer findMaxPeriodByUser(ObjectId user) {
		MatchOperation matchStage = Aggregation.match(new Criteria("usuario").is(user));
		GroupOperation groupStage = Aggregation.group("usuario").max("periodo").as("max");

		Aggregation aggregation = Aggregation.newAggregation(matchStage, groupStage);

		AggregationResults<AggregationResult> output = mongoTemplate.aggregate(aggregation, "usuariotipometa",
				AggregationResult.class);

		if (output == null || output.getUniqueMappedResult() == null)
			return null;
		return output.getUniqueMappedResult().getMax();
	}

}
