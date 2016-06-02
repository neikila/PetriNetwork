package storageModel.storageDetails;

import storageModel.Storage;
import utils.Output;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by neikila on 21.11.15.
 */
public class GraphOfWays {
    public final double DELAY_AT_THE_CORNER = 3.0;
    public final double DELAY_PER_METER = 1.0;
    private Map<Point, Node> nodes;
    private Point step;
    private List<Way> ways;
    private Output out;

    public GraphOfWays(Storage storage, Output out) {

        this.out = out;

        nodes = new HashMap<>();
        step = storage.getBox();
        Point offset = new Point(step.x / 2, step.y / 2);
        int left;
        int right;
        int top;
        int bottom;

        List<Point> points = storage.getBoundPoints();
        left = right = points.get(0).x;
        top = bottom = points.get(0).y;

        for (int i = 1; i < points.size(); ++i) {
            Point temp = points.get(i);
            if (temp.x < left)
                left = temp.x;
            if (temp.x > right)
                right = temp.x;
            if (temp.y < bottom)
                bottom = temp.y;
            if (temp.y > top)
                top = temp.y;
        }

        Point size = new Point((top - bottom) / step.y, (right - left) / step.y);

        for (int y = 0; y < size.y; ++y) {
            for (int x = 0; x < size.x; ++x) {
                Point temp = new Point(offset.x + step.x * x, offset.y + step.y * y);
                if(storage.isEmpty(temp)) {
                    Point point = new Point(x, y);
                    nodes.put(point, new Node(point));
                }
            }
        }

        nodes.values().forEach(this::setNeighbor);
        minimise();
        ways = new ArrayList<>();
        updateWays();
    }

    private double count(Node from, Node to) {
        double result = 0.0;
        if (from.getPrevious() != null && !isOnTheLine(from.getPrevious(), from, to)) {
            result += DELAY_AT_THE_CORNER;
        }
        final int MM_IN_M = 1000;
        result += from.distance(to) * step.x / MM_IN_M * DELAY_PER_METER;
        return result;
    }

    public double getTimeBetween(Point fromPoint, Point toPoint) {
        if (fromPoint.equals(toPoint))
            return 0.0;

        Node from = nodes.get(fromPoint);
        Node to = nodes.get(toPoint);
        boolean fromFlag = false;
        if (from == null) {
            fromFlag = true;
            from = addNode(fromPoint);
        }
        boolean toFlag = false;
        if (to == null) {
            toFlag = true;
            to = addNode(toPoint);
        }
        if (from == null || to == null)
            return -1.0;

        PriorityQueue<Node> front = new PriorityQueue<>(new Node.NodeComparator());
        from.setTime(0.0);
        front.add(from);
        while (!front.isEmpty()) {
            Node min = front.poll();
            if (min != to && to.isMoreThan(min.getTime())) {
                for (Node neighbor : min.getNeighbors()) {
                    double delta = count(min, neighbor);
                    double newTime = min.getTime() + delta;
                    if (neighbor.isMoreThan(newTime)) {
                        if (front.contains(neighbor))
                            front.remove(neighbor);
                        neighbor.setTime(newTime);
                        neighbor.setPrevious(min);
                        front.add(neighbor);
                    }
                }
            }
        }

        if (fromFlag)
            removeNode(from);
        if (toFlag)
            removeNode(to);

        nodes.values().forEach(Node::setToDefault);

        return to.getTime();
    }

    private Node addNode(Point point) {
        Node node = new Node(point);
        int currentIndex = -1;
        if (nodes.get(node.getIndex()) == null) {
            nodes.put(node.getIndex(), node);
            for (int i = 0; i < ways.size(); ++i) {
                Way way = ways.get(i);
                if (way.interfereWith(node.getIndex())) {
                    currentIndex = i;
                    break;
                }
            }
            if (currentIndex != -1) {
                Way way = ways.get(currentIndex);
                ways.remove(currentIndex);
                ways.add(new Way(way.one, node));
                ways.add(new Way(node, way.two));
                way.one.replaceNeighbor(way.two, node);
                way.two.replaceNeighbor(way.one, node);
                node.addNeighbor(way.one);
                node.addNeighbor(way.two);
            } else {
                node = null;
            }
        }
        return node;
    }

    private void removeNode(Node node) {
        Node first = null;
        Node second = null;
        for (int i = 0; i < ways.size(); ++i) {
            Way temp = ways.get(i);
            if (temp.one == node) {
                if (first == null)
                    first = temp.two;
                else
                    second = temp.two;
                ways.remove(i);
                --i;
            }
            if (temp.two == node){
                if (first == null)
                    first = temp.one;
                else
                    second = temp.one;
                ways.remove(i);
                --i;
            }
            if (second != null)
                break;
        }
        if (first != null && second != null) {
            ways.add(new Way(first, second));
            first.replaceNeighbor(node, second);
            second.replaceNeighbor(node, first);
            nodes.remove(node.getIndex());
        } else {
            out.println("Critical error searching the way");
            System.exit(-2);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        nodes.values().forEach(el -> stringBuilder.append(el).append('\n'));
        return stringBuilder.toString();
    }

    private void updateWays() {
        ways.clear();
        for (Node node: nodes.values()) {
            List<Node> list = node.getNeighbors();
            for (Node el: list) {
                Way newWay = new Way(node, el);
                if (!ways.contains(newWay)) {
                    ways.add(newWay);
                }
            }
        }
    }

    private void setNeighbor(Node el) {
        int x = el.getIndex().x;
        int y = el.getIndex().y;
        Node neighbor;
        if ((neighbor = nodes.get(new Point(x - 1, y))) != null) {
            el.addNeighbor(neighbor);
            neighbor.addNeighbor(el);
        }
        if ((neighbor = nodes.get(new Point(x, y - 1))) != null) {
            el.addNeighbor(neighbor);
            neighbor.addNeighbor(el);
        }
    }

    private void minimise() {
        List<Node> listValues = new ArrayList<>(nodes.values());
        listValues.stream().filter(node -> node.getNeighbors().size() == 2).forEach(node -> {
            List<Node> list = node.getNeighbors();
            Node left = list.get(0);
            Node right = list.get(1);
            if (isOnTheLine(left, node, right)) {
                left.replaceNeighbor(node, right);
                right.replaceNeighbor(node, left);
                nodes.remove(node.getIndex());
            }
        });
    }

    private boolean isOnTheLine(Point one, Point two, Point three) {
        if (one.x == two.x && two.x == three.x)
            return true;
        if (one.y == two.y && two.y == three.y)
            return true;
        return false;
    }

    private boolean isOnTheLine(Node one, Node two, Node three) {
        return isOnTheLine(one.getIndex(), two.getIndex(), three.getIndex());
    }

    protected class Way{
        private Node one;
        private Node two;

        public Way(Node one, Node two) {
            this.one = one;
            this.two = two;
        }

        public boolean interfereWith(Point point) {
            Point one = this.one.getIndex();
            Point two = this.two.getIndex();
            return Math.abs(one.x - two.x) == Math.abs(one.x - point.x) + Math.abs(point.x - two.x) && one.x != two.x &&
                    one.y == point.y ||
                    Math.abs(one.y - two.y) == Math.abs(one.y - point.y) + Math.abs(point.y - two.y) && one.y != two.y &&
                    one.x == point.x;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Way) {
                Way way = (Way)obj;
                return one == way.one && two == way.two || one == way.two && two == way.one;
            } else
                return false;
        }
    }
}
