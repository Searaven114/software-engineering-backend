package com.team6.ecommerce.util;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ImageLoader {

    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/";

    //Loads product images as a map of product ids to img data, will return a map of <productId,imageByte>
    public static Map<String, byte[]> loadProductImages() {

        //Map to store images with corresponding indices
        Map<String, byte[]> images = new HashMap<>();

        try {
            images.put("1", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "1_Dell UltraSharp Monitor.jpg")));
            images.put("2", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "2_AW2723DF.jpg")));
            images.put("3", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "3_HP Envy Laptop.jpg")));
            images.put("4", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "4_Logitech MX Master 3.jpg")));
            images.put("5", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "5_MacBook Pro 16-inch.jpg")));
            images.put("6", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "6_Dell XPS 13.jpg")));
            images.put("7", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "7_Samsung Odyssey G7.jpg")));
            images.put("8", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "8_LG UltraFine 5K.jpg")));
            images.put("9", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "9_Razer DeathAdder V2.jpg")));
            images.put("10", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "10_Corsair Dark Core RGB Pro.jpg")));
            images.put("11", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "11_Lenovo ThinkPad X1 Carbon.jpg")));
            images.put("12", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "12_Acer Predator X34.jpg")));
            images.put("13", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "13_SteelSeries Rival 600.jpg")));
            images.put("14", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "14_Asus ROG Zephyrus G14.jpg")));
            images.put("15", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "15_BenQ EX3501R.jpg")));
            images.put("16", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "16_HyperX Pulsefire FPS Pro.jpg")));
            images.put("17", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "17_Microsoft Surface Laptop 4.jpg")));
            images.put("18", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "18_Gigabyte Aorus FI27Q.jpg")));
            images.put("19", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "19_Cooler Master MM710.jpg")));
            images.put("20", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "20_Acer Swift 3.jpg")));
            images.put("21", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "21_HP Spectre x360.jpg")));
            images.put("22", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "22_ASUS ROG Swift PG259QN.jpg")));
            images.put("23", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "23_Roccat Kone Pro Air.jpg")));
            images.put("24", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "24_Dell Inspiron 15 5000.jpg")));
            images.put("25", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "25_MSI Optix MAG272C.jpg")));
            images.put("26", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "26_Logitech G502 Lightspeed.jpg")));
            images.put("27", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "27_Apple MacBook Air M1.jpg")));
            images.put("28", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "28_AOC Agon AG273QCG.jpg")));
            images.put("29", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "29_Razer Naga Pro.jpg")));
            images.put("30", Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "30_Asus VivoBook S14 Comment comment.jpg")));
            images.put("666666",Files.readAllBytes(Path.of(IMAGE_DIRECTORY + "new_product.jpg")));
        } catch (IOException e) {
            log.error("Error loading images: ", e);
        }

        return images;
    }
}

