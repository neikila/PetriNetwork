package resource;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import storageModel.Product;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by neikila on 19.11.15.
 */
public class XMLProductsParser extends XMLParser {

    public static final String PRODUCT_NAME = "productName";
    public static final String WEIGHT_OF_SINGLE_PRODUCT = "weightOfSingleProduct";

    public XMLProductsParser(String fileName) {
        super(fileName);
    }

    public List<Product> getProducts() {
        return getArrayOfSomethingFromElement(root, this::getProduct);
    }

    private Product getProduct(Element node) {
        String productName = node.
                getElementsByTagName(PRODUCT_NAME).
                item(0).
                getTextContent();
        double weight = Double.parseDouble(node.getElementsByTagName(WEIGHT_OF_SINGLE_PRODUCT).item(0).getTextContent());
        return new Product(productName, weight);
    }
}
