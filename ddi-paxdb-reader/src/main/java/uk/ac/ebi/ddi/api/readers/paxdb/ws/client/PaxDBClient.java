package uk.ac.ebi.ddi.api.readers.paxdb.ws.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.paxdb.ws.model.PaxDBDataset;
import uk.ac.ebi.ddi.api.readers.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * PaxDB returns a zip file with corresponding subfolders each subfolder contains
 * a set of file that can be parse.
 *
 * @author ypriverol
 */

public class PaxDBClient {

    private String dataSetURL;
    private String urlMappingIdentifiers;
    private String proteinIdentifiersURL;


    private static final Logger LOGGER = LoggerFactory.getLogger(PaxDBClient.class);

    public PaxDBClient(String dataSetURL, String mappingIdentifiers, String proteinIdURL) {
        this.dataSetURL = dataSetURL;
        this.urlMappingIdentifiers = mappingIdentifiers;
        this.proteinIdentifiersURL = proteinIdURL;
    }

    public Collection<PaxDBDataset> getAllDatasets() throws IOException {

        Map<String, PaxDBDataset> paxDBDatasets = new HashMap<>();

        Map<String, ByteArrayOutputStream> zipInputStreamFiles = FileUtils.doZipInputStream(this.dataSetURL);

        LOGGER.debug("Number of Datasets:" + zipInputStreamFiles.size());

        if (zipInputStreamFiles.size() > 0) {
            zipInputStreamFiles.forEach((key, value) -> {
                try {
                    paxDBDatasets.put(FileUtils.getNameFromInternalZipPath(key), PaxDBDatasetReader.readDataset(value));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        ByteArrayOutputStream zipInputStreamFile = FileUtils.doInputStream(this.urlMappingIdentifiers);
        Map<String, String> mapIdentifiers = PaxDBDatasetReader.readMapFileIdentifiers(zipInputStreamFile);

        paxDBDatasets.forEach((key, value) -> {
            if (mapIdentifiers.containsKey(key)) {
                value.setFullLink(mapIdentifiers.get(key));
            }
        });

        Map<String, ByteArrayOutputStream> zipInputStreamProteinFiles =
                FileUtils.doZipInputStream(this.proteinIdentifiersURL);
        Map<String, String> proteins = new HashMap<>();
        zipInputStreamProteinFiles.forEach((key, value) -> {
            if (value != null) {
                Map<String, String> values = null;
                try {
                    values = PaxDBDatasetReader.readProteinIdentifiers(value);
                    proteins.putAll(values);
                } catch (IOException e) {
                    LOGGER.error("Exception occurred when reading protein {}, e", value, e);
                }
            }
        });
        paxDBDatasets.forEach((key, dataset) -> dataset.updateIds(proteins));

        return paxDBDatasets.values();
    }

}
