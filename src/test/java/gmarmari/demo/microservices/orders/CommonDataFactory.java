package gmarmari.demo.microservices.orders;

import gmarmari.demo.microservices.orders.api.*;
import gmarmari.demo.microservices.orders.entities.ProductOldDao;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;

public class CommonDataFactory {

    private static final Random random = new Random();

    private static final String abc = "abcdefghijklmnopqrstuvwxyz";
    private CommonDataFactory() {
        // Hide constructor
    }

    public static boolean aBoolean() {
        return random.nextBoolean();
    }

    public static int aInt() {
        return random.nextInt();
    }

    public static int aInt(int bound) {
        return random.nextInt(bound);
    }

    public static long aLong() {
        return random.nextLong();
    }

    public static double aDouble() {
        return random.nextDouble();
    }

    public static String aText() {
        LocalTime time = ZonedDateTime.now().toLocalTime();
        return "" + abc.charAt(aInt(abc.length()))
                + abc.charAt(aInt(abc.length()))
                + abc.charAt(aInt(abc.length()))
                + " "
                + abc.charAt(aInt(abc.length()))
                + abc.charAt(aInt(abc.length()))
                + abc.charAt(aInt(abc.length()));
    }

    public static String aNullableText() {
        return aBoolean() ? aText() : null;
    }



    public static ProductOldDao aProductOldDao() {
        ProductOldDao product = new ProductOldDao();
        product.setName("Milk");
        product.setBrand("Milk Maker");
        product.setColor(random.nextBoolean() ? "White" : null);
        product.setDescription(random.nextBoolean() ? "The best milk!" : null);
        product.setHeightMm(random.nextInt());
        product.setWidthMm(random.nextInt());
        product.setLengthMm(random.nextInt());
        product.setWeightGrams(random.nextInt());
        product.setPrizeEuro(random.nextDouble());
        return product;
    }

    public static ProductOldDto aProductOldDto() {
        return new ProductOldDto(
                random.nextInt(),
                "Milk",
                "Milk Maker",
                random.nextBoolean() ? "White" : null,
                random.nextBoolean() ? "The best milk!" : null,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                random.nextDouble()
        );
    }
}
