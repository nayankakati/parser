package com.qualys.services;


import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qualys.entities.Domain;
import com.qualys.entities.ScanDetails;
import com.qualys.enums.ScanStatus;
import com.qualys.exceptions.ScanDetailNotFoundException;
import com.qualys.helpers.HtmlTagParser;
import com.qualys.repositories.DomainRepository;
import com.qualys.repositories.ScanDetailsRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ScanServiceImplTest {
	@Mock
	private HtmlTagParser htmlTagParser;
	@Mock
	private DomainRepository domainRepository;
	@Mock
	private ScanDetailsRepository scanDetailsRepository;
	@InjectMocks
	private ScanServiceImpl scanServiceImpl;

	private Domain domain;
	private ScanDetails scanDetails;
	private List<ScanDetails> scanDetailss;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		domain = getDomain();
		scanDetails = getScanDetails().get(0);
		scanDetailss = getScanDetails();
	}

	@Test
	public void test_url_scanning_success() throws Exception {
		when(htmlTagParser.scanForDomain(anyString())).thenReturn(domain);
		when(domainRepository.save(domain)).thenReturn(domain);
		when(htmlTagParser.startScanning(anyString())).thenReturn(scanDetails);
		when(scanDetailsRepository.save(scanDetails)).thenReturn(scanDetails);

		ScanDetails actualScanDetails =  scanServiceImpl.submitUrlForAScan(anyString());
		assertEquals(actualScanDetails.getWebsiteTitle(), scanDetails.getWebsiteTitle());
		assertEquals(actualScanDetails.getWebsiteBody(), scanDetails.getWebsiteBody());
		assertEquals(actualScanDetails.getDomainId(), scanDetails.getDomainId());
		assertEquals(actualScanDetails.getImgCount(), scanDetails.getImgCount());
		assertEquals(actualScanDetails.getLinksCount(), scanDetails.getLinksCount());
		assertEquals(actualScanDetails.getStatus(), scanDetails.getStatus());
		assertEquals(actualScanDetails.getSubmittedOn(), scanDetails.getSubmittedOn());
		assertEquals(actualScanDetails.getDomainId().getHostname(), scanDetails.getDomainId().getHostname());
		assertEquals(actualScanDetails.getDomainId().getIpAddress(), scanDetails.getDomainId().getIpAddress());
		assertEquals(actualScanDetails.getDomainId().getRedirectionUrl(), scanDetails.getDomainId().getRedirectionUrl());
	}

	@Test
	public void test_url_scanning_domain_scanning_failure() throws Exception {
		when(htmlTagParser.scanForDomain(anyString())).thenThrow(new IOException());
		when(domainRepository.save(domain)).thenReturn(domain);
		when(htmlTagParser.startScanning(anyString())).thenReturn(scanDetails);
		when(scanDetailsRepository.save(scanDetails)).thenReturn(scanDetails);
		assertThatExceptionOfType(IOException.class)
						.isThrownBy(() -> scanServiceImpl.submitUrlForAScan(anyString()))
						.is(new Condition<>(e -> Objects.nonNull(e), ""));
	}

	@Test
	public void test_url_scanning_failure() throws Exception {
		when(htmlTagParser.scanForDomain(anyString())).thenReturn(domain);
		when(domainRepository.save(domain)).thenReturn(domain);
		when(htmlTagParser.startScanning(anyString())).thenThrow(new IOException());
		when(scanDetailsRepository.save(scanDetails)).thenReturn(scanDetails);
		assertThatExceptionOfType(IOException.class)
			.isThrownBy(() -> scanServiceImpl.submitUrlForAScan(anyString()))
			.is(new Condition<>(e -> Objects.nonNull(e), ""));
	}

	@Test
	public void test_url_scanning_domain_scanning_malformed_failure() throws Exception {
		when(htmlTagParser.scanForDomain(anyString())).thenThrow(new MalformedURLException());
		when(domainRepository.save(domain)).thenReturn(domain);
		when(htmlTagParser.startScanning(anyString())).thenReturn(scanDetails);
		when(scanDetailsRepository.save(scanDetails)).thenReturn(scanDetails);
		assertThatExceptionOfType(MalformedURLException.class)
			.isThrownBy(() -> scanServiceImpl.submitUrlForAScan(anyString()))
			.is(new Condition<>(e -> Objects.nonNull(e), ""));
	}

	@Test
	public void test_list_scan_success() {
		when(scanDetailsRepository.listScan(anyLong(), anyLong(), anyInt())).thenReturn(scanDetailss);
		List<ScanDetails> actualScanDetailss = scanServiceImpl.listScan(anyLong(), anyLong(), anyInt());
		assertEquals(actualScanDetailss.size(), scanDetailss.size());
		assertEquals(actualScanDetailss.get(0).getWebsiteTitle(), scanDetailss.get(0).getWebsiteTitle());
		assertEquals(actualScanDetailss.get(0).getWebsiteBody(), scanDetailss.get(0).getWebsiteBody());
		assertEquals(actualScanDetailss.get(0).getDomainId(), scanDetailss.get(0).getDomainId());
		assertEquals(actualScanDetailss.get(0).getImgCount(), scanDetailss.get(0).getImgCount());
		assertEquals(actualScanDetailss.get(0).getLinksCount(), scanDetailss.get(0).getLinksCount());
		assertEquals(actualScanDetailss.get(0).getStatus(), scanDetailss.get(0).getStatus());
		assertEquals(actualScanDetailss.get(0).getSubmittedOn(), scanDetailss.get(0).getSubmittedOn());
	}

	@Test
	public void test_get_scan_detail_success() {
		when(scanDetailsRepository.findById(anyLong())).thenReturn(Optional.of(scanDetails));
		ScanDetails actualScanDetails = scanServiceImpl.getScanDetail(anyLong());
		assertEquals(actualScanDetails.getWebsiteTitle(), scanDetails.getWebsiteTitle());
		assertEquals(actualScanDetails.getWebsiteBody(), scanDetails.getWebsiteBody());
		assertEquals(actualScanDetails.getDomainId(), scanDetails.getDomainId());
		assertEquals(actualScanDetails.getImgCount(), scanDetails.getImgCount());
		assertEquals(actualScanDetails.getLinksCount(), scanDetails.getLinksCount());
		assertEquals(actualScanDetails.getStatus(), scanDetails.getStatus());
		assertEquals(actualScanDetails.getSubmittedOn(), scanDetails.getSubmittedOn());
	}

	@Test
	public void test_get_scan_detail_failure() {
		when(scanDetailsRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThatExceptionOfType(ScanDetailNotFoundException.class)
			.isThrownBy(() -> scanServiceImpl.getScanDetail(anyLong()))
			.is(new Condition<>(e -> Objects.nonNull(e), ""));
	}
	private Domain getDomain() {
		domain = new Domain();
		domain.setId(1l);
		domain.setHostname("google.in");
		domain.setIpAddress("10.10.10.10");
		domain.setRedirectionUrl("google.com");
		return domain;
	}

	private List<ScanDetails> getScanDetails() {
		List<ScanDetails> scanDetailss =  new ArrayList<>();
		scanDetails = new ScanDetails();
		scanDetails.setId(1l);
		scanDetails.setDomainId(domain);
		scanDetails.setWebsiteBody("website body");
		scanDetails.setWebsiteTitle("website title");
		scanDetails.setSubmittedOn(new Date());
		scanDetails.setStatus(ScanStatus.SUCCESS);
		scanDetails.setImgCount(10l);
		scanDetails.setLinksCount(25l);
		scanDetailss.add(scanDetails);
		return scanDetailss;
	}
}
