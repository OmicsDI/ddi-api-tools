package uk.ac.ebi.ddi.api.readers.bioprojects.ws.client;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.BioprojectDataset;
import uk.ac.ebi.ddi.api.readers.utils.FileUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

/**
 * @author Andrey Zorin (azorin@ebi.ac.uk)
 * @date 14/11/2017
 */

public class BioprojectsClient {

    private String filePath;
    private String dbXref;

    private static final Logger logger = LoggerFactory.getLogger(BioprojectsClient.class);

    public BioprojectsClient(String filePath, String dbXref){
        this.dbXref = dbXref;
        this.filePath = filePath;
    }

    public Collection<BioprojectDataset> getAllDatasets() throws Exception {

        Map<String, BioprojectDataset> paxDBDatasets = new HashMap<>();

        File dir = new File(filePath);
        FileFilter fileFilter = new WildcardFileFilter("PRJNA*.xml");

        for (File f : dir.listFiles(fileFilter)){

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(f);

            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList)xPath.evaluate("//dbXREF", doc.getDocumentElement(), XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); ++i) {
                Element e = (Element) nodes.item(i);
                String db = e.getAttribute("db");

                System.out.print(db);
            }

        }

        return paxDBDatasets.values();
    }

}
