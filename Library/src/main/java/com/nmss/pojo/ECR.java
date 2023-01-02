package com.nmss.pojo;

public class ECR {
	private String authenticationScheme;
	private String authorization;

	public String getAuthenticationScheme() {
		return authenticationScheme;
	}

	public void setAuthenticationScheme(String authenticationScheme) {
		this.authenticationScheme = authenticationScheme;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	@Override
	public String toString() {
		return "ECR [authenticationScheme=" + authenticationScheme + ", authorization=" + authorization + "]";
	}

}
