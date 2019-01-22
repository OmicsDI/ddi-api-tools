package uk.ac.ebi.ddi.api.readers.px.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.ac.ebi.ddi.api.readers.px.xml.io.PxReader;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;


/**
 * Reader using SAX the XML file
 *
 * @author ypriverol
 */
public class ReaderPxXML {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderPxXML.class);

    private static boolean validateXML(String page) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            InputStream in = org.apache.commons.io.IOUtils.toInputStream(page, "UTF-8");
            db = dbf.newDocumentBuilder();
            db.parse(in);
            return true;
        } catch (Exception e) {
            LOGGER.error("Exception occurred when reading page {}, ", page, e);
            return false;
        }
    }


    /**
     * Get a document from an String page.
     *
     * @param xml XML as string
     * @return the Document
     */
    private static Document getDomElement(String xml) {
        Document doc;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.error("Error: ", e);
            return null;
        }
        // return DOM
        return doc;
    }

    /**
     * Parse the XML JAXB file into a Prject data model. It allows to map the information in the common
     * data model for exporting.
     *
     * @param page the JAXB XML object
     * @return Project the project
     * @throws IOException
     * @throws JAXBException
     */
    public static PxReader parseDocument(String page) throws IOException, JAXBException {
        if (validateXML(page)) {
            return new PxReader(IOUtils.toInputStream(page, "UTF-8"));
        }
        return null;
    }


}
