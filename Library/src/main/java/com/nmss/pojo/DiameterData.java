package com.nmss.pojo;

public class DiameterData {
	private String tid;
	private long dcNetworkTime;
	private String iteamNumber;
	private String authenticationScheme;
	private String authenticate;
	private String authorization;
	private String confidentialityKey;
	private String integrityKey;
	private String impi;
	private int result;
	private Boolean isDigest;
	private String digestRealm; /* 104 */
	private String digestAlgo;/* 111 */
	private String digestQOP;/* 110 */
	private String digestHA1;/* 121 */

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

	public String getImpi() {
		return impi;
	}

	public void setImpi(String impi) {
		this.impi = impi;
	}

	public int isResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public long getDcNetworkTime() {
		return dcNetworkTime;
	}

	public void setDcNetworkTime(long dcNetworkTime) {
		this.dcNetworkTime = dcNetworkTime;
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

	public String getDigestAlgo() {
		return digestAlgo;
	}

	public void setDigestAlgo(String digestAlgo) {
		this.digestAlgo = digestAlgo;
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

	public int getResult() {
		return result;
	}

	@Override
	public String toString() {
		return "DiameterData [tid=" + tid + ", dcNetworkTime=" + dcNetworkTime + ", iteamNumber=" + iteamNumber
				+ ", authenticationScheme=" + authenticationScheme + ", authenticate=" + authenticate
				+ ", authorization=" + authorization + ", confidentialityKey=" + confidentialityKey + ", integrityKey="
				+ integrityKey + ", impi=" + impi + ", result=" + result + ", isDigest=" + isDigest + ", digestRealm="
				+ digestRealm + ", digestAlgo=" + digestAlgo + ", digestQOP=" + digestQOP + ", digestHA1=" + digestHA1
				+ "]";
	}

}
