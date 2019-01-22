package uk.ac.ebi.ddi.api.readers.paxdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.ddi.api.readers.model.IGenerator;
import uk.ac.ebi.ddi.api.readers.paxdb.ws.client.PaxDBClient;
import uk.ac.ebi.ddi.api.readers.paxdb.ws.model.PaxDBDataset;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.api.readers.utils.Transformers;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * This project takes class Retrieve information from GPMDB, it allows to retrieve
 * the proteins ids for an specific model, etc.
 *
 * @author Yasset Perez-Riverol
 */

public class GeneratePaxDBOmicsXML implements IGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePaxDBOmicsXML.class);

    PaxDBClient paxDBClient;

    String outputFolder;

    String releaseDate;

    public GeneratePaxDBOmicsXML(PaxDBClient paxDBClient, String outputFolder, String releaseDate) {
        this.paxDBClient = paxDBClient;
        this.outputFolder = outputFolder;
        this.releaseDate = releaseDate;
    }

    /**
     * This program take an output folder as a parameter an create different EBE eyes files for
     * all the project in MetabolomicsWorkbench. It loop all the project in MetabolomeWorkbench and
     * print them to the give output
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
        PaxDBClient paxDBClient = (PaxDBClient) ctx.getBean("paxdbClient");

        try {
            new GeneratePaxDBOmicsXML(paxDBClient, outputFolder, releaseDate).generate();
        } catch (Exception e) {
            LOGGER.error("Exception occurred, ", e);
        }
    }

    @Override
    public void generate() throws Exception {

        List<Entry> entries = new ArrayList<>();
        if (paxDBClient != null) {
            Collection<PaxDBDataset> datasets = paxDBClient.getAllDatasets()
                    .stream().filter(Objects::nonNull).collect(Collectors.toList());
            if (datasets.size() > 0) {
                datasets.forEach(dataset -> {
                    if (dataset != null && dataset.getIdentifier() != null) {
                        entries.add(Transformers.transformAPIDatasetToEntry(dataset));
                        LOGGER.info(dataset.getIdentifier());
                    }
                });
            }

        }

        FileWriter paxdbFile = new FileWriter(outputFolder + "/paxdb_data.xml");

        OmicsDataMarshaller mm = new OmicsDataMarshaller();

        Database database = new Database();
        database.setDescription(Constants.PAXDB_DESCRIPTION);
        database.setName(Constants.PAXDB);
        database.setRelease(releaseDate);
        database.setEntries(entries);
        database.setEntryCount(entries.size());
        mm.marshall(database, paxdbFile);
    }

}
