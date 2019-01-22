package uk.ac.ebi.ddi.api.readers.geo.datasets.ws.client;

import uk.ac.ebi.ddi.api.readers.ftp.AbstractFTPConfig;

/**
 * This code is licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * ==Overview==
 * <p>
 * This class
 * <p>
 * Created by ypriverol (ypriverol@gmail.com) on 24/01/2017.
 */
public class GEOFTPProd extends AbstractFTPConfig {

    /**
     * Class constructor.
     *
     * @param host     Hostname for FTP connection.
     * @param port     Port for FTP connection.
     * @param user     User logging to FTP.
     * @param password User password for FTP connection.
     * @checkstyle ParameterNumberCheck (2 lines)
     */
    public GEOFTPProd(String host, int port, String user, String password) {
        super(host, port, user, password);
    }

    public GEOFTPProd() {
       super("ftp.ncbi.nlm.nih.gov");
    }

}
