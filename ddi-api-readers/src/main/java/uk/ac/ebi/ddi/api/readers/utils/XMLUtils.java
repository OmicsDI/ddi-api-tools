package uk.ac.ebi.ddi.api.readers.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by azorin on 15/11/2017.
 */
public class XMLUtils {

    private static XPath xPathOriginal = XPathFactory.newInstance().newXPath();

    public static String readFirstAttribute(Document doc, String path, String attribute) throws Exception {

        NodeList nodes = (NodeList) xPathOriginal.evaluate(
                "//" + path, doc.getDocumentElement(), XPathConstants.NODESET);
        if (nodes.getLength() > 0) {
            Element e = (Element) nodes.item(0);
            return e.getAttribute(attribute);
        } else {
            return null;
        }
    }


    public static String readFirstElement(Document doc, String path) throws Exception {

        NodeList nodes = (NodeList) xPathOriginal.evaluate(
                "//" + path, doc.getDocumentElement(), XPathConstants.NODESET);
        return getValue(nodes);
    }

    public static String readFirstAttribute(Element node, String path, String attribute, XPath xPath) throws Exception {

        NodeList nodes = (NodeList) xPath.evaluate(path, node, XPathConstants.NODESET);
        if (nodes.getLength() > 0) {
            Element e = (Element) nodes.item(0);
            return e.getAttribute(attribute);
        } else {
            return null;
        }
    }

    public static String readFirstElement(Node node, String path, XPath xPath) throws Exception {

        NodeList nodes = (NodeList) xPath.evaluate(path, node, XPathConstants.NODESET);
        return getValue(nodes);
    }

    public static NodeList findElements(Document doc, String path, XPath xPath) throws Exception {

        return (NodeList) xPath.evaluate(path, doc.getDocumentElement(), XPathConstants.NODESET);
    }

    private static String getValue(NodeList nodes) {
        if (nodes.getLength() > 0) {
            Element e = (Element) nodes.item(0);
            return e.getFirstChild().getTextContent();
        } else {
            return null;
        }
    }

}
