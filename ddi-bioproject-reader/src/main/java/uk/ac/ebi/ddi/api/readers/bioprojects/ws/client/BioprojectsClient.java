package uk.ac.ebi.ddi.api.readers.bioprojects.ws.client;

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
    private static final Integer NUMBER_OF_THREADS = 100;

    public BioprojectsClient(String filePath, GeoClient geoClient){
        this.filePath = filePath;
        this.geoClient = geoClient;
    }

    public<T> List<List<T>> SplitArray(T[] array,Integer number){
        List<List<T>> result = new ArrayList<List<T>>();
        for(int j =0; j!= number; j++){
            result.add(new ArrayList<T>());
        }
        int j = 0;
        for(int i =0; i!= array.length; i++){
            result.get(j).add(array[i]);
            if(j==number-1) //last one
                j=0;
            else
                j++;
        }
        return result;
    }

    public Collection<BioprojectDataset> getAllDatasets() throws Exception {

        Map<String, BioprojectDataset> paxDBDatasets = new HashMap<>();

        File dir = new File(filePath);
        FileFilter fileFilter = new WildcardFileFilter("PRJNA*.xml");

        System.out.print(String.format("reading %s file mask %s \n",filePath , fileFilter));

        File[] allFiles = dir.listFiles(fileFilter);
        List<List<File>> allFilesForThreads = SplitArray(allFiles,NUMBER_OF_THREADS);

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        List<BioprojectsFileReader> readers = new ArrayList<BioprojectsFileReader>();

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {

            System.out.print(String.format("starting reader %d with %d files \n", i , allFilesForThreads.get(i).size()));

            BioprojectsFileReader worker = new BioprojectsFileReader(allFilesForThreads.get(i),geoClient);
            readers.add(worker);
            executor.execute(worker);
        }
        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {

        }

        for(BioprojectsFileReader reader : readers){
            for(BioprojectDataset dataset : reader.results){
                paxDBDatasets.put(dataset.getIdentifier(), dataset);
            }
        }

        System.out.print(String.format("all readers finished with %d results \n", paxDBDatasets.size()));

        return paxDBDatasets.values();

        }
    }

