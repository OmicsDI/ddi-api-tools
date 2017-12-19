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

/**
 * @author Andrey Zorin (azorin@ebi.ac.uk)
 * @date 14/11/2017
 */

public class BioprojectsClient {

    private String filePath;
    private GeoClient geoClient;

    private static final Logger logger = LoggerFactory.getLogger(BioprojectsClient.class);

    public BioprojectsClient(String filePath, GeoClient geoClient){
        this.filePath = filePath;
        this.geoClient = geoClient;
    }

    public Collection<BioprojectDataset> getAllDatasets() throws Exception {

        Map<String, BioprojectDataset> paxDBDatasets = new HashMap<>();

        File dir = new File(filePath);
        FileFilter fileFilter = new WildcardFileFilter("PRJNA*.xml");

        System.out.print(String.format("reading %s file mask %s \n",filePath , fileFilter));

        for (File f : dir.listFiles(fileFilter)){
            try {

                System.out.print(String.format("reading file %s\n",f.getName()));

                ///if(!(f.getName().equals("PRJNA100001.xml")))
                ///    continue;

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(f);

                BioprojectDataset dataset = new BioprojectDataset();

                String database = XMLUtils.readFirstAttribute(doc, "dbXREF", "db");
                if (null == database)
                    continue;

                String id = XMLUtils.readFirstElement(doc, "dbXREF/ID");
                String title = XMLUtils.readFirstElement(doc, "ProjectDescr/Title");
                String description = XMLUtils.readFirstElement(doc, "ProjectDescr/Description");
                String publicationDate = XMLUtils.readFirstElement(doc, "ProjectDescr/ProjectReleaseDate");

                String omicsType = XMLUtils.readFirstElement(doc, "ProjectType/ProjectTypeSubmission/ProjectDataTypeSet/DataType");
                if((null!=omicsType) && omicsType.contains("Transcriptome"))
                    dataset.addOmicsType("Transcriptomics");

                String organismName = XMLUtils.readFirstElement(doc,"ProjectType/ProjectTypeSubmission/Target/Organism/OrganismName");
                if(null!=organismName)
                    dataset.addSpecies(organismName);

                dataset.setRepository(database);

                if(null!=id)
                    dataset.setIdentifier(id);

                if(null!=title)
                    dataset.setName(title);

                if(null!=description)
                    dataset.setDescription(description);

                if(null!=publicationDate) {

                    String[] datePart = publicationDate.split("T");
                    if(datePart.length>0) {
                        publicationDate = datePart[0];
                    }
                    if(null!=publicationDate) {
                        dataset.setPublicationDate(publicationDate);
                    }
                }

                if(database.equals("GEO")){
                    SeriesFile series = geoClient.getSeries(id);
                    if(null!=series.getSeriesSuplimentraryFile()) {
                        for (String file : series.getSeriesSuplimentraryFile()) {
                            dataset.addDatasetFile(file);
                        }
                    }

                    if(null!=series.getSeriesContactName()) {
                        for (String v : series.getSeriesContactName()) {
                            //remove commas
                            dataset.addSubmitter(v.replace(","," ").replace("  "," "));
                        }
                    }

                    if(null!=series.getSeriesContactEmail()) {
                        for (String v : series.getSeriesContactEmail()) {
                            //split by commas
                            for(String v1 : v.split(",")) {
                                if(!v1.isEmpty())
                                    dataset.addSubmitterEmail(v1);
                            }
                        }
                    }

                    if(null!=series.getSeriesContactInstitute()) {
                        for (String v : series.getSeriesContactInstitute()) {
                            dataset.addSubmitterAffiliations(v);
                        }
                    }

                    String platformId = series.getPlatformId();

                    PlatformFile platformFile = geoClient.getPlatform(platformId);

                    String instrument = platformFile.get_Title();

                    dataset.addInstrument(instrument);

                    dataset.setFullLink("https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc="+id);


                    if(null!=series.getSampleIds()) {
                        if(series.getSampleIds().size()>0) {
                            String celltype = "";

                            String sampleId = series.getSampleIds().get(0);

                            SampleFile sample = geoClient.getSample(sampleId);

                            celltype = sample.getCellType();

                            dataset.addCellType(celltype);

                            dataset.setDataProtocol(sample.getDataProtocol());

                            dataset.setSampleProtocol(sample.getSampleProtocol());

                            System.out.print(String.format("download 1 of %d sampleIds celltype: %s \n", series.getSampleIds().size(), celltype));
                        }
                    }
                }
                paxDBDatasets.put(id, dataset);
            } catch(Exception ex){
                logger.error("Error processing " + f.getName() + " : " + ex);
            }
        }

        System.out.print(String.format("found %d datasets\n",paxDBDatasets.size()));

        return paxDBDatasets.values();
    }

}
