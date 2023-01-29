package com.recordcataloguer.recordcataloguer;

import com.recordcataloguer.recordcataloguer.database.hibernate.HibernateUtil;
import com.recordcataloguer.recordcataloguer.entity.AlbumEntity;
import com.recordcataloguer.recordcataloguer.entity.TestEnt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@EnableFeignClients
@SpringBootApplication(scanBasePackages={
		"com.recordcataloguer.recordcataloguer", "com.recordcataloguer.recordcataloguer.application"})
@Slf4j
public class RecordCataloguerApplication {

	@Autowired
	private ResourceLoader resourceLoader;

	public static void main(String[] args) {
		SpringApplication.run(RecordCataloguerApplication.class, args);
		// create and load default properties
//		Properties defaultProps = new Properties();
//		FileInputStream in = null;
//		try {
//			in = new FileInputStream("src/main/resources/application-dev.yaml");
//			defaultProps.load(in);
//			in.close();
//		} catch (FileNotFoundException e) {
//			log.error("EXCEPTION LOADING PROJECT CONFIG! NO FILE FOUND {}", e.getMessage());
//			e.printStackTrace();
//		} catch (IOException e) {
//			log.error("EXCEPTION LOADING PROJECT CONFIG! {}", e.getMessage());
//			e.printStackTrace();
//		}
//		log.info(String.valueOf(defaultProps));
		String cp = System.getProperty("java.library.path");
		String[] cpSplit = StringUtils.split(cp, ":");
		System.out.println(System.getProperty("java.library.path"));
//		HibernateTransaction.insert();


HibernateUtil.getSessionFactoryXml();
SessionFactory sessionFactory = HibernateUtil.getSessionFactoryXml();
		Session session = sessionFactory.openSession();
		AlbumEntity album = new AlbumEntity();
//		album.setCatno("abc-123");
		TestEnt test = new TestEnt();
		session.beginTransaction();

		session.persist(test);
		session.persist(album);


	}
}
