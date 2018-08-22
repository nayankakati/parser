package com.qualys.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.qualys.enums.ScanStatus;


@Entity
@Table(name = "scan_details")
public class ScanDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "domain_id")
	private Domain domainId;

	@Column(name = "website_title")
	private String websiteTitle;

	@Column(name = "website_body", columnDefinition = "LONGTEXT", length = 65535)
	private String websiteBody;

	@Column(name = "img_count")
	private Long imgCount;

	@Column(name = "links_count")
	private Long linksCount;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ScanStatus status;

	@Column(name = "submitted_on")
	private Date submittedOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Domain getDomainId() {
		return domainId;
	}

	public void setDomainId(Domain domainId) {
		this.domainId = domainId;
	}

	public String getWebsiteTitle() {
		return websiteTitle;
	}

	public void setWebsiteTitle(String websiteTitle) {
		this.websiteTitle = websiteTitle;
	}

	public String getWebsiteBody() {
		return websiteBody;
	}

	public void setWebsiteBody(String websiteBody) {
		this.websiteBody = websiteBody;
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

	public ScanStatus getStatus() {
		return status;
	}

	public void setStatus(ScanStatus status) {
		this.status = status;
	}

	public Date getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(Date submittedOn) {
		this.submittedOn = submittedOn;
	}
}
