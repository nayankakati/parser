package com.qualys.responses;

import com.qualys.enums.ScanStatus;


public class ScanResponse extends Response {

	private Long id;
	private String hostname;
	private ScanStatus scanStatus;
	private String ipAddress;
	private String title;
	private String submittedOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public ScanStatus getScanStatus() {
		return scanStatus;
	}

	public void setScanStatus(ScanStatus scanStatus) {
		this.scanStatus = scanStatus;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(String submittedOn) {
		this.submittedOn = submittedOn;
	}
}
