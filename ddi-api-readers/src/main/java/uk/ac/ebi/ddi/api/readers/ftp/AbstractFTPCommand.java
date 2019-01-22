package uk.ac.ebi.ddi.api.readers.ftp;

import org.apache.commons.net.ftp.FTPClient;
import uk.ac.ebi.ddi.api.readers.model.ICallback;
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
public abstract class AbstractFTPCommand<T> implements IFTPCommand {
    /**
     * ICallback after FTP execution.
     */
    private final transient ICallback<T> call;

    /**
     * Class constructor.
     *
     * @param callback How handle result of type T of FTP call execution.
     */
    public AbstractFTPCommand(final ICallback<T> callback) {
        this.call = callback;
    }

    /**
     * Wrap of the FTP call.
     *
     * @param client Apache FTP client.
     */
    public final void execute(final FTPClient client) throws IOException {
        this.call.onReturn(this.ftpCall(client));
    }

    /**
     * FTP call being wrapped.
     *
     * @param client Apache FTP client.
     * @return Parametrized result of specific FTP action call.
     */
    protected abstract T ftpCall(final FTPClient client) throws IOException;

}
