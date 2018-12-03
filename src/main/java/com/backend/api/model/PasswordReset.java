package com.backend.api.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "reiniciopass")
public class PasswordReset {

	@Id
	private ObjectId id;

	private String estado;

	private ObjectId usuario;

	@JsonIgnore
	@JsonProperty(value = "password")
	private String password;

	@Field("fecha_peticion")
	private Long fechaPeticion;

	@Field("fecha_reinicio")
	private Long fechaReinicio;

	public String getId() {
		return id.toHexString();
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ObjectId getUsuario() {
		return usuario;
	}

	public void setUsuario(ObjectId usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getFechaPeticion() {
		return fechaPeticion;
	}

	public void setFechaPeticion(Long fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}

	public Long getFechaReinicio() {
		return fechaReinicio;
	}

	public void setFechaReinicio(Long fechaReinicio) {
		this.fechaReinicio = fechaReinicio;
	}
}