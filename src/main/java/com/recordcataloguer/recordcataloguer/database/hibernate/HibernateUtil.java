package com.recordcataloguer.recordcataloguer.database.hibernate;

import com.recordcataloguer.recordcataloguer.dto.discogs.Community;
import com.recordcataloguer.recordcataloguer.entity.AlbumEntity;
import com.recordcataloguer.recordcataloguer.entity.TestEnt;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

@Slf4j
public class HibernateUtil {
    // Flow:
    //  Spring looks at property files to get connection info
    //  Tests connection to DB
    // Configuration Options:
    //  1. Add to application.properties file
    //  2. Use default hibernate.cfg.xml (either create yourself or utilize the built in Configure.configure()....as shown in the first method
    //  3. Create a custom file hibernate.properties(in this example) and add it as in last method
    // SessionFactory to get Session which needs
    // Properties which needs
    // Service Registry
    // MetaDataSources

    private HibernateUtil(){}

    @Autowired
    private ResourceLoader resourceLoader;

    private static String PROPERTY_FILE_NAME = "images/hibernate.properties";

    // Public method to call buildSessionFactory
    public static SessionFactory getSessionFactory() {
        ServiceRegistry serviceRegistry = null;
        try {
            serviceRegistry = configureServiceRegistry();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if(serviceRegistry == null) throw new HibernateException("");

        return makeSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactoryXml() {
        return buildSessionFactory();
    }

    public static void getConfigFile() {
        Configuration configuration = new Configuration();
        String path = "file:/Users/david/Coding Projects/record-cataloguer-api/src/main/resources/hibernate.properties";
//        Resource resource = resourceLoader.getResource(path);

        configuration.configure(new File(
                "file:/Users/david/Coding Projects/record-cataloguer-api/src/main/resources/hibernate.properties"
        ));
        log.info("");
    }

    @SneakyThrows
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            SessionFactory sessionFactory =  new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(TestEnt.class)
                    .addAnnotatedClass(AlbumEntity.class)
                    .buildSessionFactory();

            return sessionFactory;
        }
        catch (Exception  ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        // todo: Figure out how to use current thread so the file path is right. I think it's using the parent thread which is the root directory path to target/classes
        URL propertiesURL = Thread.currentThread()
                .getContextClassLoader()
                .getResource(StringUtils.defaultString(PROPERTY_FILE_NAME, "hibernate.properties"));
//        URL propertiesURL = new URL("file:../../../../../../../main/resources/hibernate.properties");
        try {
            assert propertiesURL != null;
            try (FileInputStream inputStream = new FileInputStream(propertiesURL.getFile())) {
                properties.load(inputStream);
            }
        }
        catch (NullPointerException ex){
            log.info("Error creating Hibernate Properties {}", ex.getMessage());
        }
        return properties;
    }

    private static ServiceRegistry configureServiceRegistry() throws IOException {
        Properties properties = getProperties();
        return new StandardServiceRegistryBuilder().applySettings(properties)
                .build();
    }

    // MetadataSources is used to tell Hibernate what your entities are???
    private static SessionFactory makeSessionFactory(ServiceRegistry serviceRegistry) {
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);

        // Lazily add package if the .properties file is used as opposed to .xml file
        metadataSources.addPackage("com.recordcataloguer.recordcataloguer.entity");
        metadataSources.addAnnotatedClass(AlbumEntity.class);
        metadataSources.addAnnotatedClass(Community.class);

        Metadata metadata = metadataSources.getMetadataBuilder()
                .build();

        return metadata.getSessionFactoryBuilder()
                .build();
    }
}