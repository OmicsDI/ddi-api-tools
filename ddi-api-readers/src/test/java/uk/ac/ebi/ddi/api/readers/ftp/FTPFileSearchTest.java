package uk.ac.ebi.ddi.api.readers.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import uk.ac.ebi.ddi.api.readers.model.IFilter;

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

@RunWith(MockitoJUnitRunner.class)
public final class FTPFileSearchTest {
    /**
     * Directory outer.
     */
    private static final String DIR_OUT = "dir";
    /**
     * Directory inner.
     */
    private static final String DIR_IN = "dir/dir1";
    /**
     * Prefix.
     */
    private static final String PREFIX = "pre";
    /**
     * Found location.
     */
    private static final String FOUND_LOC = "dir/dir1/pre-test-dir1";
    /**
     * Wrapped FTP client.
     */
    @Mock
    private transient FTPClient client;

    /**
     * Test set up.
     */
    @Before
    public void setUp() throws IOException {
        Mockito.when(this.client.listFiles(Matchers.matches(DIR_OUT)))
                .thenAnswer(
                        inv -> {
                            final FTPFile dir = new FTPFile();
                            dir.setName("dir1");
                            dir.setType(FTPFile.DIRECTORY_TYPE);
                            return new FTPFile[]{dir};
                        }
                );
        Mockito.when(this.client.listFiles(Matchers.matches(DIR_IN)))
                .thenAnswer(
                        inv -> {
                            final FTPFile filep = new FTPFile();
                            filep.setName("pre-test-dir1");
                            filep.setType(FTPFile.FILE_TYPE);
                            final FTPFile file = new FTPFile();
                            file.setName("ppp-test-dir1");
                            file.setType(FTPFile.FILE_TYPE);
                            return new FTPFile[]{filep, file};
                        }
                );
    }

    /**
     * Can find file with prefix recursively.
     */
    @Test
    public void findingFileWithPrefixRecursively() throws IOException{
        IFilter[] filters = new IFilter[1];
        filters[0] = new PrefixFilter(PREFIX);
        final Iterable<String> findings = new FTPFileSearch(
                DIR_OUT,
                filters,
                true,
                new MockCallback<>()
        ).ftpCall(this.client);

        findings.forEach(s -> Assert.assertTrue("found outside", s.equalsIgnoreCase(FOUND_LOC)));
    }

    /**
     * Can find file with prefix non-recursively.
     */
    @Test
    public void findingFileWithPrefixNonRecursively() throws IOException {
        IFilter[] filters = new IFilter[1];
        filters[0] = new PrefixFilter(PREFIX);
        final Iterable<String> findings = new FTPFileSearch(
                DIR_IN,
                filters,
                false,
                new MockCallback<>()
        ).ftpCall(this.client);
       findings.forEach(s -> Assert.assertTrue("not found inside",s.equalsIgnoreCase(FOUND_LOC)));
    }

    /**
     * Cannot find file in deep when searched non-recursively.
     */
    @Test
    public void findingFileInDeepWhenSearchedNoNRecursively() throws IOException{
        IFilter[] filters = new IFilter[1];
        filters[0] = new PrefixFilter(PREFIX);
        final Iterable<String> findings = new FTPFileSearch(
                DIR_OUT,
                filters,
                false,
                new MockCallback<>()
        ).ftpCall(this.client);
        findings.forEach(s -> Assert.assertTrue("found something", s.length() > 0));
    }
}