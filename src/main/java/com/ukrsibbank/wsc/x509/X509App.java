package com.ukrsibbank.wsc.x509;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ukrsibbank.wsc.wsdls.x509.TABLEOFZX509TEST;
import com.ukrsibbank.wsc.wsdls.x509.ZSPFNX509TESTResponse;
import com.ukrsibbank.wsc.wsdls.x509.ZX509TEST;

public class X509App {
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				X509Config.class);

		X509Client simpleClient = ctx.getBean(X509Client.class);

		TABLEOFZX509TEST t = new TABLEOFZX509TEST();

		ZSPFNX509TESTResponse resp = simpleClient.getMessage("Spring WS", t);
		System.out.println(resp.getEMESSAGE());

		for (ZX509TEST e : resp.getT().getItem()) {
			System.out.println(e.getP1());
			System.out.println(e.getP2());
			System.out.println(e.getP3());
		}
	}
}