package com.ukrsibbank.wsc.simple;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
	public static void main(String[] args) {
		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SimpleConfig.class);

		SimpleClient simpleClient = ctx.getBean(SimpleClient.class);

		System.out.println(simpleClient.getMessage("Spring WS"));
	}
}