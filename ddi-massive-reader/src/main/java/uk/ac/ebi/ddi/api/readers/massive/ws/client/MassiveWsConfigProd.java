package uk.ac.ebi.ddi.api.readers.massive.ws.client;

import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;

/**
 * This class help to configure the web-service provider that
 * would be used.
 *
 * @ypriverol
 */

public class MassiveWsConfigProd extends AbstractWsConfig {
    public MassiveWsConfigProd() {
        super("http", "massive.ucsd.edu/ProteoSAFe");
    }
}
