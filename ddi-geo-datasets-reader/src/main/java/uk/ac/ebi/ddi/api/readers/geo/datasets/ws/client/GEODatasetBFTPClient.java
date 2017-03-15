package uk.ac.ebi.ddi.api.readers.geo.datasets.ws.client;

import uk.ac.ebi.ddi.api.readers.ftp.FTPFileSearch;
import uk.ac.ebi.ddi.api.readers.ftp.MockCallback;
import uk.ac.ebi.ddi.api.readers.ftp.PostFixFilter;
import uk.ac.ebi.ddi.api.readers.geo.datasets.ws.model.Dataset;
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
public class GEODatasetBFTPClient {

    private final GEOFTPProd geoftpProd;

    public GEODatasetBFTPClient(GEOFTPProd configProd) {
        this.geoftpProd = configProd;
    }

    /**
     * Return Complete Model from GPMDB FTP Path
     * @return List of GPMDB Model Path
     * @throws IOException
     */
    private List<String> listAllGEODatasestPaths() throws IOException {

        List<String> listFiles = new ArrayList<>();
        geoftpProd.onConnect();
        geoftpProd.getClient().setBufferSize(1024000);
        geoftpProd.getClient().enterLocalPassiveMode();
        IFilter[] filters = new IFilter[1];
        filters[0] =  new PostFixFilter(Constants.GEO_POSFIX_DATABASE);
        final Iterable<String> findings = new FTPFileSearch( Constants.GEO_DATASETS_FTP_ROOT_DIRECOTRY, filters, true, new MockCallback<>()).ftpCall(geoftpProd.getClient());
        findings.forEach(s -> listFiles.add(new StringJoiner(Constants.PATH_DELIMITED).add(geoftpProd.getHost()).add(s).toString()));
        return listFiles;
    }

    /**
     * List of GEO Models in GEO Datasets
     * @return List of Datasets
     * @throws IOException
     */
    public List<Dataset> listAllGEODatasets() throws IOException {
        List<String> listModelFiles = listAllGEODatasestPaths();
        List<Dataset> datasets = new ArrayList<>(listModelFiles.size());
        listModelFiles.forEach(file -> datasets.add(parseGEODataset(file)));
        return datasets;
    }

    private Dataset parseGEODataset(String file) {
        return null;
    }


    public String getModel(String ftpPath){
        return ftpPath.substring(ftpPath.lastIndexOf(Constants.PATH_DELIMITED) +1, ftpPath.lastIndexOf(FileExtensions.FTP_GPMDB_MODEL_EXTENSION.getExtension())-1);
    }
}
