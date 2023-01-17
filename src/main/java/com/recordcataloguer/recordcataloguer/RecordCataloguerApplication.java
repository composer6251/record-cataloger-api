package com.recordcataloguer.recordcataloguer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@EnableFeignClients
@SpringBootApplication(scanBasePackages={
		"com.recordcataloguer.recordcataloguer", "com.recordcataloguer.recordcataloguer.application"})
@Slf4j
public class RecordCataloguerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordCataloguerApplication.class, args);
		// create and load default properties
		Properties defaultProps = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream("src/main/resources/application-dev.yaml");
			defaultProps.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			log.error("EXCEPTION LOADING PROJECT CONFIG! NO FILE FOUND {}", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("EXCEPTION LOADING PROJECT CONFIG! {}", e.getMessage());
			e.printStackTrace();
		}
		log.info(String.valueOf(defaultProps));

	}
}
