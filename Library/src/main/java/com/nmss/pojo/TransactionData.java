package com.nmss.pojo;

public class TransactionData {
	private String IMPI;
	private String tid;
	private long startTime;

	public String getIMPI() {
		return IMPI;
	}

	public void setIMPI(String iMPI) {
		IMPI = iMPI;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		return "TransactionData [IMPI=" + IMPI + ", tid=" + tid + ", startTime=" + startTime + "]";
	}

}
