package org.scraper.model;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.scraper.controller.ApplicationProperties;
import org.scraper.enumeration.StatusCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Crawler {

	private ApplicationProperties properties = new ApplicationProperties();

	private final int CONNECTION_TIMEOUT_MS = 30 * 1000;//30 seg
	private BasicCookieStore cookieStore;
	private Map<String, String> headers;
	private Integer statusCode;
	private String dominio;
	private Document lastResult;
	private Element lastForm;

	private String lastUrl;

	public Crawler(String dominio){
		this.dominio = dominio;
	}

	public String get(String url) {
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(CONNECTION_TIMEOUT_MS)
				.setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
				.setSocketTimeout(CONNECTION_TIMEOUT_MS).build();
		HttpClientContext context = HttpClientContext.create();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).setDefaultRequestConfig(config).build();

		try{
			HttpGet request = new HttpGet(this.dominio + url);

			for(String key : this.headers.keySet()){
				request.addHeader(key, this.headers.get(key));
			}

			HttpResponse response =  httpClient.execute(request, context);
			this.statusCode = response.getStatusLine().getStatusCode();

			Logger.getLogger(getClass().getName()).log(Level.INFO, "GET: " + this.dominio + url + " Status: "+this.statusCode);

			BasicCookieStore newCookieStore = (BasicCookieStore) context.getCookieStore();

			for(Cookie cookie : newCookieStore.getCookies()){
				if(cookieStore == null){
					cookieStore = new BasicCookieStore();
				}
				cookieStore.addCookie(cookie);
			}

			if(this.statusCode.equals(StatusCode.OK.getCode())) {

				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// return it as a String
					String result = EntityUtils.toString(entity);
					this.lastResult = Jsoup.parse(result);
					if(this.lastResult.getElementById("aspnetForm") != null){
						this.lastForm= this.lastResult.getElementById(properties.readProperty("cortinaweb.aspnetForm"));
					}
					this.lastUrl = url;
					return result;
				}
			}
			else if(this.statusCode.equals(StatusCode.Found.getCode())){
				Header header = response.getFirstHeader("Location");
				String newLocation = header.getValue();
				return get(this.dominio + newLocation);
			}


		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e){
			throw new RuntimeException(e);
		}

		return null;
	}

	public String post(String url, Map<String, String> params) {

		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(CONNECTION_TIMEOUT_MS)
				.setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
				.setSocketTimeout(CONNECTION_TIMEOUT_MS).build();
		HttpClientContext context = HttpClientContext.create();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).setDefaultRequestConfig(config).build();

		try{
			HttpPost request = new HttpPost(this.dominio + url);

			for(String key : this.headers.keySet()){
				request.addHeader(key, this.headers.get(key));
			}

			List<NameValuePair> urlParameters = new ArrayList<>();
			for(String key : params.keySet()){
				String value = params.get(key);
				urlParameters.add(new BasicNameValuePair(key, value));
			}
			request.setEntity(new UrlEncodedFormEntity(urlParameters));

			HttpResponse response =  httpClient.execute(request, context);
			this.statusCode = response.getStatusLine().getStatusCode();
			Logger.getLogger(getClass().getName()).log(Level.INFO, "POST: " + this.dominio + url + " Status: "+this.statusCode);

			BasicCookieStore newCookieStore = (BasicCookieStore) context.getCookieStore();

			for(Cookie cookie : newCookieStore.getCookies()){
				if(cookieStore == null){
					cookieStore = new BasicCookieStore();
				}
				cookieStore.addCookie(cookie);
			}

			if(this.statusCode.equals(StatusCode.OK.getCode())) {

				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// return it as a String
					String result = EntityUtils.toString(entity);
					this.lastResult = Jsoup.parse(result);
					if(this.lastResult.getElementById("aspnetForm") != null){
						this.lastForm= this.lastResult.getElementById(properties.readProperty("cortinaweb.aspnetForm"));
					}
					this.lastUrl = url;
					return result;
				}
			}
			else if(this.statusCode.equals(StatusCode.Found.getCode())){
				Header header = response.getFirstHeader("Location");
				String newLocation = header.getValue();
				return get(newLocation);
			}


		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e){
			throw new RuntimeException(e);
		}

		return null;
	}

	public BasicCookieStore  getCookieStore() {
		return cookieStore;
	}

	public void setCookieStore(BasicCookieStore  cookieStore) {
		this.cookieStore = cookieStore;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public void addHeader(String key, String value) {
		if(this.headers == null){
			this.headers = new HashMap<String, String>();
		}

		this.headers.put(key, value);
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	public Document getLastResult() {
		return lastResult;
	}

	public void setLastResult(Document lastResult) {
		this.lastResult = lastResult;
	}

	public Element getLastForm() {
		return lastForm;
	}

	public void setLastForm(Element lastForm) {
		this.lastForm = lastForm;
	}

	public String getLastUrl() {
		return lastUrl;
	}

	public void setLastUrl(String lastUrl) {
		this.lastUrl = lastUrl;
	}
}
