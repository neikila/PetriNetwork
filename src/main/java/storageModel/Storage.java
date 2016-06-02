package storageModel;

import main.Settings;
import resource.XMLStorageParser;
import storageModel.storageDetails.*;
import utils.Output;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by neikila on 19.11.15.
 */
public class Storage {
    private List<Point> boundPoints;
    private List<Barrier> barriers;
    private List<Section> sections;
    private Point box;
    private GraphOfWays graph;
    private Gates entrance;
    private Gates exit;

    public Storage(Settings settings) {
        XMLStorageParser storageSettings = settings.getStorageSettings();
        box = new Point(2000, 2000);
        this.boundPoints = storageSettings.getWallPoints();
        this.barriers = storageSettings.getBarriers();
        this.sections = new ArrayList<>();
        setSections(storageSettings.getRacks());

        graph = new GraphOfWays(this, settings.getOutput());
        List<Point> points = storageSettings.getEntranceBounds();
        List<Point> entrancePoints = new ArrayList<>();
        // TODO захардкожено располоение горизонтальное
        for (int x = points.get(0).x; x < points.get(1).x; x += box.x) {
            entrancePoints.add(new Point(x / box.x, 6));
        }
        entrance = new Gates(entrancePoints);

        points = storageSettings.getExitBounds();
        List<Point> exitPoints = new ArrayList<>();
        for (int x = points.get(0).x; x < points.get(1).x; x += box.x) {
            exitPoints.add(new Point(x / box.x, 0));
        }
        exit = new Gates(exitPoints);
    }

    public Point getEntrancePoint() {
        return entrance.getRandomField();
    }

    public Point getExitPoint() {
        return exit.getRandomField();
    }

    public Section findSectionForProduct(Product product, double weight) {
        List<Section> possibleSections = new ArrayList<>();
        for (Section section: sections) {
            if (section.isAcceptable(product, weight)) {
                possibleSections.add(section);
            }
        }
        if (possibleSections.size() > 0) {
            return possibleSections.get(new Random().nextInt(possibleSections.size()));
        } else {
            return null;
        }
    }

    public Section findSectionWithProduct(Product product, int amount) {
        List<Section> possibleSections = new ArrayList<>();
        for (Section section: sections) {
            Product sectionProduct = section.getProduct();
            if (sectionProduct != null &&
                    sectionProduct.getId() == product.getId() &&
                    section.getAmount() >= amount) {
                possibleSections.add(section);
            }
        }
        if (possibleSections.size() > 0) {
            return possibleSections.get(new Random().nextInt(possibleSections.size()));
        } else {
            return null;
        }
    }

    public double getTimeDelay(Point from, Point to) {
        return graph.getTimeBetween(from, to);
    }

    private void setSections(List<Rack> racks) {
        for (Rack rack: racks) {
            Point coord = rack.getCoordinate();
            Point sectionSize = rack.getSectionSize();
            Point size = rack.getSize();
            int sectionInRow = size.x / sectionSize.x;
            int sectionInColumn = size.y / sectionSize.y;
            for (int j = 0; j < sectionInColumn; ++j) {
                for (int i = 0; i < sectionInRow; ++i) {
                    for (int k = 0; k < rack.getLevel(); ++k) {
                        Point temp = new Point(coord);
                        temp.translate(box.x * i, box.y * j);
                        int[] x = new int[4];
                        int[] y = new int[4];
                        x[0] = temp.x;
                        y[0] = temp.y;
                        x[1] = temp.x + box.x;
                        y[1] = temp.y;
                        x[2] = temp.x + box.x;
                        y[2] = temp.y + box.y;
                        x[3] = temp.x;
                        y[3] = temp.y + box.y;
                        sections.add(
                                new Section(
                                        new Point(temp.x / box.x, temp.y / box.y),
                                        sectionSize,
                                        k,
                                        rack.getDirection(),
                                        new Polygon(x, y, 4),
                                        rack.getMaxWeightPerSection()
                                )
                        );
                    }
                }
            }
        }

        for (Section section: sections) {
            if (section.getPossibleDirection() != null)
                continue;
            Point current;
            current = new Point(section.getCenter());
            current.translate(0, box.y);
            if (isEmpty(current)) {
                section.setPossibleDirection(Section.Direction.Up);
                continue;
            }
            current = new Point(section.getCenter());
            current.translate(0, -box.y);
            if (isEmpty(current)) {
                section.setPossibleDirection(Section.Direction.Down);
                continue;
            }
            current = new Point(section.getCenter());
            current.translate(box.x, 0);
            if (isEmpty(current)) {
                section.setPossibleDirection(Section.Direction.Right);
                continue;
            }
            current = new Point(section.getCenter());
            current.translate(-box.x, 0);
            if (isEmpty(current)) {
                section.setPossibleDirection(Section.Direction.Left);
                continue;
            }
        }

    }

    public boolean isEmpty(Point point) {
        for (Barrier el: barriers) {
            if (el.getPolygon().contains(point))
                return false;
        }
        for (Section el: sections) {
            if(el.getPolygon().contains(point))
                return false;
        }
        return true;
    }

    public List<Point> getBoundPoints() {
        return boundPoints;
    }

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Point getBox() {
        return box;
    }

    public void printAllSections(Output out) {
        for (Section el: sections) {
            if (el.getLevel() == 0) {
                out.println("id: " + el.getId() + "; index[" + el.getIndex().x + ";" + el.getIndex().y + ']');
            }
        }
    }

    public void printAllWalls(Output out) {
        int size = boundPoints.size();
        for (int i = 0; i < size; ++i) {
            out.println(
                    String.format("a = [%d:%d]; b = [%d:%d]%n",
                        boundPoints.get(i).x, boundPoints.get(i).y,
                        boundPoints.get((i + 1) % size).x, boundPoints.get((i + 1) % size).y
                    )
            );
        }
    }

    public void printAllBarriers(Output out) {
        for (Barrier barrier : barriers) {
            out.println(barrier);
        }
    }

    public Section getRandomSectionWithProduct() {
        List <Section> filledSections = new ArrayList<>();
        for (Section section: sections) {
            if (section.getProduct() != null) {
                filledSections.add(section);
            }
        }
        if (filledSections.size() != 0)
            return filledSections.get(new Random().nextInt(filledSections.size()));
        else
            return null;
    }
}
