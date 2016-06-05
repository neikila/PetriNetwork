package storageModel.storageDetails;

import java.awt.*;

/**
 * Created by neikila on 19.11.15.
 */
public class Rack {
    private Point coordinate;
    private Point size;
    private int levels;
    private double maxWeightPerSection;
    private Point sectionSize;
    private Section.Direction direction;

    public Rack(Point coordinate, Point size, int levels, double maxWeightPerSection, Point sectionSize) {
        this(coordinate, size,levels, maxWeightPerSection, sectionSize, null);
    }

    public Rack(Point coordinate, Point size, int levels,
                double maxWeightPerSection, Point sectionSize, Section.Direction direction) {
        this.coordinate = coordinate;
        this.size = size;
        this.levels = levels;
        this.maxWeightPerSection = maxWeightPerSection;
        this.sectionSize = sectionSize;
        this.direction = direction;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public Point getSize() {
        return size;
    }

    public int getLevel() {
        return levels;
    }

    public double getMaxWeightPerSection() {
        return maxWeightPerSection;
    }

    public Point getSectionSize() {
        return sectionSize;
    }

    public Section.Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return String.format("Coord: %d:%d; Size = %d:%d", coordinate.x, coordinate.y, size.x, size.y) +
                String.format("Levels: %d, maxWeight: %f ", levels, maxWeightPerSection) +
                direction.toString();
    }
}
