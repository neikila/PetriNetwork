package utils.generators;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Neikila on 03.05.2016.
 */
public class Uniform implements Generator<Double> {
    private double left;
    private double right;
    private ThreadLocalRandom random;

    public Uniform(double left, double right) {
        this.left = left;
        this.right = right;
        random = ThreadLocalRandom.current();
    }

    public Double next() {
        return random.nextDouble(left, right);
    }
}
