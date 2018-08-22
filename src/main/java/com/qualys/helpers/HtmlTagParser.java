package com.qualys.helpers;

import static com.qualys.helpers.RegexGenerator.getBodyTextPatternMatcher;
import static com.qualys.helpers.RegexGenerator.getHtmlBodyPatternMatcher;
import static com.qualys.helpers.RegexGenerator.getImagePatternMatcher;
import static com.qualys.helpers.RegexGenerator.getLinksPatternMatcher;
import static com.qualys.helpers.RegexGenerator.getTitlePatternMatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.qualys.entities.Domain;
import com.qualys.entities.ScanDetails;
import com.qualys.enums.ScanStatus;


@Component
public class HtmlTagParser {

	public Domain scanForDomain(String url) throws IOException {
		Domain domain = new Domain();
		URL fullUrl = new URL(url);

		InetAddress in= InetAddress.getByName(fullUrl.getHost());

		HttpURLConnection con = (HttpURLConnection)(fullUrl.openConnection());
		con.setInstanceFollowRedirects( false );
		con.connect();
		String location = con.getHeaderField( "Location" );
		con.disconnect();
		domain.setIpAddress(in.getHostAddress());
		domain.setHostname(in.getHostName());
		domain.setRedirectionUrl(location);
		return domain;
	}

	public  ScanDetails startScanning(String url) throws IOException {
		ScanDetails details =  new ScanDetails();
		String html = readFromUrl(url);
		details.setWebsiteTitle(getTitleFromHtml(html));
		details.setWebsiteBody(getBodyTextFromHtml(html).toString());
		details.setImgCount(new Long(getImageCountFromHtml(html).size()));
		details.setLinksCount(new Long(getLinksCountFromHtml(html).size()));
		details.setSubmittedOn(new Date(System.currentTimeMillis()));
		details.setStatus(ScanStatus.SUCCESS);
		return details;
	}

	private String readFromUrl(String url) throws IOException {
		StringBuffer response = new StringBuffer();
		String line;

		URL fullUrl = new URL(url);
		HttpURLConnection con = (HttpURLConnection)(fullUrl.openConnection());
		con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
		con.setInstanceFollowRedirects(true);
		con.connect();
		InputStream inputStream = con.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));

		try {
			while ((line = rd.readLine()) != null) {
				response.append(line).append("\n");
			}
		} catch ( IOException exception) {
				throw exception;
		} finally {
			rd.close();
			con.disconnect();
		}
		return response.toString();
	}

	private String getTitleFromHtml(String html) {
		String title = "";
		Matcher titleMatcher = getTitlePatternMatcher(html);
		while (titleMatcher.find()) {
			title = titleMatcher.group(1).replaceAll("&[^\\s]*;","");
			title = title.replaceAll("[^\\w]"," ");
		}
		return title;
	}

	private List<String> getLinksCountFromHtml(String html) {
		Matcher linkMatcher = getLinksPatternMatcher(html);
		List<String> link = new ArrayList<>();
		while(linkMatcher.find()) {
			link.add(linkMatcher.group());
		}
		return link;
	}

	private List<String> getImageCountFromHtml(String html) {
		Matcher imageMatcher = getImagePatternMatcher(html);
		List<String> images = new ArrayList<>();

		while(imageMatcher.find()) {
			images.add(imageMatcher.group());
		}
		return images;
	}

	private StringBuilder getBodyTextFromHtml(String html) {
		Matcher bodyMatcher = getHtmlBodyPatternMatcher(html);
		StringBuilder bodyText = new StringBuilder();
		if (bodyMatcher.find()) {
			String bodyGroup = bodyMatcher.group(1).replaceAll("&[^\\s]*;","");
			Matcher bodyTextMatcher = getBodyTextPatternMatcher(bodyGroup);
			while (bodyTextMatcher.find()) {
				String setting = "";
				if(bodyTextMatcher.group(2) != null)
					setting = bodyTextMatcher.group(2);
				else if(bodyTextMatcher.group(4) != null)
					setting = bodyTextMatcher.group(4);
				else if(bodyTextMatcher.group(6) != null)
					setting = bodyTextMatcher.group(6);
				else if(bodyTextMatcher.group(8) != null)
					setting = bodyTextMatcher.group(8);

				Pattern extractPattern = Pattern.compile(">([\\w\\d$&+,:;=?@#|'.\\-^*()%!\\s]*)<");
				Matcher extractMatcher = extractPattern.matcher(setting);
				while (extractMatcher.find()) {
					String text = extractMatcher.group(1);
					text = text.replaceAll("[^\\w]"," ");
					bodyText.append(text).append(" ");
				}
			}
		}
		return bodyText;
	}
}
