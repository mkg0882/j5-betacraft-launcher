package uk.betacraft.auth;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.commons.httpclient.ssl.SimpleSSLTestProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.protocol.Protocol;

import org.betacraft.launcher.Lang;

import com.google.gson.Gson;

import uk.betacraft.util.WebData;

public class RequestUtil {
	private static boolean debug = false;

	public static String webDataToString(WebData data) {
		if (data.getData() != null) {
			try {
				String response = new String(data.getData(), "UTF-8");
				if (debug) System.out.println("INCOMING: " + response);
				return response;
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return null;
	}

	public static String performPOSTRequest(Request req) {
		WebData data = performRawPOSTRequest(req);
		return webDataToString(data);
	}

	public static WebData performRawPOSTRequest(Request req) {
		Protocol bchttps = new Protocol("https", new SimpleSSLTestProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", bchttps);
		HttpClient httpclient = new HttpClient();
		PostMethod httppost = new PostMethod(req.REQUEST_URL);
		try {
			for (String key : req.PROPERTIES.keySet()) {
				httppost.addRequestHeader(key, req.PROPERTIES.get(key));
			}
			// Send POST
			if (req.POST_DATA == null) {
				Gson gson = new Gson();
				String s = gson.toJson(req);
				if (debug) System.out.println("OUTGOING: " + s);
				httppost.setRequestBody(s);
			} else {
				if (debug) System.out.println("OUTGOING: " + req.POST_DATA);
				httppost.setRequestBody(req.POST_DATA);
			}
			httpclient.executeMethod(httppost);
			// Read response
			int http = httppost.getStatusCode();
			byte[] data = null;
			if (debug) System.out.println(http);
			data = readInputStream(httppost.getResponseBodyAsStream());
			httppost.releaseConnection();
			return new WebData(data, http);
		} catch (javax.net.ssl.SSLHandshakeException e) {
			e.printStackTrace();
			return new WebData(null, -2);
		} catch (Throwable t) {
			t.printStackTrace();
			return new WebData(null, -1);
		}
	}

	public static String performGETRequest(Request req) {
		WebData data = performRawGETRequest(req);
		return webDataToString(data);
	}

	public static WebData performRawGETRequest(Request req) {
		Protocol bchttps = new Protocol("https", new SimpleSSLTestProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", bchttps);
		HttpClient httpclient = new HttpClient();
		GetMethod httpget = new GetMethod(req.REQUEST_URL);
		try {
			if (debug) System.out.println("OUTCOME TO: " + req.REQUEST_URL);
			// i'm a browser C:
			httpget.addRequestHeader("User-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
			for (String key : req.PROPERTIES.keySet()) {
				httpget.addRequestHeader(key, req.PROPERTIES.get(key));
			}
			httpclient.executeMethod(httpget);
			// Read response
			int http = httpget.getStatusCode();
			byte[] data = null;
			if (debug) System.out.println(http);
			data = readInputStream(httpget.getResponseBodyAsStream());
			return new WebData(data, http);
		} catch (javax.net.ssl.SSLHandshakeException e) {
			e.printStackTrace();
			return new WebData(null, -2);
		} catch (Throwable t) {
			t.printStackTrace();
			return new WebData(null, -1);
		}
	}

	public static byte[] readInputStream(InputStream in) {
		try {
			byte[] buffer = new byte[4096];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int count = in.available();
			while ((count = in.read(buffer)) > 0) {
				baos.write(buffer, 0, count);
			}
			byte[] data = baos.toByteArray();
			return data;
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
}
