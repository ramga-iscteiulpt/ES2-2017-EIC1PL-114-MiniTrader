package mt.persistence;


import mt.Order;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XMLSaver {

	/*
     * FOR TEST
	 *

	public static void main(String[] args) {
		try {

			Order order = Order.createBuyOrder("jrel", "EDP", 10, 10.5);

			File inputFile = new File("OrdersPersistence.xml");
			XMLSaver xml = new XMLSaver(inputFile).init();

			xml.save(order);
			xml.save(order);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 */


    private File file = new File("OrdersPersistence.xml");
    private Document document;
    private Transformer transformer;

    public XMLSaver() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//	public XMLSaver(File file) {
//		this.file = file;
//	}

    public XMLSaver init() throws TransformerConfigurationException, TransformerFactoryConfigurationError, SAXException,
            IOException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        document = dBuilder.parse(this.file);

        transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        return this;
    }

    private Element toXML(Order order) {

        Element orderXML = document.createElement("Order");
        orderXML.setAttribute("Id", String.valueOf(order.getServerOrderID()));
        orderXML.setAttribute("Type", order.isBuyOrder() ? "buy" : "sel");
        orderXML.setAttribute("Stock", order.getStock());
        orderXML.setAttribute("Units", String.valueOf(order.getNumberOfUnits()));
        orderXML.setAttribute("Price", String.valueOf(order.getPricePerUnit()));

        Element costumer = document.createElement("costumer");
        costumer.appendChild(document.createTextNode(order.getNickname()));
        orderXML.appendChild(costumer);

        return orderXML;
    }

    public void save(Order order) throws TransformerException {
        Node node = document.getDocumentElement();
        node.appendChild(toXML(order));

        StreamResult result = new StreamResult(file);
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
    }

}