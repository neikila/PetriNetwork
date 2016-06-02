package storageModel.events;

import storageModel.Worker;

/**
 * Created by neikila on 22.11.15.
 */
public class ProductLoaded extends Event {
    private Worker worker;

    public ProductLoaded(double date, Worker worker) {
        super(date, EventType.ProductLoaded);
        this.worker = worker;
    }

    public Worker getWorker() {
        return worker;
    }
}
