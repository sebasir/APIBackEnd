package com.backend.api.dto;

public class GoalDTO {

	private String nombre;
	private Integer avance;
	private String descripcion;
	private String fecha;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

}
