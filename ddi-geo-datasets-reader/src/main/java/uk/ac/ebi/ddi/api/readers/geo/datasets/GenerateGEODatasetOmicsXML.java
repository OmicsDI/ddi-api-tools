package uk.ac.ebi.ddi.api.readers.geo.datasets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.ddi.api.readers.geo.datasets.ws.client.GEODatasetBFTPClient;
import uk.ac.ebi.ddi.api.readers.geo.datasets.ws.client.GEOFTPProd;
import uk.ac.ebi.ddi.api.readers.geo.datasets.ws.model.Dataset;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.api.readers.utils.Transformers;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entries;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.FileWriter;
import java.util.List;


/**
 * This project takes class Retrieve information from GPMDB, it allows to retrieve
 * the proteins ids for an specific model, etc.
 *
 * @author Yasset Perez-Riverol
 */

public class GenerateGEODatasetOmicsXML {

    /**
     * This program generate the massive files in two different type of files MASSIVE and GNPS Files. The MASSIVE
     * files correspond to proteomics datasets and the GNPS correspond to metabolomics datasets.
     *
     * @param args
     */
    public static void main(String[] args) {

        String outputFolder = null;
        String releaseDate = null;

        if (args != null && args.length > 1 && args[0] != null) {
            outputFolder = args[0];
            releaseDate = args[1];
        } else {
            System.exit(-1);
        }


        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/app-context.xml");
        GEOFTPProd geoConfigProd = (GEOFTPProd) ctx.getBean("GEOConfig");

        try {
            generateMWXMLFiles(geoConfigProd, outputFolder, releaseDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateMWXMLFiles(GEOFTPProd configProd, String outputFolder, String releaseDate)
            throws Exception {

        GEODatasetBFTPClient ftpClient = new GEODatasetBFTPClient(configProd);
        List<Dataset> datasetList = ftpClient.listAllGEODatasets();

        if (datasetList != null && datasetList.size() > 0) {
            Entries geoEntries = new Entries();
            datasetList.parallelStream().forEach(datasetFTP -> {
                if (datasetFTP != null && datasetFTP.getIdentifier() != null) {
                    Entry entry = Transformers.transformAPIDatasetToEntry(datasetFTP);
                    geoEntries.addEntry(entry);
                }
            });

            FileWriter geoFile = new FileWriter(outputFolder + "/geo_data.xml");
            OmicsDataMarshaller mm = new OmicsDataMarshaller();

            Database database = new Database();
            database.setDescription(Constants.GEO_DESCRIPTION);
            database.setName(Constants.GEO_DATABASE_DATASETS);
            database.setRelease(releaseDate);
            database.setEntries(geoEntries);
            database.setEntryCount(geoEntries.getEntry().size());
            mm.marshall(database, geoFile);
        }
    }


}
