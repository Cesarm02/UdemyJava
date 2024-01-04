package com.example.best_travel.util;

import java.time.LocalDateTime;
import java.util.Random;

public class BestTravelUtil {

    private static final Random random = new Random();

    public static LocalDateTime getRandomSoon(){
        int randomHours = random.nextInt(5 - 2) + 2;
        LocalDateTime now = LocalDateTime.now();
        return now.plusHours(randomHours);
    }


    public static LocalDateTime getRandomLater(){
        int randomHours = random.nextInt(12 - 6) + 2;
        LocalDateTime now = LocalDateTime.now();
        return now.plusHours(randomHours);
    }

}
