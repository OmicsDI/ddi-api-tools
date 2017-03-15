package uk.ac.ebi.ddi.api.readers.massive.ws.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ebi.ddi.api.readers.utils.HttpDownload;
import uk.ac.ebi.ddi.api.readers.ws.AbstractClient;
import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;
import uk.ac.ebi.ddi.api.readers.massive.ws.model.MassiveDatasetList;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 09/11/15
 */
public class ISODetasetsWsClient extends AbstractClient {

    private ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    /**
     * Default constructor for Archive clients
     *
     * @param config
     */
    public ISODetasetsWsClient(AbstractWsConfig config) {
        super(config);
    }

    public MassiveDatasetList getAllDatasets(){
        String url = String.format("%s://%s/datasets_json.jsp", config.getProtocol(), config.getHostName());
        try {
            InputStream in = HttpDownload.getPage(url);
            InputStreamReader isoInput = new InputStreamReader(in, Charset.forName("UTF-8"));
            return mapper.readValue(isoInput, MassiveDatasetList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
