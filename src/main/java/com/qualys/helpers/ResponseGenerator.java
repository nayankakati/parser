package com.qualys.helpers;

import java.util.ArrayList;
import java.util.List;

import com.qualys.entities.ScanDetails;
import com.qualys.responses.ReportResponse;
import com.qualys.responses.ScanResponse;


public class ResponseGenerator {
	public static List<ScanResponse> createScanResponse(List<ScanDetails> scanDetails) {
		List<ScanResponse> scanResponse = new ArrayList<>();
		for (ScanDetails scanDetail : scanDetails) {
			ScanResponse response = new ScanResponse();
			response.setId(scanDetail.getId());
			response.setHostname(scanDetail.getDomainId().getHostname());
			response.setIpAddress(scanDetail.getDomainId().getIpAddress());
			response.setScanStatus(scanDetail.getStatus());
			response.setSubmittedOn(scanDetail.getSubmittedOn().toString());
			response.setTitle(scanDetail.getWebsiteTitle());
			scanResponse.add(response);
		}
		return scanResponse;
	}

	public static List<ReportResponse> createReportResponse(ScanDetails scanDetail) {
		List<ReportResponse> reportResponses = new ArrayList<>();
		ReportResponse reportResponse = new ReportResponse();
		reportResponse.setUrl(scanDetail.getDomainId().getHostname());
		reportResponse.setTitle(scanDetail.getWebsiteTitle());
		reportResponse.setBody(scanDetail.getWebsiteBody());
		reportResponse.setImgCount(scanDetail.getImgCount());
		reportResponse.setLinksCount(scanDetail.getLinksCount());
		reportResponse.setRedirectionUrl(scanDetail.getDomainId().getRedirectionUrl());
		reportResponse.setSubmittedOn(scanDetail.getSubmittedOn().toString());
		reportResponses.add(reportResponse);
		return reportResponses;
	}
}
