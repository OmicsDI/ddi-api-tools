package uk.ac.ebi.ddi.api.readers.lincs.ws.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.api.readers.lincs.ws.model.DatasetList;
import uk.ac.ebi.ddi.api.readers.utils.ISOHttpMessageConverter;
import uk.ac.ebi.ddi.api.readers.ws.AbstractClient;
import uk.ac.ebi.ddi.api.readers.utils.HttpDownload;
import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;


/**
 * @author Yasset Perez-Riverol ypriverol
 */
public class LINCSClient extends AbstractClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LINCSClient.class);
    private ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    /**
     * Default constructor for Ws clients
     *
     * @param config
     */
    public LINCSClient(AbstractWsConfig config) {
        super(config);
        this.restTemplate.getMessageConverters().clear();
        this.restTemplate.getMessageConverters().add(new ISOHttpMessageConverter());
    }

    /**
     * Returns the Datasets from MtabolomeWorbench
     *
     * @return A list of entries and the facets included
     */
    public DatasetList getAllDatasets() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme(config.getProtocol())
                .host(config.getHostName())
                .path("/dcic/api/fetchdata")
                .queryParam("searchTerm", "*")
                .queryParam("limit", "1000");
        URI uri = builder.build().encode().toUri();
        try {
            InputStream in = HttpDownload.getPage(uri);
            InputStreamReader isoInput = new InputStreamReader(in, Charset.forName("UTF-8"));
            return mapper.readValue(isoInput, DatasetList.class);
        } catch (Exception e) {
            LOGGER.error("Exception occurred when fetching all datasets", e);
        }
        return null;
    }

}
