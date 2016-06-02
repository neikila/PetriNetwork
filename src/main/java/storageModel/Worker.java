package storageModel;

import storageModel.events.*;
import storageModel.events.Event;
import storageModel.storageDetails.Section;
import utils.Output;

import java.awt.*;

/**
 * Created by neikila on 23.11.15.
 */
public class Worker {
    private static int lastId = 0;
    private int id;
    private Point position;
    private State state;
    private Storage storage;
    private Task task;
    private Output out;

    public Worker(Point position, Storage storage, Output out) {
        id = lastId++;
        this.position = position;
        state = State.Free;
        this.storage = storage;
        task = null;

        this.out = out;
    }

    public int getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public boolean isFree() {
        return state.equals(State.Free);
    }

    public State getState() {
        return state;
    }

    public void moveTo(Point point) {
        position = point;
    }

    public Event nextState(double time) {
        double delay = 0;
        switch (state) {
            case Free:
                state = State.GetToLoad;
                delay = storage.getTimeDelay(position, task.from);
                out.println("Worker");
                out.printPoint("From   ", position);
                out.printPoint("To     ", task.from);
                out.println("Time in the way: " + delay);
                return new PointAchieved(time + delay, task.from, this);
            case GetToLoad:
                state = State.Loading;
                position = task.from;
                delay = 3.0;
                if (task.sectionFrom != null) {
                    delay += task.sectionFrom.getLevel() * 4;
                }
                return new ProductLoaded(time + delay, this);
            case Loading:
                state = State.GetToRelease;
                if (task.sectionFrom != null) {
                    task.sectionFrom.getProduct(task.amount);
                }
                delay += storage.getTimeDelay(position, task.to);
                out.println("Worker");
                out.printPoint("From   ", position);
                out.printPoint("To     ", task.to);
                out.println("Time in the way: " + delay);
                return new PointAchieved(time + delay, task.to, this);
            case GetToRelease:
                state = State.Releasing;
                position = task.to;
                delay = 3.0;
                if (task.sectionTo != null) {
                    delay += task.sectionTo.getLevel() * 4;
                }
                return new ProductReleased(time + delay, this);
            case Releasing:
                state = State.Free;
                if (task.sectionTo != null) {
                    task.sectionTo.addProduct(task.product, task.amount);
                }
                break;
        }
        return null;
    }

    public enum State {
        GetToLoad,
        Loading,
        GetToRelease,
        Releasing,
        Free
    }

    private class Task {
        final Point from;
        final Point to;
        final Product product;
        final int amount;
        final Section sectionFrom;
        final Section sectionTo;

        public Task(Point from, Point to, Section sectionFrom, Section sectionTo, Product product, int amount) {
            this.from = from;
            this.to = to;
            this.sectionFrom = sectionFrom;
            this.sectionTo = sectionTo;
            this.product = product;
            this.amount = amount;
        }
    }

    public boolean handleProductEvent(Event event) {
        switch(event.getEventType()) {
            case ProductIncome:
                return handleProductIncome((ProductIncome) event);
            case ProductRequest:
                return handleProductRequest((ProductRequest) event);
            default:
                return false;
        }
    }

    private boolean handleProductIncome(ProductIncome income) {
        Point from = storage.getEntrancePoint();
        double totalWeight = income.getAmount() * income.getProduct().getWeightOfUnit();
        Section section = storage.findSectionForProduct(income.getProduct(), totalWeight);
        if (section == null) {
            out.println("No place for product.\n" +
                    income.getProduct() + "\n" +
                    "Amount " + income.getAmount());
            return false;
        } else {
            out.printPoint("From   ", from);
            out.printPoint("Section", section.getIndex());
        }
        Point to = section.getPointAccess();
        task = new Task(from, to, null, section, income.getProduct(), income.getAmount());
        return true;
    }

    private boolean handleProductRequest(ProductRequest request) {
        Point to = storage.getExitPoint();
        Section section = storage.findSectionWithProduct(request.getProduct(), request.getAmount());
        if (section == null) {
            out.println("No such product.\n" +
                    request.getProduct() + "\n" +
                    "Amount " + request.getAmount());
            return false;
        }
        Point from = section.getPointAccess();
        out.printPoint("Section", section.getIndex());
        out.printPoint("To     ", to);
        task = new Task(from, to, section, null, request.getProduct(), request.getAmount());
        return true;
    }
}
