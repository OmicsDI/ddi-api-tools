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

    public GenerateBioprojectsOmicsXML(BioprojectsClient bioprojectsClient, String outputFolder, String releaseDate) {
        this.bioprojectsClient = bioprojectsClient;
        this.outputFolder = outputFolder;
        this.releaseDate = releaseDate;
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

        try {
            new GenerateBioprojectsOmicsXML(bioprojectsClient,outputFolder,releaseDate).generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generate() throws Exception {
        /*copypasted from PaxDB */
        List<Entry> entries     = new ArrayList<>();
        if(bioprojectsClient != null){
            Collection<BioprojectDataset> datasets = bioprojectsClient.getAllDatasets().stream().filter(x -> x != null).collect(Collectors.toList());
            if (datasets != null && datasets.size() > 0) {
                datasets.forEach( dataset -> {
                    if(dataset != null && dataset.getIdentifier() != null){
                        entries.add(Transformers.transformAPIDatasetToEntry(dataset)); //
                        logger.info(dataset.getIdentifier());
                    }
                });
            }

        }

        FileWriter paxdbFile = new FileWriter(outputFolder + "/bioprojects_data.xml");

        OmicsDataMarshaller mm = new OmicsDataMarshaller();

        Database database = new Database();
        database.setDescription(Constants.GEO_DESCRIPTION);
        database.setName("GEO"); //Constants.GEO
        database.setRelease(releaseDate);
        database.setEntries(entries);
        database.setEntryCount(entries.size());
        mm.marshall(database, paxdbFile);

    }

}
