package utils;

import java.util.Random;

/**
 * Created by neikila on 31.10.15.
 */
public class RandomGenerator {
    private Random random;

    public RandomGenerator() {
        random = new Random();
    }

    public double getTime(double leftBound, double delta) {
        return random.nextDouble() % delta + leftBound;
    }

    public double getTime(double delta) {
        return random.nextDouble() % delta;
    }

    public double getMiddleTime(double middle, double delta) {
        return random.nextDouble() % delta + middle - delta / 2;
    }

    public double fiftyFifty() {
        return random.nextInt(2);
    }
}
