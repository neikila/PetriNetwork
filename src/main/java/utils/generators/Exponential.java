package utils.generators;

import java.util.Random;

/**
 * Created by Neikila on 03.05.2016.
 */
public class Exponential implements Generator<Double> {
    private double time;
    private Random random;

    private final int limit = 10000;

    public Exponential(double time) {
        this.time = time;
        random = new Random();
    }

    public Double next() {
        return -1 * time * Math.log((double)(random.nextInt(limit - 1) + 1) / limit);
    }

    public void setTime(double time) {
        this.time = time;
    }
}
