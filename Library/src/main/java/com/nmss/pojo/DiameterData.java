package com.nmss.pojo;

public class DiameterData {
	private RequestType requestType;
	private String tid;
	private String impi;
	private Boolean isDigest;
	private long dcNetworkTime;
	private MAR mar;
	private UDR udr;
	private ZX_SSO zx_sso;
	private ZX_DIGEST zx_digest;
	private MAA maa;

	private Integer result;
	private Integer experimentalResultCode;

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
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

	public Integer getResult() {
		return result;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public MAR getMar() {
		return mar;
	}

	public void setMar(MAR mar) {
		this.mar = mar;
	}

	public UDR getUdr() {
		return udr;
	}

	public void setUdr(UDR udr) {
		this.udr = udr;
	}

	public ZX_SSO getZx_sso() {
		return zx_sso;
	}

	public void setZx_sso(ZX_SSO zx_sso) {
		this.zx_sso = zx_sso;
	}

	public ZX_DIGEST getZx_digest() {
		return zx_digest;
	}

	public void setZx_digest(ZX_DIGEST zx_digest) {
		this.zx_digest = zx_digest;
	}

	public MAA getMaa() {
		return maa;
	}

	public void setMaa(MAA maa) {
		this.maa = maa;
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

	@Override
	public String toString() {
		return "DiameterData [requestType=" + requestType + ", tid=" + tid + ", impi=" + impi + ", isDigest=" + isDigest
				+ ", dcNetworkTime=" + dcNetworkTime + ", mar=" + mar + ", udr=" + udr + ", zx_sso=" + zx_sso
				+ ", zx_digest=" + zx_digest + ", maa=" + maa + ", result=" + result + ", experimentalResultCode="
				+ experimentalResultCode + "]";
	}

}
