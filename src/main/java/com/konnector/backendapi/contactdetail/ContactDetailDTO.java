package com.konnector.backendapi.contactdetail;

import java.util.Objects;

public class ContactDetailDTO {

	private Long id;
	private Long userId;
	private String type;
	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ContactDetailDTO that = (ContactDetailDTO) o;
		return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(type, that.type) && Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userId, type, value);
	}
}
