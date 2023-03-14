package uk.ac.ebi.ddi.api.readers.lincs.ws.client;

import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;

/**
 * This class help to configure the web-service provider that would be used.
 */
public class LINCSConfigProd extends AbstractWsConfig {

    public LINCSConfigProd() {
        super("https", "lincsportal.ccs.miami.edu");
    }
}
