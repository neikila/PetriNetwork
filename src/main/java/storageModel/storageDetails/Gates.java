package storageModel.storageDetails;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by neikila on 23.11.15.
 */
public class Gates {
    private List<Point> fields;

    public Gates(List<Point> fields) {
        this.fields = new ArrayList<>(fields);
    }

    public List<Point> getFields() {
        return fields;
    }

    public Point getRandomField() {
        Random random = new Random();
        return fields.get(random.nextInt(fields.size()));
    }
}
