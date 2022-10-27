//package com.recordcataloguer.recordcataloguer.helpers.imagecracker;
//
//import org.bytedeco.javacpp.tesseract;
//
//import java.io.File;
//
//public class ImageCracker {
//
//    public static String crackImage(String filePath) {
//        File imageFile = new File(filePath);
//        ITesseract instance = new tesseract.Tesseract();
//        try {
//            String result = instance.doOCR(imageFile);
//            return result;
//        } catch (TesseractException e) {
//            System.err.println(e.getMessage());
//            return "Error while reading image";
//        }
//    }
//}
