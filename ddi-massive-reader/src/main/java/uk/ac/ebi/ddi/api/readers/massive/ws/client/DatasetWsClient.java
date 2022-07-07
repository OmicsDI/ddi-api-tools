package uk.ac.ebi.ddi.api.readers.massive.ws.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.api.readers.ftp.AbstractFTPConfig;
import uk.ac.ebi.ddi.api.readers.ftp.FTPFileSearch;
import uk.ac.ebi.ddi.api.readers.ftp.MockCallback;
import uk.ac.ebi.ddi.api.readers.massive.ws.model.MassiveDatasetDetail;
import uk.ac.ebi.ddi.api.readers.model.IFilter;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;
import uk.ac.ebi.ddi.api.readers.utils.CustomHttpMessageConverter;
import uk.ac.ebi.ddi.api.readers.ws.AbstractClient;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


/**
 * @author Yasset Perez-Riverol ypriverol
 */
public class DatasetWsClient extends AbstractClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetWsClient.class);

    /**
     * Default constructor for Ws clients
     *
     * @param config
     */
    public DatasetWsClient(AbstractWsConfig config) {
        super(config);
        this.restTemplate.getMessageConverters().add(new CustomHttpMessageConverter());
    }


    /**
     * This function provides a way to retrieve the information of a dataset from Massive
     * Specially the metadata.
     *
     * @param task the id of the dataset
     * @return the MassiveDatasetDetail
     */
    public MassiveDatasetDetail getDataset(String task) {

        MassiveDatasetDetail massiveDatasetDetail = null;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                    .scheme(config.getProtocol())
                    .host(config.getHostName())
                    .path("/ProteoSAFe")
                    .path("/MassiveServlet")
                    .queryParam("task", task)
                    .queryParam("function", "massiveinformation");

            URI uri = builder.build().encode().toUri();

            massiveDatasetDetail = restTemplate.getForObject(uri, MassiveDatasetDetail.class);
        } catch (Exception ex) {
            LOGGER.error("exception in MassiveDatasetDetail getdataset ", ex.getMessage());
        }
        return massiveDatasetDetail;
    }

    public List<String> getFilePaths(String massiveID) throws IOException {

        AbstractFTPConfig ftpConfig = new AbstractFTPConfig("massive.ucsd.edu");
        List<String> listFiles = new ArrayList<>();
        ftpConfig.onConnect();
        IFilter[] filters = new IFilter[0];
        final Iterable<String> findings = new FTPFileSearch(
                massiveID, filters, true, new MockCallback<>()).ftpCall(ftpConfig.getClient());
        findings.forEach(s -> listFiles.add(
                new StringJoiner(Constants.PATH_DELIMITED)
                        .add(Constants.FTP_PROTOCOL + ftpConfig.getHost())
                        .add(s).toString()));
        return listFiles;
    }
}
