package main;

import storageModel.events.Event;

import java.util.Queue;

public class Analyzer {
        private int counterRejected = 0;

        public int getCounterRejected() {
            return counterRejected;
        }

        public void incrementRejected() {
            ++counterRejected;
        }


        private int countWays = 0;
        private double timeSpentOnMove = 0;

        public void saveTime(double time) {
            timeSpentOnMove += time;
            ++countWays;
        }

        public double getAverageTimeSpentOnMove() {
            return timeSpentOnMove / countWays;
        }

        public double getTotalTimeSpentOnMove() {
            return timeSpentOnMove;
        }

        private double sumQueueInOut = 0.0;
        private double lastTime = 0.0;
        private double timeFinishModelling = 0.0;
        public void addToIntegralSumOfInputOutput(double time, Queue<Event> queue) {
            sumQueueInOut += (time - lastTime) * queue.size();
            lastTime = time;
        }

        public void stopTimer(double time) {
            timeFinishModelling = time;
        }

        public double getAverageQueueSize() {
            if (timeFinishModelling != 0) {
                return sumQueueInOut / timeFinishModelling;
            } else {
                return -1;
            }
        }

        private int counterProductEvent = 0;
        private double timeWaiting = 0;
        public void pollEventFromQueue(Event event, double time) {
            ++counterProductEvent;
            timeWaiting += (time - event.getDate());
        }

        public double getAverageTimeWaiting() {
            return timeWaiting / counterProductEvent;
        }
    }
