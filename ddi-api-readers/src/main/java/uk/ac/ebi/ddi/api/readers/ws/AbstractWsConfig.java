package uk.ac.ebi.ddi.api.readers.ws;

/**
 * Abstract API WS Config that can be use to connect the WS
 * @author ypriverol
 *
 */
public abstract class AbstractWsConfig {

    /**
     * Host Name with the WS
     */
    private String hostName;

    /**
     * Protocol http or https
     */
    private String protocol;

    protected AbstractWsConfig(String protocol, String hostName) {
        this.hostName = hostName;
        this.protocol = protocol;
    }

    public String getHostName() {
        return hostName;
    }


    public String getProtocol() {
        return protocol;
    }

}
