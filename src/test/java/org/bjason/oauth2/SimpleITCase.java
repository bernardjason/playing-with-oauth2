package org.bjason.oauth2;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

public class SimpleITCase {

	String code = "";
	String accessToken = "";
	String HOST = "http://127.0.0.1:8080";
	String client_id = "client_id=oauth2test";
	String client_secret = "client_secret=oauth2clientsecret";

	/*
	 * to run as junit from eclipse make sure mvn jetty:run is executed
	 */

	@Test
	public void testGetCodeAskingForFirstAndThirdResource() throws IOException {
		getCodeForApplication();
	}

	@Test
	public void testGetAccessTokenForResource1() throws IOException,
			JSONException {

		getCodeForApplication();

		getAccessTokenForResource("&scope=resource1");
	}

	@Test
	public void testGetCodeThenAccessTokenThenCallApiResources()
			throws IOException, JSONException {

		getCodeForApplication();

		getAccessTokenForResource("&scope=resource1");

		Response r;
		r = callApi("1");
		assertEquals(200, r.getStatus());

		// should fail, my access_token is scoped for 1 only
		r = callApi("3");
		assertEquals(401, r.getStatus());

	}

	@Test
	public void testGetCodeThenAccessTokenThenCallAllScopedApiResources()
			throws IOException, JSONException {

		getCodeForApplication();

		getAccessTokenForResource(null);

		Response r;
		r = callApi("1");
		assertEquals(200, r.getStatus());

		r = callApi("3");
		assertEquals(200, r.getStatus());

	}

	private Response callApi(String api) throws IOException {
		WebClient client = WebClient.create(HOST + "/api/demo/" + api);
		client.replaceHeader("Authorization", "Bearer " + accessToken);
		Response r = client.get();
		InputStream i = (InputStream) r.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(i));
		String back = br.readLine();
		System.out.println("Got back from api " + back);
		return r;
	}

	private void getAccessTokenForResource(String scope) throws IOException,
			JSONException {

		if (scope == null)
			scope = "";

		WebClient client = WebClient.create(

		HOST + "/api/token?grant_type=authorization_code&" + code + "&"
				+ client_id + "&" + client_secret + scope + "&redirect_uri=na");

		// TODO Need to find out about posting in body rather than URL

		Form f = new Form();
		f.set("grant_type", "authorization_code");
		Response r = client.post(f);
		InputStream i = (InputStream) r.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(i));

		JSONObject jsonObj = new JSONObject(br.readLine());

		accessToken = jsonObj.getString("access_token");

		System.out.println("access_token now " + accessToken);
		assertEquals(200, r.getStatus());
	}

	private void getCodeForApplication() throws IOException {
		WebClient client = WebClient.create(HOST
				+ "/api/auth?response_type=code&" + client_id + "&"
				+ client_secret
				+ "&scope=resource1true%20resource2false%20resource3true");

		Response r = client.get();
		InputStream i = (InputStream) r.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(i));
		code = br.readLine();
		System.out.println("Code now " + code);
		assertEquals(200, r.getStatus());

	}
}
