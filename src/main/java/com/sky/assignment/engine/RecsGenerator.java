package com.sky.assignment.engine;

import com.sky.assignment.model.Recommendation;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;

/**
 * Created by Pradeep Muralidharan.
 */

@Service
public class RecsGenerator {
    private static final Random random = new Random(currentTimeMillis());

    /**
     * Generate recommendations
     * @return
     */
    public  Recommendation generate() {
        long num1 = random.nextInt(7 * 1000 * 60 * 30);
        long num2 = num1 + random.nextInt(4 * 1000 * 60 * 30);
        long base = currentTimeMillis() - (7 * 1000 * 60 * 30);
        return new Recommendation(UUID.randomUUID().toString(), base + num1, base + num2);
    }
}
