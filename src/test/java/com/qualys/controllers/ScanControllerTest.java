package com.qualys.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.qualys.HtmlparserApplication;
import com.qualys.entities.Domain;
import com.qualys.entities.ScanDetails;
import com.qualys.enums.ScanStatus;
import com.qualys.services.ScanService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ScanController.class, secure = false)
@ContextConfiguration(classes = HtmlparserApplication.class)
public class ScanControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean(name="scanService")
	private ScanService scanService;
	private ScanDetails scanDetails;
	private Domain domain;

	@Test
	public void test_submit_url_for_a_scan_success() throws Exception {
		Mockito.when(scanService.submitUrlForAScan(anyString())).thenReturn(getScanDetails().get(0));
		RequestBuilder builder = MockMvcRequestBuilders.post("/api/submit/").param("urlAddr","https://google.co.in").contentType(MediaType.APPLICATION_FORM_URLENCODED);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"id\":1,\"hostname\":\"google.in\",\"scanStatus\":\"SUCCESS\",\"ipAddress\":\"10.10.10.10\",\"title\":\"website title\",\"submittedOn\":\"Mon Jun 18 21:17:36 IST 2018\"}\n", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_submit_url_for_a_scan_invalid_url() throws Exception {
		Mockito.when(scanService.submitUrlForAScan(anyString())).thenReturn(getScanDetails().get(0));
		RequestBuilder builder = MockMvcRequestBuilders.post("/api/submit/").param("urlAddr","google.co.in").contentType(MediaType.APPLICATION_FORM_URLENCODED);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"message\":\"Invalid URL format, Please enter URL with http[s]\",\"code\":\"INVALID_URL_FORMAT\"}\n", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_submit_url_for_a_scan_exception() throws Exception {
		Mockito.when(scanService.submitUrlForAScan(anyString())).thenThrow(new RuntimeException("Exception"));
		RequestBuilder builder = MockMvcRequestBuilders.post("/api/submit/").param("urlAddr","https://google.co.in").contentType(MediaType.APPLICATION_FORM_URLENCODED);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("{\"message\":\"Exception\",\"code\":\"EXPECTATION_FAILED\"}",mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_get_scan_details_success() throws Exception {
		Mockito.when(scanService.getScanDetail(anyLong())).thenReturn(getScanDetails().get(0));
		RequestBuilder builder = MockMvcRequestBuilders.get("/api/scan/1").accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("[{\"url\":\"google.in\",\"redirectionUrl\":\"google.com\",\"submittedOn\":\"Mon Jun 18 21:17:36 IST 2018\",\"title\":\"website title\",\"body\":\"website body\",\"imgCount\":10,\"linksCount\":25}]\n", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_get_scan_details_failure() throws Exception {
		ScanDetails details = getScanDetails().get(0);
		details.setDomainId(null);
		Mockito.when(scanService.getScanDetail(anyLong())).thenReturn(details);
		RequestBuilder builder = MockMvcRequestBuilders.get("/api/scan/1").accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("[{\"message\":null,\"code\":\"EXPECTATION_FAILED\"}]",mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_list_scan_success() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("fromScan","1");
		params.add("toScan","2");
		params.add("size", "3");
		Mockito.when(scanService.listScan(1l, 2l, 3)).thenReturn(getScanDetails());
		RequestBuilder builder = MockMvcRequestBuilders.get("/api/list").params(params).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("[{\"id\":1,\"hostname\":\"google.in\",\"scanStatus\":\"SUCCESS\",\"ipAddress\":\"10.10.10.10\",\"title\":\"website title\",\"submittedOn\":\"Mon Jun 18 21:17:36 IST 2018\"}]\n", mvcResult.getResponse().getContentAsString(), false);
	}

	@Test
	public void test_list_scan_failure() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("fromScan","1");
		params.add("toScan","2");
		params.add("size", "3");
		Mockito.when(scanService.listScan(1l, 2l, 3)).thenThrow(new Exception("Exception"));
		RequestBuilder builder = MockMvcRequestBuilders.get("/api/list").params(params).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		JSONAssert.assertEquals("[{\"message\":\"Exception\",\"code\":\"EXPECTATION_FAILED\"}]",mvcResult.getResponse().getContentAsString(), false);
	}


	private List<ScanDetails> getScanDetails() {
		List<ScanDetails> scanDetailss =  new ArrayList<>();
		domain = new Domain();
		domain.setId(1l);
		domain.setHostname("google.in");
		domain.setIpAddress("10.10.10.10");
		domain.setRedirectionUrl("google.com");
		scanDetails = new ScanDetails();
		scanDetails.setDomainId(domain);
		scanDetails.setId(1l);
		scanDetails.setWebsiteBody("website body");
		scanDetails.setWebsiteTitle("website title");
		scanDetails.setSubmittedOn(Date.from(Instant.ofEpochMilli(1529336856297l)));
		scanDetails.setStatus(ScanStatus.SUCCESS);
		scanDetails.setImgCount(10l);
		scanDetails.setLinksCount(25l);
		scanDetailss.add(scanDetails);
		return scanDetailss;
	}
}
