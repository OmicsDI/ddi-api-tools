package uk.ac.ebi.ddi.api.readers.gpmdb.ws.client;

import uk.ac.ebi.ddi.api.readers.ftp.*;
import uk.ac.ebi.ddi.api.readers.model.IFilter;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.api.readers.utils.FileExtensions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

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
public class GPMDBFTPClient {

    private final GPMDBFTPProd gpmdbProd = new GPMDBFTPProd();

    /**
     * Return Complete Model from GPMDB FTP Path
     * @return List of GPMDB Model Path
     * @throws IOException
     */
    public List<String> listAllGPMDBModelPaths() throws IOException {

        List<String> listFiles = new ArrayList<>();
        gpmdbProd.onConnect();
        IFilter[] filters = new IFilter[2];
        filters[0] =  new PrefixFilter(Constants.GPMDB_PREFIX_MODEL);
        filters[1] = new PostFixFilter(FileExtensions.FTP_GPMDB_MODEL_EXTENSION.getExtension());
        final Iterable<String> findings = new FTPFileSearch( Constants.GPMDB_FTP_ROOT_DIRECOTRY, filters, true, new MockCallback<>()).ftpCall(gpmdbProd.getClient());
        findings.forEach(s -> listFiles.add(new StringJoiner(Constants.PATH_DELIMITED).add(gpmdbProd.getHost()).add(s).toString()));
        return listFiles;
    }

    /**
     * List of models in GPMDB
     * @return List of Models
     * @throws IOException
     */
    public List<String> listAllGPMDBModels() throws IOException {
        List<String> listModelFiles = listAllGPMDBModelPaths();
        List<String> models = new ArrayList<>(listModelFiles.size());
        listModelFiles.forEach(file -> models.add(getModel(file)));
        return models;
    }

    public String getModel(String ftpPath){
        return ftpPath.substring(ftpPath.lastIndexOf(Constants.PATH_DELIMITED) +1, ftpPath.lastIndexOf(FileExtensions.FTP_GPMDB_MODEL_EXTENSION.getExtension())-1);
    }
}
