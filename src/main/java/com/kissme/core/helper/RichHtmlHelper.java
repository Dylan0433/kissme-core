package com.kissme.core.helper;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Sets;

/**
 * 
 * @author loudyn
 * 
 */
public final class RichHtmlHelper {
	
	public static Set<String> populateJavascripts(String html){

		if (StringUtils.isBlank(html)) {
			return Collections.emptySet();
		}

		Document doc = Jsoup.parse(html);
		Elements eles = doc.select("script[src$=js]");
		if (eles.isEmpty()) {
			return Collections.emptySet();
		}

		Set<String> javascripts = Sets.newHashSet();
		for (Element ele : eles) {
			javascripts.add(ele.attr("src"));
		}

		return javascripts;
	}
	
	public static Set<String> populateStylesheets(String html){

		if (StringUtils.isBlank(html)) {
			return Collections.emptySet();
		}

		Document doc = Jsoup.parse(html);
		Elements eles = doc.select("link[href$=css]");
		if (eles.isEmpty()) {
			return Collections.emptySet();
		}

		Set<String> stylesheets = Sets.newHashSet();
		for (Element ele : eles) {
			stylesheets.add(ele.attr("href"));
		}

		return stylesheets;
	}

	/**
	 * 
	 * @param html
	 * @return
	 */
	public static Set<String> populatePhotos(String html) {

		if (StringUtils.isBlank(html)) {
			return Collections.emptySet();
		}

		Document doc = Jsoup.parse(html);
		Elements eles = doc.select("img[src]");
		if (eles.isEmpty()) {
			return Collections.emptySet();
		}

		Set<String> photos = Sets.newHashSet();
		for (Element ele : eles) {
			photos.add(ele.attr("src"));
		}

		return photos;
	}

	/**
	 * 
	 * @param html
	 * @return
	 */
	public static Set<String> populateFlashes(String html) {
		if (StringUtils.isBlank(html)) {
			return Collections.emptySet();
		}

		Document doc = Jsoup.parse(html);
		Elements eles = doc.select("embed[src][type$=flash]");
		if (eles.isEmpty()) {
			return Collections.emptySet();
		}

		Set<String> flashes = Sets.newHashSet();
		for (Element ele : eles) {
			flashes.add(ele.attr("src"));
		}

		return flashes;
	}

	private RichHtmlHelper() {}
}
