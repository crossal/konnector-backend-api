package com.konnector.backendapi.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class UserDTO {

	private Long id;
	private String email;
	private String username;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private String firstName;
	private String lastName;
	@JsonProperty(value = "isEmailVerified", access = JsonProperty.Access.READ_ONLY)
	private Boolean isEmailVerified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("isEmailVerified")
	public Boolean isEmailVerified() {
		return isEmailVerified;
	}

	public void setEmailVerified(Boolean isEmailVerified) {
		isEmailVerified = isEmailVerified;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserDTO userDTO = (UserDTO) o;
		return Objects.equals(id, userDTO.id) &&
				Objects.equals(email, userDTO.email) &&
				Objects.equals(username, userDTO.username) &&
				Objects.equals(password, userDTO.password) &&
				Objects.equals(firstName, userDTO.firstName) &&
				Objects.equals(lastName, userDTO.lastName) &&
				Objects.equals(isEmailVerified, userDTO.isEmailVerified);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email, username, password, firstName, lastName, isEmailVerified);
	}
}
