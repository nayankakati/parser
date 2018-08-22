package com.qualys.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexGenerator {

	public static Matcher getTitlePatternMatcher(String search) {
		Pattern titlePattern = Pattern.compile("<title[^>]*>([\\s\\S]+)<\\/title>");
		return titlePattern.matcher(search);
	}

	public static Matcher getBodyTextPatternMatcher(String search) {
		Pattern textPattern = Pattern.compile("(<span[^>]*>([\\s\\S]*)<\\/span)|(<p[^>]*>([\\s\\S]*)<\\/p>)|(<a[^>]*>([\\s\\S]*)<\\/a)|(<h[123456]+[^>]*>([\\s\\S]*)<\\/h[123456]+)\n");
		return textPattern.matcher(search);
	}

	public static Matcher getHtmlBodyPatternMatcher(String search) {
		Pattern bodyPattern = Pattern.compile(".*?<body.*?>(.*?)</body>.*?", Pattern.DOTALL);
		return bodyPattern.matcher(search);
	}

	public static Matcher getImagePatternMatcher(String search) {
		Pattern imagePattern = Pattern.compile("(<img)[.]*");
		return imagePattern.matcher(search);
	}

	public static Matcher getLinksPatternMatcher(String search) {
		Pattern linkPattern = Pattern.compile("<a([^>]+)>");
		return linkPattern.matcher(search);
	}
}
