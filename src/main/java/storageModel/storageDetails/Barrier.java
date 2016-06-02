package storageModel.storageDetails;

import java.awt.*;

/**
 * Created by neikila on 19.11.15.
 */
public class Barrier {
    private Polygon polygon;

    public Barrier(Point absoluteCoordinate, Polygon polygon) {
        this.polygon = polygon;
        if (absoluteCoordinate != null) {
            polygon.translate(absoluteCoordinate.x, absoluteCoordinate.y);
        }
    }

    public Polygon getPolygon() {
        return polygon;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        stringBuilder
                .append("[").append(polygon.xpoints[0])
                .append(':')  .append(polygon.ypoints[0])
                .append(']');

        for (int i = 1; i < polygon.npoints; ++i) {
            stringBuilder
                    .append(", [").append(polygon.xpoints[i])
                    .append(':')  .append(polygon.ypoints[i])
                    .append(']');
        }
        return stringBuilder.append("}").toString();
    }
}