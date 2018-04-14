package com.nmss.pojo;

public class DiameterData {
	private RequestType requestType;
	private String tid;
	private long dcNetworkTime;
	private String iteamNumber;
	private String authenticationScheme;
	private String authenticate;
	private String authorization;
	private String confidentialityKey;
	private String integrityKey;
	private String impi;
	private Integer result;
	private Integer experimentalResultCode;
	private Boolean isDigest;
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

	private Integer featureListId;
	private Integer featureList;
	private String IMPU;
	private String msisdn;
	private Integer dataReference;

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

	public Integer isResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Integer getExperimentalResultCode() {
		return experimentalResultCode;
	}

	public void setExperimentalResultCode(Integer experimentalResultCode) {
		this.experimentalResultCode = experimentalResultCode;
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

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public Integer getFeatureListId() {
		return featureListId;
	}

	public void setFeatureListId(Integer featureListId) {
		this.featureListId = featureListId;
	}

	public Integer getFeatureList() {
		return featureList;
	}

	public void setFeatureList(Integer featureList) {
		this.featureList = featureList;
	}

	public String getIMPU() {
		return IMPU;
	}

	public void setIMPU(String iMPU) {
		IMPU = iMPU;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Integer getDataReference() {
		return dataReference;
	}

	public void setDataReference(Integer dataReference) {
		this.dataReference = dataReference;
	}

	public String getDigestAlgorithm() {
		return digestAlgorithm;
	}

	public void setDigestAlgorithm(String digestAlgorithm) {
		this.digestAlgorithm = digestAlgorithm;
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
		return "DiameterData [tid=" + tid + ", dcNetworkTime=" + dcNetworkTime + ", iteamNumber=" + iteamNumber
				+ ", authenticationScheme=" + authenticationScheme + ", authenticate=" + authenticate
				+ ", authorization=" + authorization + ", confidentialityKey=" + confidentialityKey + ", integrityKey="
				+ integrityKey + ", impi=" + impi + ", result=" + result + ", isDigest=" + isDigest + ", digestRealm="
				+ digestRealm + ", digestAlgo=" + digestAlgorithm + ", digestQOP=" + digestQOP + ", digestHA1="
				+ digestHA1 + "]";
	}

}
