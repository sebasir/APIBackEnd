package com.backend.api.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "empresarios")
public class Promoter {

	@Id
	private ObjectId id;

	@Field("imc_number")
	private Long imcNumber;

	private String estado;

	public String getId() {
		return id.toHexString();
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Long getImcNumber() {
		return imcNumber;
	}

	public void setImcNumber(Long imcNumber) {
		this.imcNumber = imcNumber;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}