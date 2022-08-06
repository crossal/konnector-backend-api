package com.konnector.backendapi.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.konnector.backendapi.connection.ConnectionDTO;
import com.konnector.backendapi.http.Views;

import java.util.Objects;

public class UserDTO {

	@JsonView(Views.Public.class)
	private Long id;
	@JsonView(Views.Private.class)
	private String email;
	@JsonView(Views.Public.class)
	private String username;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@JsonView(Views.Private.class)
	private String password;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@JsonView(Views.Private.class)
	private String oldPassword;
	@JsonView(Views.Public.class)
	private String firstName;
	@JsonView(Views.Public.class)
	private String lastName;
	@JsonProperty(value = "emailVerified")
	@JsonView(Views.Private.class)
	private Boolean emailVerified;
	/**
	 * Populated if there is connection between the user and requester
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonView(Views.Public.class)
	private ConnectionDTO connection;

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

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
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

	public Boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public ConnectionDTO getConnection() {
		return connection;
	}

	public void setConnection(ConnectionDTO connection) {
		this.connection = connection;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserDTO userDTO = (UserDTO) o;
		return Objects.equals(id, userDTO.id) && Objects.equals(email, userDTO.email) && Objects.equals(username, userDTO.username) && Objects.equals(password, userDTO.password) && Objects.equals(oldPassword, userDTO.oldPassword) && Objects.equals(firstName, userDTO.firstName) && Objects.equals(lastName, userDTO.lastName) && Objects.equals(emailVerified, userDTO.emailVerified) && Objects.equals(connection, userDTO.connection);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email, username, password, oldPassword, firstName, lastName, emailVerified, connection);
	}
}
