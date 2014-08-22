package com.ukrsibbank.wsc.simple;

import java.io.IOException;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
public class SimpleConfig {

	@Bean
	public PropertiesFactoryBean settings() throws IOException {
		PropertiesFactoryBean bean = new PropertiesFactoryBean();
		bean.setLocation(new ClassPathResource("META-INF/settings.properties"));
		return bean;
	}

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.ukrsibbank.wsc.wsdls");

		return marshaller;
	}

	@Bean
	public UsernamePasswordCredentials credentials(
			PropertiesFactoryBean settings) throws IOException {
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				settings.getObject().getProperty("user_name"), settings
						.getObject().getProperty("user_psw"));

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
	public SimpleClient simpleClient(Jaxb2Marshaller marshaller,
			HttpComponentsMessageSender messageSender,
			PropertiesFactoryBean settings) throws IOException {
		SimpleClient client = new SimpleClient();
		client.setDefaultUri(settings.getObject().getProperty("ws_url_simple"));
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		client.setMessageSender(messageSender);

		return client;
	}
}