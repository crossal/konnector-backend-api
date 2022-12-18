package com.konnector.backendapi.connection;

import com.konnector.backendapi.exceptions.InvalidDataException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class Connection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "requester_id")
	private Long requesterId;

	@Column(name = "requestee_id")
	private Long requesteeId;

	@Column(name = "status")
	private ConnectionStatus status;

	@Column(name = "created_on")
	@CreationTimestamp
	private LocalDateTime createdOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private LocalDateTime updatedOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}

	public Long getRequesteeId() {
		return requesteeId;
	}

	public void setRequesteeId(Long requesteeId) {
		this.requesteeId = requesteeId;
	}

	public ConnectionStatus getStatus() {
		return status;
	}

	public void setStatus(ConnectionStatus status) {
		this.status = status;
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
		if (requesterId == null) {
			throw new InvalidDataException("Requester Id cannot be null.");
		}
		if (requesteeId == null) {
			throw new InvalidDataException("Requestee Id cannot be null.");
		}
		if (status == null) {
			throw new InvalidDataException("Status cannot be null.");
		}
	}

	public void merge(Connection connection) {
		this.status = connection.status;
	}
}
