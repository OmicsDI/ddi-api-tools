package uk.ac.ebi.ddi.api.readers.gpmdb.ws.client;

import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;

/**
 * This class help to configure the web-service provider that would be used.
 */
public class GPMDBWsConfigProd extends AbstractWsConfig {

    public GPMDBWsConfigProd() {
        super("http", "rest.thegpm.org/1");
    }
}
