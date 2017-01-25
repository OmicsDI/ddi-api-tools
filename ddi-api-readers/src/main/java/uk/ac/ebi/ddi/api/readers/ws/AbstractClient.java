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

    /**
     * Return the ResTemplate need to connect Http
     * @return
     */
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    /**
     * Set a new ResTemplate
     * @param restTemplate
     */
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Return the AbstractWsConfig
     * @return AbstractWsConfig
     */
    public AbstractWsConfig getConfig() {
        return config;
    }

    /**
     * Set a new Config
     * @param config
     */
    public void setConfig(AbstractWsConfig config) {
        this.config = config;
    }
}
