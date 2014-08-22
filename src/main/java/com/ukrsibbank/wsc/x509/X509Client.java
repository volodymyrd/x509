package com.ukrsibbank.wsc.x509;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.ukrsibbank.wsc.wsdls.x509.TABLEOFZX509TEST;
import com.ukrsibbank.wsc.wsdls.x509.ZSPFNX509TEST;
import com.ukrsibbank.wsc.wsdls.x509.ZSPFNX509TESTResponse;

public class X509Client extends WebServiceGatewaySupport {

	// @Autowired
	// private PropertiesFactoryBean settings;
	@Value("#{settings['ws_url_x509']}")
	private String url;

	public ZSPFNX509TESTResponse getMessage(String m, TABLEOFZX509TEST t) {

		ZSPFNX509TEST req = new ZSPFNX509TEST();
		req.setINAME(m);
		req.setT(t);

		ZSPFNX509TESTResponse resp = (ZSPFNX509TESTResponse) getWebServiceTemplate()
				.marshalSendAndReceive(req, new SoapActionCallback(url));

		return resp;
	}
}