package uk.ac.ebi.ddi.api.readers.bioprojects.ws.client;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.BioprojectDataset;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

/**
 * @author Andrey Zorin (azorin@ebi.ac.uk)
 * @date 14/11/2017
 */

public class BioprojectsClient {

    public String getFilePath() {
        return filePath;
    }

    private String filePath;
    private GeoClient geoClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(BioprojectsClient.class);
    private static final Integer BATCH_SIZE = 5;
    private static final String ONLY_NEWS = System.getenv("ONLY_NEWS");
    private static final String BIOPROJECT_ENDPOINT = "ftp://ftp.ncbi.nlm.nih.gov/bioproject/summary.txt";

    // Due to rate limit from eutils.ncbi.nlm.nih.gov
    private static final int PARALLEL = 3;

    public BioprojectsClient(String filePath, GeoClient geoClient) {
        this.filePath = filePath;
        this.geoClient = geoClient;
    }

    // Return list of IDs which was not downloaded
    private List<String> getNewIDs(String summaryPath) {

        List<String> result = new ArrayList<>();

        try {
            /*File f = new File(filePath + "/summary.txt");
            URL website = new URL(BIOPROJECT_ENDPOINT);
            try (InputStream in = website.openStream()) {
                Files.copy(in, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }*/

            //try (Stream<String> stream = Files.lines(f.toPath()))
            try (Stream<String> stream = Files.lines(new File(summaryPath).toPath())) {
                stream.forEach(line -> {
                    String accession = line.split("\t")[2];

                    if (accession.startsWith("PRJNA")) {
                        File f1 = new File(filePath + "/" + accession + ".xml");
                        if (!f1.exists() || (ONLY_NEWS != null && ONLY_NEWS.equals("false"))) {
                            result.add(accession);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR in getIDs, ", ex);
        }

        return result;
    }

    public Collection<BioprojectDataset> getAllDatasets(String summaryFilePath) throws Exception {

        Map<String, BioprojectDataset> paxDBDatasets = new ConcurrentHashMap<>();
        LOGGER.info("Getting new IDs from NCBI...");

        List<String> allFiles = getNewIDs(summaryFilePath);

        LOGGER.info("Getting new IDs from NCBI: {} received", allFiles.size());
        List<List<String>> allFilesForThreads = Lists.newArrayList(Iterables.partition(allFiles, BATCH_SIZE));

        ForkJoinPool customThreadPool = new ForkJoinPool(PARALLEL);

        BioprojectsFileReader reader = new BioprojectsFileReader(geoClient);
        customThreadPool.submit(() -> allFilesForThreads.stream().forEach(datasetBundle -> {
            for (BioprojectDataset dataset : reader.readIds(filePath, datasetBundle)) {
                paxDBDatasets.put(dataset.getIdentifier(), dataset);
            }
            LOGGER.info("Founded {} new datasets", paxDBDatasets.size());
        })).get();

        LOGGER.info("All readers finished with {} results", paxDBDatasets.size());

        return paxDBDatasets.values();
    }
}

