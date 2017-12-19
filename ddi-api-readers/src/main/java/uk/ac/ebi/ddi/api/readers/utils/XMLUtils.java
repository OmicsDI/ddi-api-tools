package uk.ac.ebi.ddi.api.readers.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by azorin on 15/11/2017.
 */
public class XMLUtils {

    private static XPath xPath = XPathFactory.newInstance().newXPath();

    public static String readFirstAttribute(Document doc, String path, String attribute) throws Exception{

        NodeList nodes = (NodeList)xPath.evaluate("//"+path, doc.getDocumentElement(), XPathConstants.NODESET);
        if(nodes.getLength() > 0){
            Element e = (Element) nodes.item(0);
            String result = e.getAttribute(attribute);
            return result;
        }else{
            return null;
        }
    }


    public static String readFirstElement(Document doc, String path) throws Exception{

        NodeList nodes = (NodeList)xPath.evaluate("//" + path, doc.getDocumentElement(), XPathConstants.NODESET);
        if(nodes.getLength() > 0) {
            Element e = (Element) nodes.item(0);
            String result = e.getFirstChild().getTextContent();
            return result;
        }else{
            return null;
        }
    }


}
