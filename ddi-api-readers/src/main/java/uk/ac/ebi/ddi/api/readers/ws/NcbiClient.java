package uk.ac.ebi.ddi.api.readers.ws;

import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import uk.ac.ebi.ddi.api.readers.model.NcbiDataset;
import uk.ac.ebi.ddi.api.readers.utils.XMLUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Created by azorin on 13/12/2017.
 */
public class NcbiClient {

    String filePath = "";

    public NcbiClient(String filePath) {
        this.filePath = filePath;
    }

    private File getNcbiFile(String id) throws Exception {
        File f = new File(filePath + "/" + id + ".soft");
        if (!f.exists()) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://eutils.ncbi.nlm.nih.gov")
                    .path("/entrez/eutils/efetch.fcgi")
                    .queryParam("db", "bioproject")
                    .queryParam("id", id);
            URL website = builder.build().encode().toUri().toURL();
            try (InputStream in = website.openStream()) {
                Path targetPath = f.toPath();
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return f;
    }

    public NcbiDataset getNcbiDataset(String id) throws Exception {

        File file = getNcbiFile(id);

        NcbiDataset dataset = new NcbiDataset();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        dataset.database = XMLUtils.readFirstAttribute(doc, "dbXREF", "db");

        dataset.id = XMLUtils.readFirstElement(doc, "dbXREF/ID");
        dataset.title = XMLUtils.readFirstElement(doc, "ProjectDescr/Title");
        dataset.description = XMLUtils.readFirstElement(doc, "ProjectDescr/Description");
        dataset.publicationDate = XMLUtils.readFirstElement(doc, "ProjectDescr/ProjectReleaseDate");

        String omicsType = XMLUtils.readFirstElement(doc,
                "ProjectType/ProjectTypeSubmission/ProjectDataTypeSet/DataType");
        if ((null != omicsType) && omicsType.contains("Transcriptome")) {
            dataset.omicsType = "Transcriptomics";
        }

        dataset.organismName = XMLUtils.readFirstElement(doc,
                "ProjectType/ProjectTypeSubmission/Target/Organism/OrganismName");

        return dataset;
    }
}
