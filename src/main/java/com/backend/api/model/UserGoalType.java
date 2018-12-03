package com.backend.api.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "usuariotipometa")
public class UserGoalType {

	@Id
	private ObjectId id;

	@Field(value = "tipometa")
	private String tipoMeta;

	private ObjectId usuario;

	private Integer periodo;

	private Integer total;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getTipoMeta() {
		return tipoMeta;
	}

	public void setTipoMeta(String tipoMeta) {
		this.tipoMeta = tipoMeta;
	}

	public ObjectId getUsuario() {
		return usuario;
	}

	public void setUsuario(ObjectId usuario) {
		this.usuario = usuario;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof UserGoalType) {
			UserGoalType other = (UserGoalType) o;
			return other.periodo.equals(this.periodo) && other.tipoMeta.equals(this.tipoMeta)
					&& other.usuario.equals(this.usuario);
		}
		return false;
	}

}
