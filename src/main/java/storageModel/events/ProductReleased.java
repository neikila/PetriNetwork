package storageModel.events;

import storageModel.Worker;

/**
 * Created by neikila on 22.11.15.
 */
public class ProductReleased extends Event {
    private Worker worker;

    public ProductReleased(double date, Worker worker) {
        super(date, EventType.ProductReleased);
        this.worker = worker;
    }

    public Worker getWorker() {
        return worker;
    }
}
