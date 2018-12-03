package com.backend.api.dto;

public class UserGoalTypeDTO {
	private String nombre;
	private String label;
	private int cantidad;

	public UserGoalTypeDTO(String nombre, String label, int cantidad) {
		super();
		this.nombre = nombre;
		this.label = label;
		this.cantidad = cantidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
}
