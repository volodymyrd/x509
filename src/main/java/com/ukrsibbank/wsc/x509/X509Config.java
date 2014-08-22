package com.ukrsibbank.wsc.x509;

import java.io.IOException;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.components.crypto.Crypto;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j.support.CryptoFactoryBean;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
public class X509Config {
	@Bean
	public PropertiesFactoryBean settings() throws IOException {
		PropertiesFactoryBean bean = new PropertiesFactoryBean();
		bean.setLocation(new ClassPathResource("META-INF/settings.properties"));
		return bean;
	}

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.ukrsibbank.wsc.wsdls.x509");

		return marshaller;
	}

	@Bean
	public UsernamePasswordCredentials credentials() {
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				"P00000335", "");

		return credentials;
	}

	@Bean
	public HttpComponentsMessageSender messageSender(
			UsernamePasswordCredentials credentials) {

		HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
		messageSender.setCredentials(credentials);

		return messageSender;
	}

	@Bean
	public CryptoFactoryBean signatureCrypto(PropertiesFactoryBean settings)
			throws IOException {

		CryptoFactoryBean validationSignatureCrypto = new CryptoFactoryBean();
		validationSignatureCrypto.setKeyStoreLocation(new ClassPathResource(
				settings.getObject().getProperty("keystore_path")));
		validationSignatureCrypto.setKeyStorePassword(settings.getObject()
				.getProperty("keystore_psw"));
		// validationSignatureCrypto.setDefaultX509Alias("uadx509client");

		return validationSignatureCrypto;
	}

	@Bean
	public ClientInterceptor sigInterceptor(Crypto signatureCrypto,
			PropertiesFactoryBean settings) throws IOException {
		Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
		interceptor.setSecurementActions("Signature");
		interceptor.setSecurementSignatureKeyIdentifier("DirectReference");
		// interceptor.setValidationActions("NoSecurity");
		interceptor.setSecurementPassword(settings.getObject().getProperty(
				"private_key_psw"));
		interceptor.setSecurementUsername(settings.getObject().getProperty("private_key_alias"));
		interceptor.setSecurementSignatureCrypto(signatureCrypto);
		interceptor.setSecurementPasswordType(WSConstants.PASSWORD_TEXT);
		interceptor
				.setSecurementSignatureParts("Body; {}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp");
		return interceptor;
	}

	@Bean
	public ClientInterceptor timeInterceptor(Crypto signatureCrypto) {
		Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
		interceptor.setSecurementActions("Timestamp");
		interceptor.setTimestampPrecisionInMilliseconds(true);
		// interceptor.setValidationSignatureCrypto(signatureCrypto);
		return interceptor;
	}

	@Bean
	public X509Client x509Client(Jaxb2Marshaller marshaller,
			HttpComponentsMessageSender messageSender,
			ClientInterceptor sigInterceptor,
			ClientInterceptor timeInterceptor, PropertiesFactoryBean settings)
			throws IOException {

		X509Client client = new X509Client();
		client.setDefaultUri(settings.getObject().getProperty("ws_url_x509"));
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		// client.setMessageSender(messageSender);
		ClientInterceptor[] interceptors = new ClientInterceptor[2];
		interceptors[0] = timeInterceptor;
		interceptors[1] = sigInterceptor;
		client.setInterceptors(interceptors);
		return client;
	}
}