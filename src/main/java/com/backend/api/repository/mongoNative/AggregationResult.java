package com.backend.api.repository.mongoNative;

import org.bson.types.ObjectId;

public class AggregationResult {
	private ObjectId id;
	private Integer max;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

}
