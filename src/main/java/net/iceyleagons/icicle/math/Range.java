package net.iceyleagons.icicle.math;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class Range {

    private final Random random = ThreadLocalRandom.current();
    private double min;
    private double max;

    public double getDifference() {
        return max - min;
    }

    public double random() {
        return min + getDifference() * random.nextDouble();
    }

    public static double map(double value, Range old, Range New) {
        double diff = old.getDifference();
        if (diff != 0)
            return (((value - old.getMin()) * New.getDifference()) / diff) + New.getMin();

        return value;
    }
}
