package com.nmss.pojo;

public class MAA {
	private String authenticationScheme;
	private String authorization;
	private Boolean isDigest;
	private String digestRealm;
	private String digestAlgorithm;
	private String digestQOP;
	private String digestHA1;

	private String iteamNumber;
	private String authenticate;
	private String confidentialityKey;
	private String integrityKey;

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

	public Boolean getIsDigest() {
		return isDigest;
	}

	public void setIsDigest(Boolean isDigest) {
		this.isDigest = isDigest;
	}

	public String getDigestRealm() {
		return digestRealm;
	}

	public void setDigestRealm(String digestRealm) {
		this.digestRealm = digestRealm;
	}

	public String getDigestAlgorithm() {
		return digestAlgorithm;
	}

	public void setDigestAlgorithm(String digestAlgorithm) {
		this.digestAlgorithm = digestAlgorithm;
	}

	public String getDigestQOP() {
		return digestQOP;
	}

	public void setDigestQOP(String digestQOP) {
		this.digestQOP = digestQOP;
	}

	public String getDigestHA1() {
		return digestHA1;
	}

	public void setDigestHA1(String digestHA1) {
		this.digestHA1 = digestHA1;
	}

	public String getIteamNumber() {
		return iteamNumber;
	}

	public void setIteamNumber(String iteamNumber) {
		this.iteamNumber = iteamNumber;
	}

	public String getAuthenticate() {
		return authenticate;
	}

	public void setAuthenticate(String authenticate) {
		this.authenticate = authenticate;
	}

	public String getConfidentialityKey() {
		return confidentialityKey;
	}

	public void setConfidentialityKey(String confidentialityKey) {
		this.confidentialityKey = confidentialityKey;
	}

	public String getIntegrityKey() {
		return integrityKey;
	}

	public void setIntegrityKey(String integrityKey) {
		this.integrityKey = integrityKey;
	}

	@Override
	public String toString() {
		return "MAA [authenticationScheme=" + authenticationScheme + ", authorization=" + authorization + ", isDigest="
				+ isDigest + ", digestRealm=" + digestRealm + ", digestAlgorithm=" + digestAlgorithm + ", digestQOP="
				+ digestQOP + ", digestHA1=" + digestHA1 + ", iteamNumber=" + iteamNumber + ", authenticate="
				+ authenticate + ", confidentialityKey=" + confidentialityKey + ", integrityKey=" + integrityKey + "]";
	}

}
