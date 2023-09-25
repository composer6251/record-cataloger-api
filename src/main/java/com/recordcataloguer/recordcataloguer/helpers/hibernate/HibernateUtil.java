package com.recordcataloguer.recordcataloguer.helpers.hibernate;

import com.recordcataloguer.recordcataloguer.dto.discogs.response.Album;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.Community;
import com.recordcataloguer.recordcataloguer.entity.AlbumEntity;
import com.recordcataloguer.recordcataloguer.entity.TestEnt;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class HibernateUtil {

    private HibernateUtil(){}

    @Autowired
    private ResourceLoader resourceLoader;

    private static SessionFactory sessionFactory;
    private static Session session;

    private static String PROPERTY_FILE_NAME = "hibernate.properties";

    /***
     * Controller method to build session factory, session, and persist albums to DB
     * @param albums
     * @return void
     * @throws IOException
     */
    public static void persistAlbumsToDBController(List<Album> albums) {
        // Get session from sessionFactory
        session = getSession();
        List<AlbumEntity> albumEntities = buildEntitiesFromAlbums(albums);
        // Pass albums to method that will persist them to DB
        persistAlbumsToDB(albumEntities);
    }

    public static List<AlbumEntity> buildEntitiesFromAlbums(List<Album> albums) {

        return albums
                .stream()
                .filter(a -> !Objects.equals(a.getCatno(), ""))
                .map(AlbumEntity::buildAlbumEntityFromAlbum)
                .collect(Collectors.toList());
    }

    private static void persistAlbumsToDB(List<AlbumEntity> albums) {
        session.beginTransaction();

        albums.forEach(r -> {
            try {
                log.info("Inserting record {} {}", r.getCatno(), r.getTitle());
                session.persist(r);
                session.flush();
            } catch (Exception exception) {
                log.error("Error inserting record {} {} \n {}", r.getCatno(), r.getTitle(), exception.getMessage());
                exception.printStackTrace();
            }
        });
        session.getTransaction().commit();

        shutdown();
    }


    @SneakyThrows
    private static SessionFactory buildSessionFactory() {
        if(sessionFactory != null) return sessionFactory;

        try {
            // Create the SessionFactory from hibernate.cfg.xml, and add annotated classes
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(TestEnt.class)
                    .addAnnotatedClass(AlbumEntity.class)
                    .buildSessionFactory();
        }
        catch (Exception  ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Session getSession() {
        // If there's an active sessionFactory, return the current session
        if(sessionFactory != null) return sessionFactory.getCurrentSession();

        return buildSessionFactory().openSession();
    }

    public static void shutdown() {
        // Close caches and connection pools
        session.close();
    }

    /***************************************************************************/
    /*******TODO: UNIMPLEMENTED Using the default hibernate.cfg.xml file********/
    private static SessionFactory getSessionFactoryXml() {
        return buildSessionFactory();
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

    public static void getConfigFile() {
        Configuration configuration = new Configuration();
        String path = "file:/Users/david/Coding Projects/record-cataloguer-api/src/main/resources/hibernate.properties";
//        Resource resource = resourceLoader.getResource(path);

        configuration.configure(new File(
                "file:/Users/david/Coding Projects/record-cataloguer-api/src/main/resources/hibernate.properties"
        ));
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


//    private static ServiceRegistry configureServiceRegistry() throws IOException {
//        Properties properties = getProperties();
//        return new StandardServiceRegistryBuilder().applySettings(properties)
//                .build();
//    }
//
//    /***
//     * Function to get session factory
//     * @return
//     */
//    private static SessionFactory getSessionFactory() {
//        ServiceRegistry serviceRegistry = null;
//        try {
//            serviceRegistry = configureServiceRegistry();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        if(serviceRegistry == null) throw new HibernateException("");
//
//        return makeSessionFactory(serviceRegistry);
//    }

}
