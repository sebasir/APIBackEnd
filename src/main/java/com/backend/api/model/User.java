package com.backend.api.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "usuarios")
public class User {

	@Id
	private ObjectId id;

	@Field("imc_number")
	private Long imcNumber;
	
	private String estado;

	private String rol;

	private String email;

	private String nombres;

	private ObjectId ubicacion;

	@JsonIgnore
	@JsonProperty(value = "password")
	private String password;

	@Field("fecha_nacimiento")
	private Long fechaNacimiento;

	@Field("fecha_creacion")
	private Long fechaCreacion;

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

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getUbicacion() {
		return ubicacion != null ? ubicacion.toHexString() : null;
	}

	public void setUbicacion(ObjectId ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Long fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Long getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Long fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}