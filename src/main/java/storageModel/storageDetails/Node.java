package storageModel.storageDetails;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Node {
    private List<Node> neighbors;
    private Point index;
    private double time;
    private Node previous;

    public Node(Point index) {
        previous = null;
        time = -1.0;
        neighbors = new ArrayList<>();
        this.index = index;
    }

    public Node(Point index, double time) {
        this(index);
        this.time = time;
    }

    public boolean isMoreThan(double time) {
        return this.time < 0 || this.time > time;
    }

    public double getTime() {
        return time;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setToDefault() {
        time = -1.0;
        previous = null;
    }

    public Point getIndex() {
        return index;
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }

    public boolean addNeighbor(Node node) {
        boolean toAdd = !neighbors.contains(node);
        if (toAdd) {
            neighbors.add(node);
        }
        return toAdd;
    }

    public void replaceNeighbor(Node toDelete, Node toAdd) {
        neighbors.remove(toDelete);
        neighbors.add(toAdd);
    }

    public boolean isNeighborTo(Node node) {
        return neighbors.contains(node);
    }

    @Override
    public String toString() {
        return "Node: [" + index.x + ';' + index.y + "] Neighbors: " + neighbors.size();
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            if (o1.time > o2.time)
                return 1;
            if (o1.time < o2.time)
                return -1;
            return 0;
        }
    }

    public void removeNeighbor(Node node) {
        neighbors.remove(node);
    }

    public double distance(Node node) {
        return index.distance(node.index);
    }
}