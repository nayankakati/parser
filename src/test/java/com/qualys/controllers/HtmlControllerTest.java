package com.qualys.controllers;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@RunWith(SpringRunner.class)
@WebMvcTest(value = HtmlController.class, secure = false)
@ContextConfiguration(classes = HtmlparserApplication.class)
public class HtmlControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void test_scan_success() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("id","1");
		params.add("model", "model");

		RequestBuilder builder = MockMvcRequestBuilders.get("/scan").params(params).accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(builder).andReturn();
		assertNotNull(mvcResult);
	}
}
