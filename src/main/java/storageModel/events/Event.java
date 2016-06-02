package storageModel.events;

import java.util.Comparator;

/**
 * Created by neikila on 31.10.15.
 */
public class Event {
    final protected double date;
    final protected EventType eventType;

    public Event(double date, EventType eventType) {
        this.date = date;
        this.eventType = eventType;
    }

    public double getDate() {
        return date;
    }

    public EventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "date = " + date +
                ", eventType = " + eventType +
                '}';
    }

    public static class EventComparator implements Comparator<Event> {

        @Override
        public int compare(Event o1, Event o2) {
            if (o1.date < o2.date) {
                return -1;
            } else {
                if (o1.date == o2.date) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

    /**
     * Created by neikila on 31.10.15.
     */
    public enum EventType {
        ProductIncome,
        ProductRequest,
        PointAchieved,
        ProductLoaded,
        ProductReleased
    }
}
