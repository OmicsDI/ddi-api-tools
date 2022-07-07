package uk.ac.ebi.ddi.api.readers.mw.ws.client;

import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;

/**
 * This class help to configure the web-service provider that would be used.
 */
public class MWWsConfigProd extends AbstractWsConfig {

    public MWWsConfigProd() {
        super("https", "www.metabolomicsworkbench.org");
    }
}
