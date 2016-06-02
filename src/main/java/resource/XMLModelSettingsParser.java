package resource;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by neikila on 19.11.15.
 */
public class XMLModelSettingsParser extends XMLParser {
    public static final String DEADLINE = "deadline";
    public static final String STOP_GENERATING = "stopGenerating";
    public static final String EVENTS = "events";
    public static final String PRODUCT_INCOME = "productIncome";
    public static final String PRODUCT_REQUEST = "productRequest";
    public static final String AMOUNT = "amount";
    public static final String DELTA = "delta";

    public XMLModelSettingsParser(String fileName) {
        super(fileName);
    }

    public double getDeadline() {
        return getDouble(root, DEADLINE);
    }

    public double getStopGenerating() {
        return getDouble(root, STOP_GENERATING);
    }

    private Element getEvent(String eventName) {
        NodeList list = root.getElementsByTagName(EVENTS);
        return  (Element)((Element)list.item(0)).getElementsByTagName(eventName).item(0);
    }

    public int getIncomeAmount() {
        return Integer.parseInt(getEvent(PRODUCT_INCOME).getElementsByTagName(AMOUNT).item(0).getTextContent());
    }

    public int getIncomeDelta() {
        return Integer.parseInt(getEvent(PRODUCT_INCOME).getElementsByTagName(DELTA).item(0).getTextContent());
    }

    public int getRequestDelta() {
        return Integer.parseInt(getEvent(PRODUCT_REQUEST).getElementsByTagName(DELTA).item(0).getTextContent());
    }
}