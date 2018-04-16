package com.nmss.pojo;

public class MAR {
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
		return "MAR [authenticationScheme=" + authenticationScheme + ", authorization=" + authorization + "]";
	}

}
