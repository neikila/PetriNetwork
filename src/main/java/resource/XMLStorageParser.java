package resource;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import storageModel.storageDetails.Barrier;
import storageModel.storageDetails.Rack;
import storageModel.storageDetails.Section;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by neikila on 19.11.15.
 */
public class XMLStorageParser extends XMLParser {
    public static final String BOUNDS = "bounds";
    public static final String BARRIERS = "barriers";
    public static final String RACKS = "racks";
    public static final String ENTRANCE = "entrance";
    public static final String EXIT = "exit";

    public XMLStorageParser(String fileName) {
        super(fileName);
    }

    public List<Barrier> getBarriers() {
        return getArrayOfSomethingFromElement(root, BARRIERS, this::getBarrier);
    }

    public List<Point> getWallPoints() {
        return getArrayOfSomethingFromElement(root, BOUNDS, this::getPoint);
    }

    public List<Rack> getRacks() {
        return getArrayOfSomethingFromElement(root, RACKS, this::getRack);
    }

    public List<Point> getEntranceBounds() {
        return getArrayOfSomethingFromElement(root, ENTRANCE, this::getPoint);
    }

    public List<Point> getExitBounds() {
        return getArrayOfSomethingFromElement(root, EXIT, this::getPoint);
    }

    private Barrier getBarrier(Element node) {
        Polygon polygon = getPolygon((Element)node.getElementsByTagName("form").item(0));
        NodeList nList = node.getElementsByTagName("position");
        Point coordinate = null;
        if (nList.getLength() == 1) {
            coordinate = getPoint((Element) nList.item(0));
        }
        return new Barrier(coordinate, polygon);
    }

    private Polygon getPolygon(Element node) {
        NodeList nList = node.getChildNodes();
        int len = countPoints(nList);
        int x[] = new int[len];
        int y[] = new int[len];
        int counter = 0;
        for (int i = 0; i < nList.getLength(); ++i) {
            if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Point temp = getPoint((Element)nList.item(i));
                x[counter] = temp.x;
                y[counter] = temp.y;
                ++counter;
            }
        }
        return new Polygon(x, y, len);
    }

    private Rack getRack(Element el) {
        Point position = getPoint((Element)el.getElementsByTagName("position").item(0));
        Point size = getPoint((Element)el.getElementsByTagName("size").item(0));
        int levels = Integer.parseInt(el.getElementsByTagName("levels").item(0).getTextContent());
        double maxWeightPerSection = Double.parseDouble(el.getElementsByTagName("maxWeightPerSection").item(0).getTextContent());
//        Point sectionSize = getPoint((Element)el.getElementsByTagName("sectionSize").item(0));
        Point sectionSize = new Point(2000, 2000);
        NodeList direction = el.getElementsByTagName("direction");
        Section.Direction possibleDirection = null;
        if (direction.getLength() > 0) {
            possibleDirection = Section.Direction.valueOf(direction.item(0).getTextContent());
        }

        return new Rack(position, size, levels, maxWeightPerSection, sectionSize, possibleDirection);
    }
}
