package uk.ac.ebi.ddi.api.readers.bioprojects.ws.client;

import com.sun.xml.bind.v2.model.core.ID;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import uk.ac.ebi.ddi.api.readers.utils.XMLUtils;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.BioprojectDataset;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.PlatformFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SampleFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SeriesFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Andrey Zorin (azorin@ebi.ac.uk)
 * @date 14/11/2017
 */

public class BioprojectsClient {

    private String filePath;
    private GeoClient geoClient;

    private static final Logger logger = LoggerFactory.getLogger(BioprojectsClient.class);
    private static final Integer NUMBER_OF_THREADS = 1;

    public BioprojectsClient(String filePath, GeoClient geoClient){
        this.filePath = filePath;
        this.geoClient = geoClient;
    }

    public<T> List<List<T>> SplitArray(List<T> array,Integer number){
        List<List<T>> result = new ArrayList<List<T>>();
        for(int j =0; j!= number; j++){
            result.add(new ArrayList<T>());
        }
        int j = 0;
        for(int i =0; i!= array.size(); i++){
            result.get(j).add(array.get(i));
            if(j==number-1) //last one
                j=0;
            else
                j++;
        }
        return result;
    }

    // return list of IDs which was not downloaded
    private List<String> getNewIDs(){

        List<String> result = new ArrayList<String>();

        try {
            File f = new File(filePath + "/summary.txt");

            URL website = new URL("ftp://ftp.ncbi.nlm.nih.gov/bioproject/summary.txt");
            try (InputStream in = website.openStream()) {
                Path targetPath = f.toPath();
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            FileReader reader = new FileReader(f);
            List<String> lines = Files.readAllLines(f.toPath(),
                    StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] array = line.split("\t");

                String ID = array[2];

                if(!ID.startsWith("PRJNA"))
                    continue;

                File f1 = new File(filePath + "/" + ID + ".xml");

                if (!Files.exists(f1.toPath())) {
                    result.add(array[2]);
                }
            }
        }
        catch(Exception ex){
            logger.error(String.format("ERROR in getIDs %s \n",ex.getMessage()));
        }

        return result;
    }

    public Collection<BioprojectDataset> getAllDatasets() throws Exception {

        Map<String, BioprojectDataset> paxDBDatasets = new HashMap<>();

        File dir = new File(filePath);
        FileFilter fileFilter = new WildcardFileFilter("PRJNA*.xml");

        logger.info("getting new IDs from NCBI \n");

        List<String> allFiles = getNewIDs();

        logger.info(String.format("getting new IDs from NCBI: %d received \n", allFiles.size()));

        List<List<String>> allFilesForThreads = SplitArray(allFiles,NUMBER_OF_THREADS);

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        List<BioprojectsFileReader> readers = new ArrayList<BioprojectsFileReader>();

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {

            List<String> files = allFilesForThreads.get(i);

            if((null==files)||(files.size()<1))
                continue;

            logger.info(String.format("starting reader %d with %d files \n", i , files.size()));

            BioprojectsFileReader worker = new BioprojectsFileReader(filePath, files,geoClient);
            worker.readIds();
            //readers.add(worker);
            //executor.execute(worker);
        }
        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {

        }

        for(BioprojectsFileReader reader : readers){
            if(null==reader.results)
                continue;

            for(BioprojectDataset dataset : reader.results){
                paxDBDatasets.put(dataset.getIdentifier(), dataset);
            }
        }

        logger.info(String.format("all readers finished with %d results \n", paxDBDatasets.size()));

        return paxDBDatasets.values();

        }
    }

