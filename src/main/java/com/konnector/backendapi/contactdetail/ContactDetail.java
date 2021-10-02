package com.konnector.backendapi.contactdetail;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class ContactDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "name")
	private String name;

	@Column(name = "value")
	private String value;

	@Column(name = "created_on")
	@CreationTimestamp
	private LocalDateTime createdOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private LocalDateTime updatedOn;

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void validateForCreation() {
		if (id != null) {
			throw new InvalidDataException("Id should be empty.");
		}

		validateForCreationAndUpdate();
	}

	public void validateForUpdate() {
		if (id == null) {
			throw new InvalidDataException("Id cannot be empty.");
		}

		validateForCreationAndUpdate();
	}

	private void validateForCreationAndUpdate() {
		if (userId == null) {
			throw new InvalidDataException("User Id cannot be empty.");
		}
		if (name == null || name.isEmpty()) {
			throw new InvalidDataException("Name cannot be empty.");
		}
		if (value == null || value.isEmpty()) {
			throw new InvalidDataException("Value cannot be empty.");
		}
	}

	public void merge(ContactDetail contactDetail) {
		this.name = contactDetail.name;
		this.value = contactDetail.value;
	}
}
