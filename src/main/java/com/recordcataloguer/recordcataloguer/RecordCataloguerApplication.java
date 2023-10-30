package com.recordcataloguer.recordcataloguer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.io.ResourceLoader;

@EnableFeignClients
@SpringBootApplication(scanBasePackages={
		"com.recordcataloguer.recordcataloguer", "com.recordcataloguer.recordcataloguer.application"})
@Slf4j
public class RecordCataloguerApplication {

	@Autowired
	private ResourceLoader resourceLoader;

	public static void main(String[] args) {
		SpringApplication.run(RecordCataloguerApplication.class, args);
	}
}
