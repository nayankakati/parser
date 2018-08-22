package com.qualys.controllers;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qualys.HtmlparserApplication;
import com.qualys.entities.Domain;
import com.qualys.entities.ScanDetails;
import com.qualys.enums.ScanStatus;
import com.qualys.repositories.DomainRepository;
import com.qualys.repositories.ScanDetailsRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HtmlparserApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class ScanControllerIT {

	@Autowired
	private DomainRepository domainRepository;
	@Autowired
	private ScanDetailsRepository scanDetailsRepository;
	@LocalServerPort
	private int port;
	private TestRestTemplate restTemplate = new TestRestTemplate();
	private HttpHeaders headers = new HttpHeaders();
	private Domain domain;
	private ScanDetails scanDetails;

	public void init() {
		domain = new Domain();
		domain.setHostname("google.in");
		domain.setIpAddress("10.10.10.10");
		domain.setRedirectionUrl("google.com");
		domain = domainRepository.saveAndFlush(domain);

		scanDetails = new ScanDetails();
		scanDetails.setDomainId(domain);
		scanDetails.setWebsiteBody("website body");
		scanDetails.setWebsiteTitle("website title");
		scanDetails.setSubmittedOn(Date.from(Instant.ofEpochMilli(1529336856297l)));
		scanDetails.setStatus(ScanStatus.SUCCESS);
		scanDetails.setImgCount(10l);
		scanDetails.setLinksCount(25l);
		scanDetailsRepository.save(scanDetails);
	}

	@Test
	public void test_submit_url_for_scanning_success() throws Exception {
		Map payload = new HashMap();
		payload.put("urlAddr", "https://google.co.in");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/submit"),
			HttpMethod.POST, entity, String.class);
	}

	@Test
	public void test_submit_url_for_scanning_invalid_url_format_failure() throws Exception {
		Map payload = new HashMap();
		payload.put("urlAddr", "//google.co.in");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/submit"),
			HttpMethod.POST, entity, String.class);
		String expected = "{\"message\":\"Invalid URL format, Please enter URL with http[s]\",\"code\":\"INVALID_URL_FORMAT\"}\n";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void test_submit_url_for_scanning_invalid_url_failure() throws Exception {
		String url = "http://adfadsf";
		Map payload = new HashMap();
		payload.put("urlAddr", url);

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/submit"),
			HttpMethod.POST, entity, String.class);
		String expected = "{\"message\":\"no protocol: {\\\"urlAddr\\\":\\\""+url+"\\\"}\",\"code\":\"EXPECTATION_FAILED\"}\n";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void test_list_scanning_success() throws Exception {
		init();
		Map payload = new HashMap();
		payload.put("fromScan", "1529336856297");
		payload.put("toScan", "1529336856298");
		payload.put("size", "1");

		HttpEntity<?> entity = new HttpEntity<>(payload, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/list?fromScan=1529336856297&toScan=1529336856298&size=1"),
			HttpMethod.GET, entity, String.class);

		String expected = "[{\"id\":"+scanDetails.getId()+",\"hostname\":\"google.in\",\"scanStatus\":\"SUCCESS\",\"ipAddress\":\"10.10.10.10\",\"title\":\"website title\",\"submittedOn\":\"2018-06-18 21:17:36.297\"}]\n";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void test_get_detail_success() throws Exception {
		init();
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/scan/"+scanDetails.getId()),
			HttpMethod.GET, entity, String.class);

		String expected = "[{\"url\":\"google.in\",\"redirectionUrl\":\"google.com\",\"submittedOn\":\"2018-06-18 21:17:36.297\",\"title\":\"website title\",\"body\":\"website body\",\"imgCount\":10,\"linksCount\":25}]\n";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void test_get_detail_failure() throws Exception {
		init();
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
			createURLWithPort("/api/scan/9990"),
			HttpMethod.GET, entity, String.class);

		String expected = "[{\"message\":\"Scan Id not found\",\"code\":\"EXPECTATION_FAILED\"}]\n";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
