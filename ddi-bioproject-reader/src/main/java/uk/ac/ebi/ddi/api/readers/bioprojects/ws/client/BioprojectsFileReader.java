package uk.ac.ebi.ddi.api.readers.bioprojects.ws.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.BioprojectDataset;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.PlatformFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SampleFile;
import uk.ac.ebi.ddi.api.readers.bioprojects.ws.model.SeriesFile;
import uk.ac.ebi.ddi.api.readers.utils.XMLUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * Created by azorin on 12/01/2018.
 */
public class BioprojectsFileReader {
    private final GeoClient geoClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(BioprojectsClient.class);
    private RetryTemplate template = new RetryTemplate();
    private RestTemplate restTemplate = new RestTemplate();
    private static final String NCBI_ENDPOINT = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi";
    private static final int RETRIES = 20;
    private static final int PARALLEL = Math.min(1, Runtime.getRuntime().availableProcessors());
    private TransformerFactory transformerFactory = TransformerFactory.newInstance();
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    BioprojectsFileReader(GeoClient geoClient) {
        SimpleRetryPolicy policy =
                new SimpleRetryPolicy(RETRIES, Collections.singletonMap(Exception.class, true));
        template.setRetryPolicy(policy);
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000);
        backOffPolicy.setMultiplier(1.6);
        template.setBackOffPolicy(backOffPolicy);
        this.geoClient = geoClient;
    }

    private String readAllDatasets(List<String> files) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NCBI_ENDPOINT)
                .queryParam("db", "bioproject")
                .queryParam("id", String.join(",", files));
        return template.execute(context -> {
            HttpStatus status;
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(builder.build().toString(), String.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    return response.getBody();
                }
                status = response.getStatusCode();
            } catch (HttpStatusCodeException e) {
                status = e.getStatusCode();
            }
            String retryTimes = context.getRetryCount() + 1 + "/" + RETRIES;
            LOGGER.info("Retrying " + retryTimes + " to fetch {}, status: {}", builder.toUriString(), status.value());
            throw new Exception();
        });
    }

    private void addGeoAdditionInformations(BioprojectDataset dataset, String database) throws Exception {
        SeriesFile series;
        try {
            series = geoClient.getSeries(dataset.getIdentifier());
        } catch (Exception e) {
            LOGGER.error("Series file {} can't be read", dataset.getIdentifier());
            throw e;
        }
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
                    if (!v1.isEmpty()) {
                        dataset.addSubmitterEmail(v1);
                    }
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
        if (null != status) {
            try {
                String input = status.replace("Public on ", ""); //"Aug 11 2009";
                Date date = parser.parse(input);
                String formattedDate = formatter.format(date);
                dataset.addDate("publication", formattedDate);
            } catch (ParseException exception) {
                System.out.print("cannot parse date:" + exception.getMessage());
            }
        }

        String submissionDate = series.getSubmissionDate();
        if (null != submissionDate) {
            try {
                Date date = parser.parse(submissionDate);

                String formattedDate = formatter.format(date);

                dataset.addDate("submission", formattedDate);
            } catch (ParseException exception) {
                LOGGER.error("cannot parse date:" + exception.getMessage());
            }
        }
        String platformId = series.getPlatformId();
        if (null != platformId) {
            PlatformFile platformFile = geoClient.getPlatform(platformId);
            String instrument = platformFile.get_Title();
            dataset.addInstrument(instrument);
        }
        dataset.setFullLink("https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=" + dataset.getIdentifier());
        if (null != series.getSampleIds() && series.getSampleIds().size() > 0) {
            String sampleId = series.getSampleIds().get(0);
            SampleFile sample = geoClient.getSample(sampleId);
            String celltype = sample.getCellType();
            dataset.addCellType(celltype);
            dataset.setDataProtocol(sample.getDataProtocol());
            dataset.setSampleProtocol(sample.getSampleProtocol());
            LOGGER.info("Downloaded 1 of {} sampleIds celltype: {}", series.getSampleIds().size(), celltype);
        }
    }

    private BioprojectDataset parseDataset(Element datasetNode, XPath xPath) throws Exception {
        String database = XMLUtils.readFirstAttribute(
                datasetNode, "./ProjectDescr/ExternalLink/dbXREF", "db", xPath);
        BioprojectDataset dataset = new BioprojectDataset();
        String id = XMLUtils.readFirstElement(datasetNode, "./ProjectDescr/ExternalLink/dbXREF/ID", xPath);
        String title = XMLUtils.readFirstElement(datasetNode, "./ProjectDescr/Title", xPath);
        String description = XMLUtils.readFirstElement(datasetNode, "./ProjectDescr/Description", xPath);
        String pubDate = XMLUtils.readFirstElement(datasetNode, "./ProjectDescr/ProjectReleaseDate", xPath);

        String omicsType = XMLUtils.readFirstElement(
                datasetNode, "./ProjectType/ProjectTypeSubmission/ProjectDataTypeSet/DataType", xPath);
        if ((null != omicsType) && omicsType.contains("Transcriptome")) {
            dataset.addOmicsType("Transcriptomics");
        }

        String organismName = XMLUtils.readFirstElement(
                datasetNode, "./ProjectType/ProjectTypeSubmission/Target/Organism/OrganismName", xPath);
        if (null != organismName) {
            dataset.addSpecies(organismName);
        }
        dataset.setRepository(database);

        if (null != id) {
            dataset.setIdentifier(id);
        }

        if (null != title) {
            dataset.setName(title);
        }

        if (null != description) {
            dataset.setDescription(description);
        }

        if (null != pubDate) {
            String[] datePart = pubDate.split("T");
            if (datePart.length > 0) {
                pubDate = datePart[0];
            }
            dataset.setReleaseDate(pubDate);
        }

        if (database == null) {
            return dataset;
        } else if (database.equals("GEO")) {
            addGeoAdditionInformations(dataset, database);
        } else if (database.equals("dbGaP")) {
            dataset.setFullLink("https://www.ncbi.nlm.nih.gov/projects/gap/cgi-bin/study.cgi?study_id=" + id);
        }

        return dataset;
    }

    private void saveDatasetToFile(File file, Node datasetXml) throws Exception {
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Transformer transformer = transformerFactory.newTransformer();
        Document document = dBuilder.newDocument();
        Element root = document.createElement("RecordSet");
        document.appendChild(root);
        Node node = document.importNode(datasetXml, true);
        root.appendChild(node);
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(file);
        transformer.transform(domSource, streamResult);
    }

    private static final ThreadLocal<XPathFactory> XPATH_FACTORY = ThreadLocal.withInitial(XPathFactory::newInstance);

    private void processNode(Element datasetNode, String filePath, List<BioprojectDataset> results) {
        String accession = "Unable to get";
        try {
            // XPath is not thread-safe
            XPath xPath = XPATH_FACTORY.get().newXPath();
            accession = XMLUtils.readFirstAttribute(
                    datasetNode, "./ProjectID/ArchiveID", "accession", xPath);
            saveDatasetToFile(new File(filePath + "/" + accession + ".xml"), datasetNode);
            String database = XMLUtils.readFirstAttribute(
                    datasetNode, "./ProjectDescr/ExternalLink/dbXREF", "db", xPath);
            if (null != database) {
                results.add(parseDataset(datasetNode, xPath));
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred when processing dataset {}", accession, e);
        }
    }

    public List<BioprojectDataset> readIds(String filePath, List<String> files) {
        List<BioprojectDataset> results = new ArrayList<>();
        try {
            String allDatasetsContent = readAllDatasets(files);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(allDatasetsContent)));
            XPath xPath = XPATH_FACTORY.get().newXPath();
            NodeList datasetsXml = XMLUtils.findElements(doc, "/RecordSet/DocumentSummary/Project", xPath);
            int totalResults = datasetsXml.getLength();

            ForkJoinPool customThreadPool = new ForkJoinPool(PARALLEL);
            customThreadPool.submit(() -> IntStream.rangeClosed(0, totalResults - 1).parallel().forEach(i -> {
                processNode((Element) datasetsXml.item(i), filePath, results);
            })).get();
        } catch (Exception e) {
            LOGGER.error("Error processing : {}", e);
        }
        return results;
    }
}


