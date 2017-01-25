package uk.ac.ebi.ddi.api.readers.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import uk.ac.ebi.ddi.api.readers.model.ICallback;

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
public final class FTPDirList extends AbstractFTPCommand<FTPFile[]> {


    private final transient String directory;

    public FTPDirList(String directory, final ICallback<FTPFile[]> callback) {
        super(callback);
        this.directory = directory;
    }

    @Override
    protected FTPFile[] ftpCall(final FTPClient client) throws IOException {
        return client.listFiles(this.directory);
    }
}
