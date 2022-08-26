package org.scraper.controller;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.scraper.model.Crawler;
import org.scraper.util.DummyUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrawlerController {

	private ApplicationProperties properties = new ApplicationProperties();
	private Crawler crawler;

	public void iniciar(Map<String, String> paramsFront){

		Logger.getLogger(getClass().getName()).log(Level.INFO, "Iniciando...");
		this.crawler = inicarCrawler();
		login(paramsFront);
		logoff();
	}

	public void login(Map<String, String> paramsFront){
		if(this.crawler == null){
			return;
		}
		Logger.getLogger(getClass().getName()).log(Level.INFO, "Login...");
		crawler.get("/PunchClockWebClient/Login.aspx");

		Map<String, String> params = new HashMap<>();

		try{
			Element form = this.crawler.getLastForm();

			Elements inputElements = form.getElementsByTag("input");

			for (Element inputElement : inputElements) {
				String key = inputElement.attr("name");
				String value = inputElement.attr("value");
				params.put(key, value);
			}

			String login = paramsFront.get("LOGIN");
			String senha = paramsFront.get("SENHA");
			String viewSenha = "";

			for(int i=0; i<senha.length(); i++){
				viewSenha += "*";
			}

			params.put("ctl00$ContentPlaceHolderMasterPage$textBoxID", login);
			params.put("ctl00$ContentPlaceHolderMasterPage$password", senha);
			params.put("ctl00$ContentPlaceHolderMasterPage$txtPassword", viewSenha);
			params.put("ctl00$ContentPlaceHolderMasterPage$hiddenClientAddress", "465c5c66-ae67-4aad-9b65-04f49915e45f.local");

			crawler.post("/PunchClockWebClient/Login.aspx", params);
			Logger.getLogger(getClass().getName()).log(Level.INFO, crawler.getLastUrl());

			if(!isLogado()){
				throw new RuntimeException("Login Failed!");
			}

			Logger.getLogger(getClass().getName()).log(Level.INFO, "Logado!");

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public void logoff(){
		Map<String, String> params = new HashMap<>();
		Logger.getLogger(getClass().getName()).log(Level.INFO, "Logoff...");

		try{
			if(this.crawler == null){
				return;
			}

			Element form = this.crawler.getLastForm();

			Elements inputElements = form.getElementsByTag("input");

			for (Element inputElement : inputElements) {
				String key = inputElement.attr("name");
				String value = inputElement.attr("value");
				params.put(key, value);
			}

			params.put("__EVENTTARGET", "ctl00$ContentPlaceHolderMasterPage$linkButtonLogout");

			this.crawler.post("/PunchClockWebClient/IronCurtain.aspx", params);
			Logger.getLogger(getClass().getName()).log(Level.INFO, crawler.getLastUrl());
			logout();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public void logout(){
		Map<String, String> params = new HashMap<>();
		Logger.getLogger(getClass().getName()).log(Level.INFO, "Logout...");

		try{
			if(this.crawler == null){
				return;
			}
			this.crawler.get("/PunchClockWebClient/logout.aspx");
			Logger.getLogger(getClass().getName()).log(Level.INFO, "Deslogado!");
			Logger.getLogger(getClass().getName()).log(Level.INFO, crawler.getLastUrl());
		}
		catch (Exception e){
			e.printStackTrace();
		}

		this.crawler = null;
	}

	public void iniciarJornada(Map<String, String> paramsFront){
		Map<String, String> params = new HashMap<>();

		Logger.getLogger(getClass().getName()).log(Level.INFO, "Iniciar Jornada...");

		if(DummyUtils.isDev()){
			return;
		}


		try{
			this.crawler = inicarCrawler();
			login(paramsFront);

			Element form = crawler.getLastForm();

			Elements inputElements = form.getElementsByTag("input");

			for (Element inputElement : inputElements) {
				String key = inputElement.attr("name");
				String value = inputElement.attr("value");
				params.put(key, value);
			}

			params.put("__EVENTTARGET", "");
			params.put("__EVENTARGUMENT", "");
			params.put("ctl00$ContentPlaceHolderMasterPage$imageButtonGetin.x", "45");
			params.put("ctl00$ContentPlaceHolderMasterPage$imageButtonGetin.y", "29");

			this.crawler.post("/PunchClockWebClient/IronCurtain.aspx", params);

			form = this.crawler.getLastForm();

			inputElements = form.getElementsByTag("input");

			for (Element inputElement : inputElements) {
				String key = inputElement.attr("name");
				String value = inputElement.attr("value");
				params.put(key, value);
			}

			params.put("__EVENTTARGET", "ctl00$ContentPlaceHolderMasterPage$dayValidation$gridView");
			params.put("__EVENTARGUMENT", "save$0");
			params.put("ctl00$ContentPlaceHolderMasterPage$dayValidation$gridView$ctl02$ctl01", "on");
			params.put("ctl00$ContentPlaceHolderMasterPage$dayValidation$gridView$ctl02$ctl05", "on");

			crawler.post("/PunchClockWebClient/DayValidationRequired.aspx", params);

			Logger.getLogger(getClass().getName()).log(Level.INFO, crawler.getLastUrl());
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally {
			logoff();
		}
	}

	public void finalizarJornada(Map<String, String> paramsFront){
		Map<String, String> params = new HashMap<>();

		Logger.getLogger(getClass().getName()).log(Level.INFO, "Finalizar Jornada...");

		if(DummyUtils.isDev()){
			return;
		}

		try{
			this.crawler = inicarCrawler();
			login(paramsFront);

			Element form = crawler.getLastForm();

			Elements inputElements = form.getElementsByTag("input");

			for (Element inputElement : inputElements) {
				String key = inputElement.attr("name");
				String value = inputElement.attr("value");
				params.put(key, value);
			}

			params.put("__EVENTTARGET", "");
			params.put("__EVENTARGUMENT", "");
			params.put("ctl00$ContentPlaceHolderMasterPage$imageButtonGetout.x", "23");
			params.put("ctl00$ContentPlaceHolderMasterPage$imageButtonGetout.y", "39");

			this.crawler.post("/PunchClockWebClient/IronCurtain.aspx", params);

			Logger.getLogger(getClass().getName()).log(Level.INFO, crawler.getLastUrl());

		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally {
			logout();
		}
	}

	public void iniciarPausa(Map<String, String> paramsFront){
		Map<String, String> params = new HashMap<>();

		Logger.getLogger(getClass().getName()).log(Level.INFO, "Iniciar Pausa...");

		if(DummyUtils.isDev()){
			return;
		}

		try{
			this.crawler = inicarCrawler();
			login(paramsFront);

			Element form = crawler.getLastForm();

			Elements inputElements = form.getElementsByTag("input");

			for (Element inputElement : inputElements) {
				String key = inputElement.attr("name");
				String value = inputElement.attr("value");
				params.put(key, value);
			}

			params.put("__EVENTTARGET", "");
			params.put("ctl00$ContentPlaceHolderMasterPage$imageButtonGetin.x", "42");
			params.put("ctl00$ContentPlaceHolderMasterPage$imageButtonGetin.y", "15");

			this.crawler.post("/PunchClockWebClient/IronCurtain.aspx", params);

			form = this.crawler.getLastForm();

			inputElements = form.getElementsByTag("input");

			for (Element inputElement : inputElements) {
				String key = inputElement.attr("name");
				String value = inputElement.attr("value");
				params.put(key, value);
			}

			params.put("__EVENTTARGET", "ctl00$ContentPlaceHolderMasterPage$pause$gridViewPause");
			params.put("__EVENTARGUMENT", "Select$1");

			this.crawler.post("/PunchClockWebClient/IronCurtain.aspx", params);

			form = this.crawler.getLastForm();

			inputElements = form.getElementsByTag("input");

			for (Element inputElement : inputElements) {
				String key = inputElement.attr("name");
				String value = inputElement.attr("value");
				params.put(key, value);
			}

			params.put("__EVENTTARGET", "");
			params.put("__EVENTARGUMENT", "");
			params.put("ctl00$ContentPlaceHolderMasterPage$pause$btnPauseOk", "OK");

			this.crawler.post("/PunchClockWebClient/IronCurtain.aspx", params);

			Logger.getLogger(getClass().getName()).log(Level.INFO, crawler.getLastUrl());
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally {
			logoff();
		}
	}

	public void finalizarPausa(Map<String, String> paramsFront){
		Map<String, String> params = new HashMap<>();

		Logger.getLogger(getClass().getName()).log(Level.INFO, "Finalizar Pausa...");

		if(DummyUtils.isDev()){
			return;
		}

		try{
			this.crawler = inicarCrawler();
			login(paramsFront);

			Element form = crawler.getLastForm();

			Elements inputElements = form.getElementsByTag("input");

			for (Element inputElement : inputElements) {
				String key = inputElement.attr("name");
				String value = inputElement.attr("value");
				params.put(key, value);
			}

			params.put("__EVENTTARGET", "");
			params.put("__EVENTARGUMENT", "");
			params.put("ctl00$ContentPlaceHolderMasterPage$imageButtonPauseFinish.x", "35");
			params.put("ctl00$ContentPlaceHolderMasterPage$imageButtonPauseFinish.y", "22");

			this.crawler.post("/PunchClockWebClient/IronCurtain.aspx", params);

			form = this.crawler.getLastForm();

			inputElements = form.getElementsByTag("input");

			for (Element inputElement : inputElements) {
				String key = inputElement.attr("name");
				String value = inputElement.attr("value");
				params.put(key, value);
			}

			params.put("__EVENTTARGET", "");
			params.put("__EVENTARGUMENT", "");

			String login = paramsFront.get("LOGIN");
			String senha = paramsFront.get("SENHA");
			String viewSenha = "";

			for(int i=0; i<senha.length(); i++){
				viewSenha += "*";
			}

			params.put("ctl00$ContentPlaceHolderMasterPage$imagePause$password$textBoxLiberateCode", login);
			params.put("ctl00$ContentPlaceHolderMasterPage$imagePause$password$textBoxLiberatePassword", senha);
			params.put("ctl00$ContentPlaceHolderMasterPage$imagePause$password$textBoxPassword", viewSenha);
			params.put("ctl00$ContentPlaceHolderMasterPage$imagePause$password$btnPasswordOK", "OK");

			crawler.post("/PunchClockWebClient/IronCurtain.aspx", params);

			Logger.getLogger(getClass().getName()).log(Level.INFO, crawler.getLastUrl());
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally {
			logoff();
			this.crawler = null;
		}
	}

	private Crawler inicarCrawler(){
		Crawler crawler = new Crawler(properties.readProperty("cortinaweb.dominio"));
		crawler.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		crawler.addHeader("Accept-Encoding", "gzip, deflate, br");
		crawler.addHeader("Accept-Language", "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7,gl;q=0.6");
		crawler.addHeader("Cache-Control", "max-age=0");
		crawler.addHeader("Connection", "keep-alive");
		crawler.addHeader("Host", "punchclock.neobpo.com.br");
		crawler.addHeader("sec-ch-ua", "\"Chromium\";v=\"104\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"104\"");
		crawler.addHeader("sec-ch-ua-mobile", "?0");
		crawler.addHeader("sec-ch-ua-platform", "\"Windows\"");
		crawler.addHeader("Sec-Fetch-Dest", "document");
		crawler.addHeader("Sec-Fetch-Mode", "navigate");
		crawler.addHeader("Sec-Fetch-Site", "none");
		crawler.addHeader("Sec-Fetch-User", "?1");
		crawler.addHeader("Upgrade-Insecure-Requests", "1");
		crawler.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");

		return crawler;
	}

	private Boolean isLogado(){
		Document document = this.crawler.getLastResult();

		List<Element> elements = document.getElementsByAttributeValue("title", "Entrada");

		return !elements.isEmpty();

	}

	public Crawler getCrawler() {
		return crawler;
	}

	public void setCrawler(Crawler crawler) {
		this.crawler = crawler;
	}
}
