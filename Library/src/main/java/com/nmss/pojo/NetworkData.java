package com.nmss.pojo;

public class NetworkData {
	private String iteamNumber;
	private String authenticationScheme;
	private String authenticate;
	private String authorization;
	private String confidentialityKey;
	private String integrityKey;
	private String impi;
	private String tid;
	private boolean result;
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getImpi() {
		return impi;
	}
	public void setImpi(String impi) {
		this.impi = impi;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getIteamNumber() {
		return iteamNumber;
	}
	public void setIteamNumber(String iteamNumber) {
		this.iteamNumber = iteamNumber;
	}
	public String getAuthenticationScheme() {
		return authenticationScheme;
	}
	public void setAuthenticationScheme(String authenticationScheme) {
		this.authenticationScheme = authenticationScheme;
	}
	public String getAuthenticate() {
		return authenticate;
	}
	public void setAuthenticate(String authenticate) {
		this.authenticate = authenticate;
	}
	public String getAuthorization() {
		return authorization;
	}
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
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
		return "NetworkData [iteamNumber=" + iteamNumber + ", authenticationScheme=" + authenticationScheme
				+ ", authenticate=" + authenticate + ", authorization=" + authorization + ", confidentialityKey="
				+ confidentialityKey + ", integrityKey=" + integrityKey + ", impi=" + impi + ", tid=" + tid
				+ ", result=" + result + "]";
	}
}
