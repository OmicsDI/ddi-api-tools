package uk.ac.ebi.ddi.api.readers.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.api.readers.utils.RetryClient;

/**
 * Abstract client to query the EBI search.
 *
 * @author ypriverol
 */
public abstract class AbstractClient extends RetryClient {

    protected RestTemplate restTemplate;

    protected AbstractWsConfig config;

    protected ObjectMapper objectMapper;

    /**
     * Default constructor for All clients
     * @param config
     */
    public AbstractClient(AbstractWsConfig config){
        this.config = config;
        restTemplate = new RestTemplate(clientHttpRequestFactory());
        objectMapper = new ObjectMapper();
    }

    /**
     * Create Default HttpRequestFactory using default TimeOut
     * @return ClientHttpRequestFactory
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        int timeOut = 200000;
        factory.setReadTimeout(timeOut);
        factory.setConnectTimeout(timeOut);
        return factory;
    }

}
