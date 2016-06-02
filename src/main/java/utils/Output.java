package utils;

import storageModel.Product;
import storageModel.events.*;
import storageModel.events.Event;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by Kirill on 07.12.2015.
 */
public class Output {
    private PrintStream out;
    private PrintStream console = System.out;
    private boolean toConsole;
    private boolean isDebug;

    public Output(String dir, String filename, boolean toConsole) {
        isDebug = false;
        this.toConsole = toConsole;
        try {
            if (filename != null) {
                out = new PrintStream(new File(dir, filename));
            } else {
                out = null;
            }
        } catch (IOException e) {
            System.err.println("Error while opening file for log");
            System.err.println(e);
        }
    }

    public Output(String filename, boolean toConsole) {
        this("out", filename, toConsole);
    }

    public Output(boolean toConsole) {
        this(null, toConsole);
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public void printPoint(Point point) {
        printPoint("Point  ", point);
    }

    public void printProductEvent(Event event) {
        if (event.getEventType().equals(Event.EventType.ProductIncome)) {
            println("{'Product': '" + ((ProductIncome) event).getProduct().getName() +
                    "', 'amount': " + ((ProductIncome) event).getAmount() + '}');
        }
        if (event.getEventType().equals(Event.EventType.ProductRequest)) {
            println("{'Product': '" + ((ProductRequest) event).getProduct().getName() +
                    "', 'amount': " + ((ProductRequest) event).getAmount() + '}');
        }
    }

    public void printPoint(String message, Point point) {
        println(message + ":{'x': " + point.x + ", 'y': " + point.y + '}');
    }

    public void print(Object str) {
        if (out != null) {
            out.print(str);
        }
        if (toConsole) {
            console.print(str);
        }
    }

    public void println(Object str) {
        print(String.valueOf(str) + '\n');
    }

    public void println() {
        print("\n");
    }

    public void debugPrintln(Object str) {
        if (isDebug) {
            println(str);
        }
    }

    public void close() {
        out.close();
    }
}
