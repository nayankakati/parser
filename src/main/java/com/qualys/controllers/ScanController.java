package com.qualys.controllers;

import static com.qualys.helpers.ResponseGenerator.createReportResponse;
import static com.qualys.helpers.ResponseGenerator.createScanResponse;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qualys.entities.ScanDetails;
import com.qualys.responses.ErrorResponse;
import com.qualys.responses.Response;
import com.qualys.services.ScanService;


@RestController
@ComponentScan
@RequestMapping("/api")
public class ScanController {

	@Autowired
	private ScanService scanService;

	@PostMapping("/submit")
	public Response submitUrlForAScan(@RequestBody String urlAddr) {
		if(!(urlAddr.contains("http")|| urlAddr.contains("https")))
			return new ErrorResponse("INVALID_URL_FORMAT", "Invalid URL format, Please enter URL with http[s]");
		try {
					ScanDetails scanDetails = scanService.submitUrlForAScan(urlAddr);
					return createScanResponse(Arrays.asList(scanDetails)).get(0);
		} catch (Exception e) {
					return new ErrorResponse(HttpStatus.EXPECTATION_FAILED.name(), e.getMessage());
		}
	}

	@GetMapping("/list")
	public List<?> listScan(@RequestParam(required = false) @NonNull Long fromScan, @RequestParam(required = false) @NonNull Long toScan, @RequestParam(required = false) @NonNull Integer size) {
		try {
			List<ScanDetails> scanDetails = scanService.listScan(fromScan, toScan, size);
			return createScanResponse(scanDetails);
		} catch (Exception e) {
			return Arrays.asList(new ErrorResponse(HttpStatus.EXPECTATION_FAILED.name(), e.getMessage()));
		}
	}

	@GetMapping("/scan/{id}")
	public List<?> getScanDetail(@PathVariable Long id) {
		try {
			ScanDetails scanDetails = scanService.getScanDetail(id);
			return createReportResponse(scanDetails);
		} catch (Exception e) {
			return Arrays.asList(new ErrorResponse(HttpStatus.EXPECTATION_FAILED.name(), e.getMessage()));
		}
	}
}
