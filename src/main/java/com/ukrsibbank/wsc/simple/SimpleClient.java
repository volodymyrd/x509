package com.ukrsibbank.wsc.simple;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.ukrsibbank.wsc.wsdls.ZSPFNSIMPLETEST;
import com.ukrsibbank.wsc.wsdls.ZSPFNSIMPLETESTResponse;

public class SimpleClient extends WebServiceGatewaySupport {

	@Value("#{settings['ws_url_x509']}")
	private String url;
	
	public String getMessage(String m) {
		
//		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//				return null;
//			}
//
//			public void checkClientTrusted(
//					java.security.cert.X509Certificate[] certs, String authType) {
//			}
//
//			public void checkServerTrusted(
//					java.security.cert.X509Certificate[] certs, String authType) {
//			}
//		} };
//
//		HostnameVerifier verifier = new HostnameVerifier() {
//			public boolean verify(String string, SSLSession sSLSession) {
//				return true;
//			}
//		};
//
//		try {
//			SSLContext sc = SSLContext.getInstance("SSL");
//			sc.init(null, trustAllCerts, new java.security.SecureRandom());
//			HttpsURLConnection
//					.setDefaultSSLSocketFactory(sc.getSocketFactory());
//			HttpsURLConnection.setDefaultHostnameVerifier(verifier);
//		} catch (Exception e) {
//		}
		
		ZSPFNSIMPLETEST req = new ZSPFNSIMPLETEST();
		req.setINAME(m);
		
		ZSPFNSIMPLETESTResponse resp = (ZSPFNSIMPLETESTResponse) getWebServiceTemplate()
				.marshalSendAndReceive(
						req,
						new SoapActionCallback(url));

		
		return resp.getEMESSAGE();
	}
}