package com.nmss.pojo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Stats {

	private String date;
	private String clientId;
	private Integer total = 0;
	private Map<Integer, Integer> status;

	public Stats(String clientId) {
		this.clientId = clientId;
		this.status = new ConcurrentHashMap<>();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Map<Integer, Integer> getStatus() {
		return status;
	}

	public void setStatus(Map<Integer, Integer> status) {
		this.status = status;
	}

	public void addResponse(Integer resultCode) {
		if (status.get(resultCode) == null)
			status.put(resultCode, 1);
		else
			status.put(resultCode, status.get(resultCode) + 1);
	}

	public void addRequest() {
		total++;
	}

}
