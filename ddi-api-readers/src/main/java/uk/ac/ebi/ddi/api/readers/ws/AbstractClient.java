package uk.ac.ebi.ddi.api.readers.ws;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Abstract client to query the EBI search.
 *
 * @author ypriverol
 */
public abstract class AbstractClient {

    protected RestTemplate restTemplate;

    protected AbstractWsConfig config;

    /**
     * Default constructor for All clients
     * @param config
     */
    public AbstractClient(AbstractWsConfig config){
        this.config = config;
        this.restTemplate = new RestTemplate(clientHttpRequestFactory());
    }

    /**
     * Create Default HttpRequestFactory using default TimeOut
     * @return ClientHttpRequestFactory
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        int timeOut = 20000;
        factory.setReadTimeout(timeOut);
        factory.setConnectTimeout(timeOut);
        return factory;
    }

}
