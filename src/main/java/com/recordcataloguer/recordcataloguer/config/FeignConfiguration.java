//package com.recordcataloguer.recordcataloguer.config;
//
//import feign.Logger;
//import feign.codec.Encoder;
//import feign.form.FormEncoder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class FeignConfiguration {
//
//    @Bean
//    Logger.Level feignLoggerLevel() {
//        return Logger.Level.FULL;
//    }
//
//    @Bean
//    public Encoder feignFormEncoder() {
//        List<Integer> nums = new ArrayList<Integer>();
//        nums.add(1);
//        return new FormEncoder();
//    }
//
//}
