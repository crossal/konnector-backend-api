package com.konnector.backendapi.http;

public class ErrorResponse {

	private String error;
	private Integer code;

	private ErrorResponse(Builder builder) {
		this.error = builder.error;
		this.code = builder.code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public static class Builder {
		private String error;
		private Integer code;

		public Builder withError(String error) {
			this.error = error;
			return this;
		}

		public Builder withCode(Integer code) {
			this.code = code;
			return this;
		}

		public ErrorResponse build() {
			return new ErrorResponse(this);
		}
	}
}
