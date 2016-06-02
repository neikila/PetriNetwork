package storageModel.storageDetails;

import storageModel.Product;

import java.awt.*;

/**
 * Created by neikila on 19.11.15.
 */
public class Section {
    private static int lastId = 0;
    private Point index;
    private Point size;
    private int level;
    private int id;
    private Direction possibleDirection;
    private Polygon polygon;
    private Product product;
    private int amount;
    private double maxWeightOfSection;

    public Section(Point index, Point size, int level, Direction direction,
                   int id, Polygon polygon, double maxWeightOfSection) {
        this.id = id;
        this.index = index;
        this.size = size;
        this.level = level;
        this.possibleDirection = direction;
        this.polygon = polygon;
        this.product = null;
        this.amount = 0;
        this.maxWeightOfSection = maxWeightOfSection;
    }

    public Section(Point index, Point size, int level, Direction direction, Polygon polygon, double maxWeightOfSection) {
        this(index, size, level, direction, lastId++, polygon, maxWeightOfSection);
    }

    public Point getCenter() {
        int sumx = 0;
        int sumy = 0;
        for (int i = 0; i < polygon.npoints; ++i) {
            sumx += polygon.xpoints[i];
            sumy += polygon.ypoints[i];
        }
        sumx /= polygon.npoints;
        sumy /= polygon.npoints;
        return new Point(sumx, sumy);
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public Point getIndex() {
        return index;
    }

    public Point getSize() {
        return size;
    }

    public int getLevel() {
        return level;
    }

    public Direction getPossibleDirection() {
        return possibleDirection;
    }

    public void setPossibleDirection(Direction possibleDirection) {
        this.possibleDirection = possibleDirection;
    }

    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public double getMaxWeightOfSection() {
        return maxWeightOfSection;
    }

    public int getProduct(int amount) {
        if (this.amount > amount) {
            this.amount -= amount;
            return amount;
        } else {
            amount = this.amount;
            this.amount = 0;
            product = null;
            return amount;
        }
    }

    public boolean isAcceptable(Product product, double weight) {
        if (this.product == null )
            return maxWeightOfSection > weight;
        else
            return (this.product.getId() == product.getId()) &&
                (maxWeightOfSection - product.getWeightOfUnit() * amount) > weight;
    }

    public Point getPointAccess() {
        Point from = new Point(index);
        switch (possibleDirection) {
            case Down:
                from.translate(0, -1);
                break;
            case Up:
                from.translate(0, 1);
                break;
            case Left:
                from.translate(-1, 0);
                break;
            case Right:
                from.translate(1, 0);
                break;
        }
        return from;
    }

    public void addProduct(Product product, int amount) {
        if (this.product == null || product.getId() == this.product.getId()) {
            this.product = product;
            this.amount += amount;
        }
    }
    @Override
    public String toString() {
        return "id: " + getId() + "; index[" + getIndex().x + ";" + getIndex().y + ']';
    }

    public enum Direction {
        Up,
        Down,
        Left,
        Right
    }
}
