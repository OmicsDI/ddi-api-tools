package uk.ac.ebi.ddi.api.readers.bioprojects.ws.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.BioprojectDataset;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.PlatformFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SampleFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SeriesFile;
import uk.ac.ebi.ddi.api.readers.utils.XMLUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by azorin on 12/01/2018.
 */
public class BioprojectsFileReader implements Runnable{
    private final String filePath;
    private final List<String> files; //files to process
    private final GeoClient geoClient;
    public List<BioprojectDataset> results = new ArrayList<BioprojectDataset>();
    private static final Logger logger = LoggerFactory.getLogger(BioprojectsClient.class);

    BioprojectsFileReader(String filePath, List<String> files, GeoClient geoClient){
        this.filePath = filePath;
        this.files = files;
        this.geoClient = geoClient;
    }

    public void readIds(){

        logger.info("file size is " + files.size());
        int fileCount = 0;
        for (String ID : files) {
            try {
                File f = new File(filePath + "/" + ID + ".xml");
                logger.info("file number is " + fileCount + "and count is "+ID);
                if (!Files.exists(f.toPath())) {
                    URL website = new URL("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=bioproject&id=" + ID);
                    try (InputStream in = website.openStream()) {
                        Path targetPath = f.toPath();
                        Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        Thread.sleep(400);
                        logger.info(String.format("Downloaded NCBI file %s \n",ID));
                    }
                }
                fileCount++;
                logger.info(String.format("reading file %s\n", f.getName()));

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
                if ((null != omicsType) && omicsType.contains("Transcriptome"))
                    dataset.addOmicsType("Transcriptomics");

                String organismName = XMLUtils.readFirstElement(doc, "ProjectType/ProjectTypeSubmission/Target/Organism/OrganismName");
                if (null != organismName)
                    dataset.addSpecies(organismName);

                dataset.setRepository(database);

                if (null != id)
                    dataset.setIdentifier(id);

                if (null != title)
                    dataset.setName(title);

                if (null != description)
                    dataset.setDescription(description);

                if (null != publicationDate) {

                    String[] datePart = publicationDate.split("T");
                    if (datePart.length > 0) {
                        publicationDate = datePart[0];
                    }
                    if (null != publicationDate) {
                        dataset.setPublicationDate(publicationDate);
                    }
                }

                if (database.equals("GEO")) {
                    try {
                        SeriesFile series = geoClient.getSeries(id);
                        if (null != series.getSeriesSuplimentraryFile()) {
                            for (String file : series.getSeriesSuplimentraryFile()) {
                                dataset.addDatasetFile(file);
                            }
                        }

                        if (null != series.getSeriesContactName()) {
                            for (String v : series.getSeriesContactName()) {
                                //remove commas
                                dataset.addSubmitter(v.replace(",", " ").replace("  ", " "));
                            }
                        }

                        if (null != series.getSeriesContactEmail()) {
                            for (String v : series.getSeriesContactEmail()) {
                                //split by commas
                                for (String v1 : v.split(",")) {
                                    if (!v1.isEmpty())
                                        dataset.addSubmitterEmail(v1);
                                }
                            }
                        }

                        if (null != series.getSeriesContactInstitute()) {
                            for (String v : series.getSeriesContactInstitute()) {
                                dataset.addSubmitterAffiliations(v);
                            }
                        }


                        if (null != series.getPubmedId()) {
                            dataset.addCrossReference("pubmed", series.getPubmedId());
                        }


                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat parser = new SimpleDateFormat("MMM d yyyy");

                    String status = series.getStatus();
                    if(null!=status){
                        try {

                            String input = status.replace("Public on ", ""); //"Aug 11 2009";

                            Date date = parser.parse(input);

                            String formattedDate = formatter.format(date);

                            dataset.addDate("publication", formattedDate);
                        }
                        catch(ParseException exception){
                            System.out.print("cannot parse date:"+exception.getMessage());
                        }
                    }

                    String submissionDate = series.getSubmissionDate();
                    if(null != submissionDate){
                        try {
                            Date date = parser.parse(submissionDate);

                            String formattedDate = formatter.format(date);

                            dataset.addDate("submission", formattedDate);
                        }
                        catch(ParseException exception){
                            logger.error("cannot parse date:"+exception.getMessage());
                        }
                    }

                    String platformId = series.getPlatformId();

                    PlatformFile platformFile = geoClient.getPlatform(platformId);

                    String instrument = platformFile.get_Title();

                    dataset.addInstrument(instrument);

                    dataset.setFullLink("https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=" + id);


                    if (null != series.getSampleIds()) {
                        if (series.getSampleIds().size() > 0) {
                            String celltype = "";

                            String sampleId = series.getSampleIds().get(0);

                            SampleFile sample = geoClient.getSample(sampleId);

                            celltype = sample.getCellType();

                            dataset.addCellType(celltype);

                            dataset.setDataProtocol(sample.getDataProtocol());

                            dataset.setSampleProtocol(sample.getSampleProtocol());

                            logger.info(String.format("download 1 of %d sampleIds celltype: %s \n", series.getSampleIds().size(), celltype));
                        }
                    }
                    }
                    catch(Exception ex){
                        logger.error("error in sries " + ex.getMessage());
                    }
                } else if (database.equals("dbGaP")){
                    dataset.setFullLink("https://www.ncbi.nlm.nih.gov/projects/gap/cgi-bin/study.cgi?study_id=" + id);
                }

                //Thread.sleep(1000);
                results.add(dataset);

            } catch (Exception ex) {
                logger.error("Error processing " + ID + " : " + ex.getMessage() + " " + ex.getStackTrace());
            }
        }
    }


    @Override
    public void run(){
        readIds();
    }
}


