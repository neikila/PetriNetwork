package storageModel.events;

import storageModel.Worker;

import java.awt.*;

/**
 * Created by neikila on 22.11.15.
 */
public class PointAchieved extends Event {
    private Point point;
    private Worker worker;

    public PointAchieved(double date, Point point, Worker worker) {
        super(date, EventType.PointAchieved);
        this.point = point;
        this.worker = worker;
    }

    public Point getPoint() {
        return point;
    }

    public Worker getWorker() {
        return worker;
    }
}
