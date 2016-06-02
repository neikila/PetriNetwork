package utils;

import main.Analyzer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by neikila on 18.12.15.
 */
public class XMLStatisticOutput {
    public static final String ROOT = "root";
    public static final String AVERAGE_QUEUE_SIZE = "averageQueueSize";
    public static final String AVERAGE_TIME_SPENT_ON_MOVE = "averageTimeSpentOnMove";
    public static final String AVERAGE_TIME_WAITING = "averageTimeWaiting";
    public static final String AMOUNT_REJECTED = "amountRejected";
    public static final String TOTAL_TIME_SPENT_ON_MOVE= "totalTimeSpentOnMove";

    private String outputDir;
    private String filename;
    private Analyzer analyzer;
    private Document document;

    public XMLStatisticOutput(String filename, Analyzer analyzer, String outputDir) {
        this.filename = filename;
        this.analyzer = analyzer;
        this.outputDir = outputDir;
    }

    public void print() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element root = document.createElement(ROOT);
//            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//            root.setAttribute("xsi:noNamespaceSchemaLocation", "model.xsd");
            document.appendChild(root);

            addProperty(root, AVERAGE_QUEUE_SIZE, analyzer.getAverageQueueSize());
            addProperty(root, AVERAGE_TIME_SPENT_ON_MOVE, analyzer.getAverageTimeSpentOnMove());
            addProperty(root, AVERAGE_TIME_WAITING, analyzer.getAverageTimeWaiting());
            addProperty(root, TOTAL_TIME_SPENT_ON_MOVE, analyzer.getTotalTimeSpentOnMove());
            addProperty(root, AMOUNT_REJECTED, analyzer.getCounterRejected());

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(root);
            StreamResult result = new StreamResult(new File(outputDir, filename));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            System.err.println(e);
        }
    }

    private void addProperty(Element element, String name, Object value) {
        Element averageQueueSize = document.createElement(name);
        averageQueueSize.appendChild(
                document.createTextNode(String.valueOf(value))
        );
        element.appendChild(averageQueueSize);
    }
}
