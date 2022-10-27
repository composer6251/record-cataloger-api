package com.recordcataloguer.recordcataloguer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages={
		"com.recordcataloguer.recordcataloguer", "com.recordcataloguer.recordcataloguer.application"})
public class RecordCataloguerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordCataloguerApplication.class, args);
	}
}
