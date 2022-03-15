package com.konnector.backendapi.connection;

import com.fasterxml.jackson.annotation.JsonView;
import com.konnector.backendapi.http.Views;

import java.util.Objects;

@JsonView(Views.Public.class)
public class ConnectionDTO {

	private Long id;
	private Long requesterId;
	private Long requesteeId;
	private ConnectionStatus status;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ConnectionDTO that = (ConnectionDTO) o;
		return Objects.equals(id, that.id) && Objects.equals(requesterId, that.requesterId) && Objects.equals(requesteeId, that.requesteeId) && status == that.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, requesterId, requesteeId, status);
	}
}
