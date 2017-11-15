package uk.ac.ebi.ddi.api.readers.bioprojects.ws.client;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.XMLUtils;
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
            try {

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(f);

                BioprojectDataset dataset = new BioprojectDataset();

                String database = XMLUtils.readFirstAttribute(doc, "dbXREF", "db");
                if (null == database)
                    continue;

                if (!"GEO".equals(database))
                    continue;

                String id = XMLUtils.readFirstElement(doc, "dbXREF/ID");
                String title = XMLUtils.readFirstElement(doc, "ProjectDescr/Title");
                String description = XMLUtils.readFirstElement(doc, "ProjectDescr/Description");

                dataset.setRepository(database);
                dataset.setIdentifier(id);
                dataset.setName(title);
                dataset.setDescription(description);

                paxDBDatasets.put(id, dataset);
            } catch(Exception ex){
                logger.error("Error processing " + f.getName() + " : " + ex.getMessage());
            }
        }

        return paxDBDatasets.values();
    }

}
