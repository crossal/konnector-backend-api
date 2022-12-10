package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.InvalidDataException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Entity
public class User {

	private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\." +
			"[a-zA-Z0-9_+&*-]+)*@" +
			"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
			"A-Z]{2,7}$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	private static final int EMAIL_MAX_LENGTH = 255;
	private static final int MIN_PASSWORD_LENGTH = 8;
	private static final int MAX_PASSWORD_LENGTH = 50;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email")
	private String email;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "is_email_verified")
	private boolean emailVerified;

	@Column(name = "created_on")
	@CreationTimestamp
	private LocalDateTime createdOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private LocalDateTime updatedOn;

	public Long getId() {
		return id;
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

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void validatePassword(String password) {
		if (password.length() < MIN_PASSWORD_LENGTH) {
			throw new InvalidDataException("Password cannot be less than " + MIN_PASSWORD_LENGTH + " characters.");
		}
		if (password.length() > MAX_PASSWORD_LENGTH) {
			throw new InvalidDataException("Password cannot be greater than " + MAX_PASSWORD_LENGTH + " characters.");
		}
	}

	public void validateForCreation() {
		if (id != null) {
			throw new InvalidDataException("Id should be empty.");
		}
		if (password == null || password.isEmpty()) {
			throw new InvalidDataException("Password cannot be empty.");
		}
		validatePassword(password);
		if (emailVerified != false) {
			throw new InvalidDataException("Email verification flag should be false on creation.");
		}

		validateForCreationAndUpdate();
	}

	public void validateForUpdate() {
		if (id == null) {
			throw new InvalidDataException("Id cannot be empty.");
		}

		if (password != null && !password.isEmpty()) {
			validatePassword(password);
		}

		validateForCreationAndUpdate();
	}

	private void validateForCreationAndUpdate() {
		if (email == null || email.isEmpty()) {
			throw new InvalidDataException("Email cannot be empty.");
		}
		if (email.length() >= EMAIL_MAX_LENGTH) {
			throw new InvalidDataException("Email too long.");
		}
		if (!EMAIL_PATTERN.matcher(email).matches()){
			throw new InvalidDataException("Email not valid.");
		}
		if (username == null || username.isEmpty()) {
			throw new InvalidDataException("Username cannot be empty.");
		}
		if (firstName == null || firstName.isEmpty()) {
			throw new InvalidDataException("First name cannot be empty.");
		}
		if (lastName == null || lastName.isEmpty()) {
			throw new InvalidDataException("Last name cannot be empty.");
		}
	}

	public void merge(User user) {
		this.firstName = user.firstName;
		this.lastName = user.lastName;
	}
}
