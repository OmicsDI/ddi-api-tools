package uk.ac.ebi.ddi.api.readers.px;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.model.IGenerator;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.api.readers.px.utils.ReaderPxXML;
import uk.ac.ebi.ddi.api.readers.utils.Transformers;
import uk.ac.ebi.ddi.api.readers.px.utils.ReadProperties;
import uk.ac.ebi.ddi.api.readers.px.xml.io.PxReader;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * This program takes a ProteomeXchange URL and generate for all the experiments the
 *
 * @author Yasset Perez-Riverol
 */
public class GeneratePxOmicsXML implements IGenerator {

    private static HashMap<String, String> pageBuffer = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePxOmicsXML.class);

    private static final String PXSUBMISSION_PATTERN = "<ProteomeXchangeDataset";

    private List<String> databases = Arrays.asList("PRIDE", "MassIVE", "PeptideAtlas", "jPOST", "iProX");
    //"PRIDE", "MassIVE", "PeptideAtlas", "jPOST",
    public int loopGap;

    public int endPoint;

    public String pxPrefix;

    public String pxURL;

    public String outputFolder;

    public String releaseDate;


    public GeneratePxOmicsXML(int loopGap, int endPoint, String pxPrefix, String pxURL, String outputFolder,
                              String releaseDate) {
        this.loopGap = loopGap;
        this.endPoint = endPoint;
        this.pxPrefix = pxPrefix;
        this.pxURL = pxURL;
        this.outputFolder = outputFolder;
        this.releaseDate = releaseDate;
    }

    public GeneratePxOmicsXML(int loopGap, int endPoint, String pxPrefix, String pxURL, String outputFolder,
                              List<String> databases, String releaseDate) {
        this.loopGap = loopGap;
        this.endPoint = endPoint;
        this.pxPrefix = pxPrefix;
        this.pxURL = pxURL;
        this.outputFolder = outputFolder;
        this.releaseDate = releaseDate;
        this.databases = databases;
    }

    public void generate() throws Exception {

        int initialGap = loopGap;
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < endPoint && loopGap > 0; i++) {

            String pxID = (pxPrefix + String.valueOf(i));
            pxID = pxID.substring(pxID.length() - 6, pxID.length());
            String pxURLProject = String.format(pxURL, pxID);
            String page = getPage(pxURLProject);
            if (page != null && isDataset(page)) {
                PxReader dataset = ReaderPxXML.parseDocument(page);
                if (dataset != null && dataset.getRepository() != null && databases.contains(dataset.getRepository())) {
                    dataset.setAccession("PXD" + pxID);
                    entries.add(Transformers.transformAPIDatasetToEntry(dataset));
                    LOGGER.debug(dataset.getIdentifier());
                }
                loopGap = initialGap;

            } else {
                loopGap--;
                LOGGER.debug(loopGap + "| LOGGER GAP CHANGE|");
            }
        }

        FileWriter pxdbFile = new FileWriter(outputFolder + "/px_data.xml");

        OmicsDataMarshaller mm = new OmicsDataMarshaller();

        Database database = new Database();
        database.setDescription(Constants.PX_DESCRIPTION);
        if (databases.size() > 0) {
            database.setName(databases.get(0));
        } else {
            database.setName(Constants.PX_DATABASE);
        }
        database.setRelease(releaseDate);
        database.setEntries(entries);
        database.setEntryCount(entries.size());
        mm.marshall(database, pxdbFile);
        LOGGER.info("Search for Files has been FINISHED!!");
    }

    /**
     * Gets the page from the given address. Returns the
     * retrieved page as a string.
     *
     * @param urlString The address of the resource to retrieve.
     * @return The page as a String
     * @throws Exception Thrown on any problem.
     */
    public static String getPage(String urlString) {
        // check if the page is cached

        try {
            if (pageBuffer.containsKey(urlString)) {
                return pageBuffer.get(urlString);
            }

            // create the url
            URL url = new URL(urlString);

            // send the request
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(10000); //set timeout to 10 seconds

            connection.setReadTimeout(300000); // set timeout to 10 seconds

            connection.connect();

            // get the page
            BufferedReader in;
            StringBuilder page = new StringBuilder();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                page.append(line);
                page.append("\n");
            }

            return page.toString();
        } catch (Exception ioe) {
            LOGGER.warn("Failed to read web page");
        }
        LOGGER.debug(urlString);
        return null;
    }

    private static boolean isPRIDEDataset(String pxSubmission) {
        String pridePattern = "hostingRepository=\"PRIDE\"";
        return pxSubmission.contains(pridePattern);
    }

    private static boolean isDataset(String pxSubmission) {
        return pxSubmission.contains(PXSUBMISSION_PATTERN);
    }

    /**
     * This program take an output folder as a parameter an create different EBE eyes files for
     * all the project in ProteomeXchange. It loop all the project in ProteomeCentral and print them to the give output
     *
     * @param args
     */
    public static void main(String[] args) {

        String outputFolder = null;
        String releaseDate = null;

        //String outputFolder = "/partition/tmp/omics/original/iprox";
        //String releaseDate = "20220809";

        if (args != null && args.length > 1 && args[0] != null) {
            outputFolder = args[0];
            releaseDate = args[1];
        } else {
            System.exit(-1);
        }
        try {

            String pxURL = ReadProperties.getInstance().getProperty("pxURL");
            String pxPrefix = ReadProperties.getInstance().getProperty("pxPrefix");
            Integer endPoint = Integer.valueOf(ReadProperties.getInstance().getProperty("pxEnd"));
            Integer loopGap = Integer.valueOf(ReadProperties.getInstance().getProperty("loopGap"));

            GeneratePxOmicsXML generator = new GeneratePxOmicsXML(loopGap, endPoint, pxPrefix, pxURL,
                    outputFolder, releaseDate);
            generator.generate();

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }

    }
}
