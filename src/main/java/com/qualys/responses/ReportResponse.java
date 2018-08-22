package com.qualys.responses;


public class ReportResponse extends Response {
	private String url;
	private String redirectionUrl;
	private String submittedOn;
	private String title;
	private String body;
	private Long imgCount;
	private Long linksCount;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRedirectionUrl() {
		return redirectionUrl;
	}

	public void setRedirectionUrl(String redirectionUrl) {
		this.redirectionUrl = redirectionUrl;
	}

	public String getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(String submittedOn) {
		this.submittedOn = submittedOn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getImgCount() {
		return imgCount;
	}

	public void setImgCount(Long imgCount) {
		this.imgCount = imgCount;
	}

	public Long getLinksCount() {
		return linksCount;
	}

	public void setLinksCount(Long linksCount) {
		this.linksCount = linksCount;
	}
}
