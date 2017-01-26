package uk.ac.ebi.ddi.api.readers.mw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.ddi.api.readers.mw.ws.client.DatasetWsClient;
import uk.ac.ebi.ddi.api.readers.mw.ws.model.DatasetList;
import uk.ac.ebi.ddi.api.readers.mw.ws.model.DiseaseList;
import uk.ac.ebi.ddi.api.readers.mw.ws.model.SpecieList;
import uk.ac.ebi.ddi.api.readers.mw.ws.model.TissueList;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.api.readers.utils.Transformers;

import uk.ac.ebi.ddi.api.readers.mw.ws.client.MWWsConfigProd;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * This program takes a MetabolomeWorkbench URL and generate for all the experiments the
 *
 * @author Yasset Perez-Riverol
 */

public class GenerateMWOmicsXML {

    private static final Logger logger = LoggerFactory.getLogger(GenerateMWOmicsXML.class);

  /**
     * This program take an output folder as a parameter an create different EBE eyes files for
     * all the project in MetabolomicsWorkbench. It loop all the project in MetabolomeWorkbench and
     * print them to the give output
     *
     * @param args
     */
    public static void main(String[] args) {

        String outputFolder = null;
        String releaseDate  = null;

        if (args != null && args.length > 1 && args[0] != null){
            outputFolder = args[0];
            releaseDate  = args[1];
        }



        else {
            System.exit(-1);
        }

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/app-context.xml");
        MWWsConfigProd mwWsConfigProd = (MWWsConfigProd) ctx.getBean("mwWsConfig");

        try {
            GenerateMWOmicsXML.generateMWXMLfiles(mwWsConfigProd, outputFolder, releaseDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateMWXMLfiles( MWWsConfigProd configProd, String outputFolder, String releaseDate) throws Exception {

        DatasetWsClient datasetWsClient = new DatasetWsClient(configProd);
        DatasetList datasets = datasetWsClient.getAllDatasets();
        TissueList tissueList   = datasetWsClient.getTissues();
        SpecieList specieList   = datasetWsClient.getSpecies();
        DiseaseList diseaseList = datasetWsClient.getDiseases();
        List<Entry> entries     = new ArrayList<>();

        if (datasets != null && datasets.datasets != null) {

            datasets.datasets.values().forEach( dataset -> {
                if(dataset != null && dataset.getIdentifier() != null){
                    dataset.setMetabolites(datasetWsClient.getMataboliteList(dataset.getIdentifier()));
                    dataset.setFactors(datasetWsClient.getFactorList(dataset.getIdentifier()));
                    dataset.setTissues(tissueList.getTissuesByDataset(dataset.getIdentifier()));
                    dataset.setSpecies(specieList.getSpeciesByDataset(dataset.getIdentifier()));
                    dataset.setDiseases(diseaseList.getDiseasesByDataset(dataset.getIdentifier()));
                    dataset.setAnalysis(datasetWsClient.getAnalysisInformantion(dataset.getIdentifier()));
                    entries.add(Transformers.transformAPIDatasetToEntry(dataset));
                    logger.info(dataset.getIdentifier());
                }
            });
        }

        FileWriter mwFile = new FileWriter(outputFolder + "/metabolomics_workbench_data.xml");

        OmicsDataMarshaller mm = new OmicsDataMarshaller();

        Database database = new Database();
        database.setDescription(Constants.METABOLOMICS_WORKBENCH_DESCRIPTION);
        database.setName(Constants.METABOLOMICS_WORKBENCH);
        database.setRelease(releaseDate);
        database.setEntries(entries);
        database.setEntryCount(entries.size());
        mm.marshall(database, mwFile);
    }
}
