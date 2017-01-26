package uk.ac.ebi.ddi.api.readers.massive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.ddi.api.readers.massive.ws.client.DatasetWsClient;

import uk.ac.ebi.ddi.api.readers.massive.ws.client.ISODetasetsWsClient;
import uk.ac.ebi.ddi.api.readers.massive.ws.model.MassiveDatasetDetail;
import uk.ac.ebi.ddi.api.readers.massive.ws.model.MassiveDatasetSummaryMassive;
import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;
import uk.ac.ebi.ddi.api.readers.massive.ws.client.MassiveWsConfigProd;
import uk.ac.ebi.ddi.api.readers.massive.ws.filters.DatasetSummarySizeFilter;
import uk.ac.ebi.ddi.api.readers.massive.ws.filters.DatasetSummaryTrancheFilter;
import uk.ac.ebi.ddi.api.readers.massive.ws.filters.DatasetSummaryUserFilter;
import uk.ac.ebi.ddi.api.readers.massive.ws.model.MassiveDatasetList;


import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.api.readers.utils.Transformers;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entries;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This program takes a datasets from massive and convert them into DDI experiments
 *
 * @author Yasset Perez-Riverol
 */

public class GenerateMassiveOmicsXML{

    private static final Logger logger = LoggerFactory.getLogger(GenerateMassiveOmicsXML.class);

    /**
   * This program generate the massive files in two different type of files MASSIVE and GNPS Files. The MASSIVE
   * files correspond to proteomics datasets and the GNPS correspond to metabolomics datasets.
   *
   * @param args
   */
    public static void main(String[] args) {

        String outputFolder = null;
        String releaseDate  = null;

        if (args != null && args.length > 1 && args[0] != null){
            outputFolder = args[0];
            releaseDate  = args[1];
        } else
            System.exit(-1);


        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/app-context.xml");
        MassiveWsConfigProd mwWsConfigProd = (MassiveWsConfigProd) ctx.getBean("mwWsConfig");

        try {
            GenerateMassiveOmicsXML.generateMWXMLFiles(mwWsConfigProd, outputFolder, releaseDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateMWXMLFiles(AbstractWsConfig configProd, String outputFolder, String releaseDate) throws Exception{

        DatasetWsClient datasetWsClient = new DatasetWsClient(configProd);
        ISODetasetsWsClient isoDetasetsWsClient = new ISODetasetsWsClient(configProd);
        MassiveDatasetList datasetList = isoDetasetsWsClient.getAllDatasets();

        if (datasetList != null && datasetList.datasets != null) {

            List<MassiveDatasetSummaryMassive> dataSetSummaries = new ArrayList<>(Arrays.asList(datasetList.datasets));
            Entries massiveEntries = new Entries();
            Entries gnpsEntries    = new Entries();
            dataSetSummaries.parallelStream().forEach( dataset -> {

                if (dataset.getTask() != null && dataset.getFileCount() > 0) {
                    MassiveDatasetDetail datasetDetail = datasetWsClient.getDataset(dataset.getTask());
                    if (datasetDetail != null && datasetDetail.getIdentifier() != null
                            && new DatasetSummarySizeFilter(10).valid(dataset)
                            && new DatasetSummarySizeFilter(1).valid(dataset)
                            && !(new DatasetSummaryUserFilter("tranche_mbraga").valid(dataset))
                            && (dataset.getTitle() != null && !dataset.getTitle().isEmpty())
                            && new DatasetSummaryTrancheFilter<>().valid(dataset)) {

                        List<String> datasetFiles = new ArrayList<>();
                        try {
                            datasetFiles = datasetWsClient.getFilePaths(datasetDetail.getIdentifier());
                        } catch (IOException e) {
                            logger.debug(e.getMessage());
                        }

                        if(datasetFiles.size() > 0 )
                            datasetDetail.setDataFilePaths(datasetFiles);

                        if (dataset.getCreated() != null)
                            datasetDetail.setCreated(dataset.getCreated());
                        if (datasetDetail.getSpecies() != null){
                            Entry entry = Transformers.transformAPIDatasetToEntry(datasetDetail);
                            if(entry.getDatabase().equalsIgnoreCase(Constants.GNPS))
                                gnpsEntries.addEntry(entry);
                            else if(entry.getDatabase().equalsIgnoreCase(Constants.MASSIVE))
                                massiveEntries.addEntry(entry);
                        }
                    }
                }
                logger.info(dataset.getHash());
            });

            FileWriter gnpsFile = new FileWriter(outputFolder + "/gnps_data.xml");
            FileWriter massiveFile = new FileWriter(outputFolder + "/massive_data.xml");

            OmicsDataMarshaller mm = new OmicsDataMarshaller();

            Database database = new Database();
            database.setDescription(Constants.GNPS_DESCRIPTION);
            database.setName(Constants.GNPS);
            database.setRelease(releaseDate);
            database.setEntries(gnpsEntries);
            database.setEntryCount(gnpsEntries.getEntry().size());
            mm.marshall(database, gnpsFile);


            database.setDescription(Constants.MASSIVE_DESCRIPTION);
            database.setName(Constants.MASSIVE);
            database.setRelease(releaseDate);
            database.setEntries(massiveEntries);
            database.setEntryCount(massiveEntries.getEntry().size());
            mm.marshall(database, massiveFile);
        }
    }

}
