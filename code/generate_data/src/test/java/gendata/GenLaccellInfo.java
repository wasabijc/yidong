package gendata;

import java.util.Random;
import java.util.UUID;

public class GenLaccellInfo {
    public static void main(String[] args) {
        double lonMin = 113.298363;
        double latMin = 23.097534;
        double lonMax = 113.445805;
        double latMax = 23.246867;

        Random random = new Random();

        for (int i = 0; i < 2000; i++) {
            double lon = lonMin + (lonMax - lonMin) * random.nextDouble();
            double lat = latMin + (latMax - latMin) * random.nextDouble();

            System.out.println(getYin(i)+"|" + lat + "|" + lon);
        }

    }
    static String getYin(Integer number){


        for (int i = 2; i <= number / 2; i++) {
            if (number % i == 0) {
                int factor1 = i;
                int factor2 = number / i;
                return String.format("%05d", factor1)+"_"+String.format("%05d", factor2);

            }
        }
        return "0001_"+String.format("%05d", number);
    }

}
