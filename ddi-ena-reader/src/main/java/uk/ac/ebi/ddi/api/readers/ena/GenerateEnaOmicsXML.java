package uk.ac.ebi.ddi.api.readers.ena;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.ddi.api.readers.ena.ws.client.EnaClient;
import uk.ac.ebi.ddi.api.readers.ena.ws.model.EnaDataset;
import uk.ac.ebi.ddi.api.readers.model.IGenerator;
import uk.ac.ebi.ddi.api.readers.utils.Transformers;
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
 * @date 14/12/2017
 */

public class GenerateEnaOmicsXML implements IGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateEnaOmicsXML.class);

    String outputFolder;

    String releaseDate;

    EnaClient enaClient;

    String databases;

    public GenerateEnaOmicsXML(EnaClient enaClient, String outputFolder, String releaseDate, String databases) {
        this.enaClient = enaClient;
        this.outputFolder = outputFolder;
        this.releaseDate = releaseDate;
        this.databases = databases;
    }

    /**
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
        EnaClient enaClient = (EnaClient) ctx.getBean("enaClient");

        try {
            new GenerateEnaOmicsXML(enaClient, outputFolder, releaseDate, "ENA").generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generate() throws Exception {

        LOGGER.info("calling GenerateEnaOmicsXML generate");

        if (enaClient == null) {
            throw new Exception("enaClient is null");
        }

        Collection<EnaDataset> datasets = enaClient.getAllDatasets().stream()
                .filter(x -> x != null).collect(Collectors.toList());

        if (datasets.size() == 0) {
            LOGGER.info("enaClient.getAllDatasets() returned zero datasets");
            return;
        }

        LOGGER.info("returned {} datasets", datasets.size());

        List<Entry> entries = new ArrayList<>();

        String databaseName = "ENA";

        System.out.print(String.format("processing database: %s \n", databaseName));

        datasets.forEach(dataset -> {
            if (dataset != null && dataset.getIdentifier() != null && dataset.getRepository().equals(databaseName)) {
                entries.add(Transformers.transformAPIDatasetToEntry(dataset)); //
            }
        });

        System.out.print(String.format("found datasets: %d \n", entries.size()));

        String filepath = outputFolder + "/" + databaseName + "_data.xml";
        FileWriter outputFile = new FileWriter(filepath);

        OmicsDataMarshaller mm = new OmicsDataMarshaller();

        Database database = new Database();
        //database.setDescription(Constants.GEO_DESCRIPTION);
        database.setName(databaseName); //Constants.GEO
        database.setRelease(releaseDate);
        database.setEntries(entries);
        database.setEntryCount(entries.size());
        mm.marshall(database, outputFile);

        System.out.print(String.format("exported %s %d to %s\n", databaseName, entries.size(), filepath));
    }
}
