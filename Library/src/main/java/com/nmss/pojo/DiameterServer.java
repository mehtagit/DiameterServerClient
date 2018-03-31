package com.nmss.pojo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DiameterServer {

	private String clientId;
	private String destinationIP;
	private Integer destinationPort;
	private String destinationRelam;
	private String originIp;
	private Integer originPort;
	private String originRelam;
	private Integer vendorId;
	private String applicationId;
	private String product;
	private Integer authApp;
	private boolean isDigestTrueGbuFalse;

	public String getDestinationIP() {
		return destinationIP;
	}

	public void setDestinationIP(String destinationIP) throws Exception {
		this.destinationIP = destinationIP;
	}

	public Integer getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(Integer destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getDestinationRelam() {
		return destinationRelam;
	}

	public void setDestinationRelam(String destinationRelam) {
		this.destinationRelam = destinationRelam;
	}

	public String getOriginIp() {
		return originIp;
	}

	public void setOriginIp(String originIp) {
		this.originIp = originIp;
	}

	public Integer getOriginPort() {
		return originPort;
	}

	public void setOriginPort(Integer originPort) {
		this.originPort = originPort;
	}

	public String getOriginRelam() {
		return originRelam;
	}

	public void setOriginRelam(String originRelam) {
		this.originRelam = originRelam;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Integer getAuthApp() {
		return authApp;
	}

	public void setAuthApp(Integer authApp) {
		this.authApp = authApp;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public boolean isDigestTrueGbuFalse() {
		return isDigestTrueGbuFalse;
	}

	public void setDigestTrueGbuFalse(boolean isDigestTrueGbuFalse) {
		this.isDigestTrueGbuFalse = isDigestTrueGbuFalse;
	}

	@Override
	public String toString() {
		return "DiameterServer [clientId=" + clientId + ", destinationIP=" + destinationIP + ", destinationPort="
				+ destinationPort + ", destinationRelam=" + destinationRelam + ", originIp=" + originIp
				+ ", originPort=" + originPort + ", originRelam=" + originRelam + ", vendorId=" + vendorId
				+ ", applicationId=" + applicationId + ", product=" + product + ", authApp=" + authApp
				+ ", isDigestTrueGbuFalse=" + isDigestTrueGbuFalse + "]";
	}

}
