//package com.recordcataloguer.recordcataloguer.util;
//
//import com.google.api.client.util.Value;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Component;
//
//@Component
//@Log4j2
//public class AppPropertiesUtil {
//
//    @Value("${mongodb+srv}")
//    private static String mongoDbConnectionString;
//    public static void loadAppProps() {
//
//        String rootPath = Thread.currentThread()
//                .getContextClassLoader()
//                .getResource("")
//                .getPath();
//
//        String appConfigPath = rootPath + "application.properties";
//    }
//
//    public static void getMongoDbConnection() {
//        log.info("mongoDb connection String {}", mongoDbConnectionString);
//    }
//}
