package com.nmss.pojo;

public class ZX_DIGEST {
	private String IMPU;
	private String digestRealm;
	private String digestAlgorithm;
	private String digestQOP;
	private String digestHA1;
	private String digestCNonce;
	private String digestNonce;
	private String digestNonceCount;
	private String digestResponse;
	private String digestUsername;
	private String framedIpAddress;
	private String ericssonDigestHA2;

	public String getIMPU() {
		return IMPU;
	}

	public void setIMPU(String iMPU) {
		IMPU = iMPU;
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

	public String getDigestCNonce() {
		return digestCNonce;
	}

	public void setDigestCNonce(String digestCNonce) {
		this.digestCNonce = digestCNonce;
	}

	public String getDigestNonce() {
		return digestNonce;
	}

	public void setDigestNonce(String digestNonce) {
		this.digestNonce = digestNonce;
	}

	public String getDigestNonceCount() {
		return digestNonceCount;
	}

	public void setDigestNonceCount(String digestNonceCount) {
		this.digestNonceCount = digestNonceCount;
	}

	public String getDigestResponse() {
		return digestResponse;
	}

	public void setDigestResponse(String digestResponse) {
		this.digestResponse = digestResponse;
	}

	public String getDigestUsername() {
		return digestUsername;
	}

	public void setDigestUsername(String digestUsername) {
		this.digestUsername = digestUsername;
	}

	public String getFramedIpAddress() {
		return framedIpAddress;
	}

	public void setFramedIpAddress(String framedIpAddress) {
		this.framedIpAddress = framedIpAddress;
	}

	public String getEricssonDigestHA2() {
		return ericssonDigestHA2;
	}

	public void setEricssonDigestHA2(String ericssonDigestHA2) {
		this.ericssonDigestHA2 = ericssonDigestHA2;
	}

	@Override
	public String toString() {
		return "ZX_DIGEST [IMPU=" + IMPU + ", digestRealm=" + digestRealm + ", digestAlgorithm=" + digestAlgorithm
				+ ", digestQOP=" + digestQOP + ", digestHA1=" + digestHA1 + ", digestCNonce=" + digestCNonce
				+ ", digestNonce=" + digestNonce + ", digestNonceCount=" + digestNonceCount + ", digestResponse="
				+ digestResponse + ", digestUsername=" + digestUsername + ", framedIpAddress=" + framedIpAddress
				+ ", ericssonDigestHA2=" + ericssonDigestHA2 + "]";
	}

}
