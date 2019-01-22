package uk.ac.ebi.ddi.api.readers.bioprojects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.client.BioprojectsClient;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.BioprojectDataset;
import uk.ac.ebi.ddi.api.readers.model.IGenerator;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.api.readers.utils.Transformers;
import uk.ac.ebi.ddi.service.db.service.dataset.DatasetService;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author Andrey Zorin (azorin@ebi.ac.uk)
 * @date 14/11/2017
 */

public class GenerateBioprojectsOmicsXML implements IGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateBioprojectsOmicsXML.class);

    private String outputFolder;

    private String releaseDate;

    private BioprojectsClient bioprojectsClient;

    private DatasetService datasetService;

    private String databases;

    private OmicsDataMarshaller mm = new OmicsDataMarshaller();

    public GenerateBioprojectsOmicsXML(BioprojectsClient bioprojectsClient,
                                       DatasetService datasetService,
                                       String outputFolder,
                                       String releaseDate,
                                       String databases) {
        this.bioprojectsClient = bioprojectsClient;
        this.datasetService = datasetService;
        this.outputFolder = outputFolder;
        this.releaseDate = releaseDate;
        this.databases = databases;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        if (args == null || args.length < 2 || args[0] == null) {
            System.exit(-1);
        }

        String outputFolder = args[0];
        String releaseDate = args[1];

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/app-context.xml");
        BioprojectsClient bioprojectsClient = (BioprojectsClient) ctx.getBean("bioprojectsClient");
        DatasetService datasetService = (DatasetService) ctx.getBean("DatasetService");

        try {
            LOGGER.info("Output folder is {}", outputFolder);
            GenerateBioprojectsOmicsXML omicsXML = new GenerateBioprojectsOmicsXML(
                    bioprojectsClient, datasetService, outputFolder, releaseDate, "GEO,dbGaP");
            omicsXML.generate();
        } catch (Exception e) {
            LOGGER.error("Exception occurred during initializing the application, ", e);
        }
    }

    @Override
    public void generate() throws Exception {

        LOGGER.info("Calling GenerateBioprojectsOmicsXML generate");

        if (bioprojectsClient == null) {
            throw new Exception("bioprojectsClient is null");
        }

        List<BioprojectDataset> datasets = bioprojectsClient.getAllDatasets()
                .stream().filter(Objects::nonNull).collect(Collectors.toList());

        LOGGER.info("All datasets count is " + datasets.size());

        if (datasets.size() == 0) {
            LOGGER.info("bioprojectsClient.getAllDatasets() returned zero datasets");
            return;
        }

        LOGGER.info("Returned {} datasets", datasets.size());

        LOGGER.info("Starting to insert datasets...");

        for (String databaseName : databases.split(",")) {
            List<Entry> entries = new ArrayList<>();

            LOGGER.info("Processing database: {} ", databaseName);

            datasets.forEach(dataset -> {
                if (dataset.getIdentifier() != null && dataset.getRepository().equals(databaseName)) {
                    dataset.addOmicsType(Constants.GENOMICS_TYPE);
                    String accession = dataset.getIdentifier();
                    //List<Dataset> existingDatasets = this.datasetService.getBySecondaryAccession(accession);
                    if (datasetService.existsBySecondaryAccession(accession)) {
                        //dataset already exists in OmicsDI, TODO: add some data
                        //this.datasetService.setDatasetNote();
                        LOGGER.info("Accession " + accession + " exists as secondary accession");
                    } else {
                        entries.add(Transformers.transformAPIDatasetToEntry(dataset)); //
                    }
                }
            });

            LOGGER.info("Found datasets entries : {}", entries.size());

            String filepath = outputFolder + "/" + databaseName + "_data.xml";

            LOGGER.info("Filepath is " + filepath);
            FileWriter paxdbFile = new FileWriter(filepath);

            Database database = new Database();
            //database.setDescription(Constants.GEO_DESCRIPTION);
            database.setName(databaseName); //Constants.GEO
            database.setRelease(releaseDate);
            database.setEntries(entries);
            database.setEntryCount(entries.size());
            LOGGER.info("Writing bioproject file at location " + filepath);
            mm.marshall(database, paxdbFile);
            LOGGER.info(String.format("Exported %s %d to %s", databaseName, entries.size(), filepath));
        }
    }
}
