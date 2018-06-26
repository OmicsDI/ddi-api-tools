package uk.ac.ebi.ddi.api.readers.bioprojects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.client.BioprojectsClient;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.client.GeoClient;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.BioprojectDataset;
import uk.ac.ebi.ddi.api.readers.model.IGenerator;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.api.readers.utils.Transformers;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.service.dataset.DatasetService;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Andrey Zorin (azorin@ebi.ac.uk)
 * @date 14/11/2017
 */

public class GenerateBioprojectsOmicsXML implements IGenerator{

    private static final Logger logger = LoggerFactory.getLogger(GenerateBioprojectsOmicsXML.class);

    String outputFolder;

    String releaseDate;

    BioprojectsClient bioprojectsClient;

    DatasetService datasetService;

    String databases;

    public GenerateBioprojectsOmicsXML(BioprojectsClient bioprojectsClient
            , DatasetService datasetService
            , String outputFolder
            , String releaseDate
            , String databases) {
        this.bioprojectsClient = bioprojectsClient;
        this.datasetService = datasetService;
        this.outputFolder = outputFolder;
        this.releaseDate = releaseDate;
        this.databases = databases;
    }

    /**
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
        BioprojectsClient bioprojectsClient = (BioprojectsClient) ctx.getBean("bioprojectsClient");
        DatasetService  datasetService = (DatasetService) ctx.getBean("DatasetService");

        try {
            new GenerateBioprojectsOmicsXML(bioprojectsClient,datasetService,outputFolder,releaseDate, "GEO,dbGaP").generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generate() throws Exception {

        System.out.print("calling GenerateBioprojectsOmicsXML generate\n");

        if(bioprojectsClient == null)
            throw new Exception("bioprojectsClient is null");

        Collection<BioprojectDataset> datasets = bioprojectsClient.getAllDatasets().stream().filter(x -> x != null).collect(Collectors.toList());

        if (datasets == null || datasets.size() == 0) {
            System.out.print(String.format("bioprojectsClient.getAllDatasets() returned zero datasets\n"));
            return;
        }

        System.out.print(String.format("returned %d datasets\n",datasets.size()));

        for (String database_name : this.databases.split(",")) {
            List<Entry> entries = new ArrayList<>();

            System.out.print(String.format("processing database: %s \n",database_name));

            datasets.forEach( dataset -> {
                if(dataset != null && dataset.getIdentifier() != null && dataset.getRepository().equals(database_name)){
                    dataset.addOmicsType(Constants.GENOMICS_TYPE);
                   String accession = dataset.getIdentifier();
                   //List<Dataset> existingDatasets = this.datasetService.getBySecondaryAccession(accession);
                   if(this.datasetService.existsBySecondaryAccession(accession)){
                        //dataset already exists in OmicsDI, TODO: add some data
                        //this.datasetService.setDatasetNote();
                       System.out.print("accession "+ accession + " exists as secondary accession\n");
                   }
                   else{
                       entries.add(Transformers.transformAPIDatasetToEntry(dataset)); //
                   }
                }
            });

            System.out.print(String.format("found datasets: %d \n",entries.size()));

            String filepath = outputFolder + "/" + database_name + "_data.xml";
            FileWriter paxdbFile = new FileWriter(filepath);

            OmicsDataMarshaller mm = new OmicsDataMarshaller();

            Database database = new Database();
            //database.setDescription(Constants.GEO_DESCRIPTION);
            database.setName(database_name); //Constants.GEO
            database.setRelease(releaseDate);
            database.setEntries(entries);
            database.setEntryCount(entries.size());
            mm.marshall(database, paxdbFile);

            System.out.print(String.format("exported %s %d to %s\n",database_name,entries.size(),filepath));
        }
    }
}
