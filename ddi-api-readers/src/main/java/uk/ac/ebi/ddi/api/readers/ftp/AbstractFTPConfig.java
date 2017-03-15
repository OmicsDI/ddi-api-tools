package uk.ac.ebi.ddi.api.readers.ftp;

import org.apache.commons.net.ftp.FTPClient;
import uk.ac.ebi.ddi.api.readers.model.IFTPCommand;


import java.io.IOException;

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
public class AbstractFTPConfig {

    /**
     * Host to connect to.
     */
    protected String host;
    /**
     * Host's port to connect to.
     */
    protected int port;
    /**
     * User connecting.
     */
    protected String usr;
    /**
     * User's password for connection.
     */
    protected String pass;
    /**
     * Apache FTP client wrapped.
     */
    protected FTPClient client;

    /**
     * Default Constructor
     */
    public AbstractFTPConfig(){

    }

    /**
     * Create Anonymous FTP client.
     * @param host
     */

    public AbstractFTPConfig(String host){
        this.host = host;
        this.pass = "";
        this.usr  = "anonymous";
        this.port = 21;
        this.client = new FTPClient();
    }

    /**
     * Class constructor.
     *
     * @param host Hostname for FTP connection.
     * @param port Port for FTP connection.
     * @param user User logging to FTP.
     * @param password User password for FTP connection.
     * @checkstyle ParameterNumberCheck (2 lines)
     */
    public AbstractFTPConfig(final String host, final int port, final String user,
               final String password) {
        this.host = host;
        this.port = port;
        this.usr  = user;
        this.pass = password;
        this.client = new FTPClient();
    }

    /**
     * Main flow executed on FTP connection.
     *
     * @param commands FTP commands run during FTP connection.
     */
    public void onConnect(final IFTPCommand... commands) {
        try {
            this.client.connect(this.host, this.port);
            this.client.login(this.usr, this.pass);
            for (final IFTPCommand command : commands) {
                command.execute(this.client);
            }
        }catch(IOException ignored){

        }
    }

    public FTPClient getClient() {
        return client;
    }

    public void setClient(FTPClient client) {
        this.client = client;
    }

    public String getHost() {
        return host;
    }
}
