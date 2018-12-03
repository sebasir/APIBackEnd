package com.backend.api.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

@Document(collection = "tipometa")
public class GoalType {

	@Id
	private ObjectId id;
	private String nombre;
	private List<I18N> i18n;

	public String getId() {
		return id.toHexString();
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@JsonAnyGetter
	public Map<String, String> getI18n() {
		Map<String, String> map = new HashMap<>();
		i18n.stream().forEach(e -> map.put(e.locale, e.value));
		return map;
	}

	public void setI18n(List<I18N> i18n) {
		this.i18n = i18n;
	}

	class I18N {
		String locale;
		String value;

		public I18N(String locale, String value) {
			this.locale = locale;
			this.value = value;
		}

		public String getLocale() {
			return locale;
		}

		public void setLocale(String locale) {
			this.locale = locale;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

}
