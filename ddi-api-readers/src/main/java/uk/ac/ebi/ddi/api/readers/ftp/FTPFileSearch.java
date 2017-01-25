package uk.ac.ebi.ddi.api.readers.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.model.ICallback;
import uk.ac.ebi.ddi.api.readers.model.IFilter;
import uk.ac.ebi.ddi.api.readers.utils.Constants;

import java.io.IOException;
import java.util.*;

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
public class FTPFileSearch extends AbstractFTPCommand<Iterable<String>>{

    private static final Logger logger = LoggerFactory.getLogger(FTPFileSearch.class);


    /**
     * Directory to start search from.
     */
    private final transient String dir;

    /**
     * IFilter for files found.
     */
    private final transient IFilter<FTPFile>[] fltr;

    /**
     * Is search recursive.
     */
    private final transient boolean recurs;

    /**
     * Class constructor.
     *
     * @param directory Directory to start search from.
     * @param filter Limiting files found to those filtered.
     * @param recursive Is search recursive.
     * @param callback ICallback on files found.
     * @checkstyle ParameterNumberCheck (6 lines)
     */
    public FTPFileSearch(final String directory, final IFilter<FTPFile>[] filter,
                         final boolean recursive, final ICallback<Iterable<String>> callback) {
        super(callback);
        this.dir = directory;
        this.fltr = filter;
        this.recurs = recursive;
    }

    @Override
    public Iterable<String> ftpCall(final FTPClient client) throws IOException {
            return this.search(new ArrayList<>(0), this.dir, client);
    }

    /**
     * Recursive search in directory calling FTP client search.
     *
     * @param result Partial results for recursion.
     * @param directory Directory to be searched in.
     * @param client Apache FTP client.
     * @return File names found.
     */
    private Collection<String> search(final Collection<String> result,
                                      final String directory,
                                      final FTPClient client) throws IOException {
        String delimiter = Constants.PATH_DELIMITED;
        final FTPFile[] ftpFiles = client.listFiles(directory);
        for (final FTPFile ftpFile : ftpFiles) {
                if (ftpFile.isFile()) {
                    boolean valid = true;
                    for(IFilter filter: this.fltr){
                        if(!filter.valid(ftpFile))
                            valid = false;
                    }
                    if(valid)
                        result.add(new StringJoiner(delimiter).add(directory).add(ftpFile.getName()).toString());
                }else if (ftpFile.isDirectory() && this.recurs) {
                        search(result, new StringJoiner(delimiter).add(directory).add(ftpFile.getName()).toString(), client);

                }
        }
        return result;
    }

}
