package uk.ac.ebi.ddi.api.readers.massive.ws.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.api.readers.massive.ws.model.MassiveDatasetList;
import uk.ac.ebi.ddi.api.readers.utils.HttpDownload;
import uk.ac.ebi.ddi.api.readers.ws.AbstractClient;
import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;


/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 09/11/15
 */
public class ISODetasetsWsClient extends AbstractClient {

    private ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    private static final Logger LOGGER = LoggerFactory.getLogger(ISODetasetsWsClient.class);

    /**
     * Default constructor for Archive clients
     *
     * @param config
     */
    public ISODetasetsWsClient(AbstractWsConfig config) {
        super(config);
    }

    public MassiveDatasetList getAllDatasets() {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme(config.getProtocol())
                .host(config.getHostName())
                .path("/ProteoSAFe")
                .path("/datasets_json.jsp");
        URI uri = builder.build().encode().toUri();
        try {
            InputStream in = HttpDownload.getPage(uri);
            InputStreamReader isoInput = new InputStreamReader(in, Charset.forName("UTF-8"));
            return mapper.readValue(isoInput, MassiveDatasetList.class);
        } catch (Exception e) {
            LOGGER.error("Exception occurred when fetching all datasets, ", e);
        }
        return null;
    }
}
