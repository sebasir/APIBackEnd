package com.backend.api.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "meta")
public class Goal {

	@Id
	private ObjectId id;

	@Field(value = "usuariotipometa")
	private ObjectId usuarioTipoMeta;
	
	private String tipometa;

	private String nombre;

	private Long fecha;

	private Integer avance;

	private String descripcion;

	public String getId() {
		return id.toHexString();
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getUsuarioTipoMeta() {
		return usuarioTipoMeta;
	}

	public void setUsuarioTipoMeta(ObjectId usuarioTipoMeta) {
		this.usuarioTipoMeta = usuarioTipoMeta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getFecha() {
		return fecha;
	}

	public void setFecha(Long fecha) {
		this.fecha = fecha;
	}

	public Integer getAvance() {
		return avance;
	}

	public void setAvance(Integer avance) {
		this.avance = avance;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipometa() {
		return tipometa;
	}

	public void setTipometa(String tipometa) {
		this.tipometa = tipometa;
	}

	
}
