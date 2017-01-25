package uk.ac.ebi.ddi.api.readers.gpmdb.ws.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

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
public class GPMDBFTPClientTest {

    GPMDBFTPClient gpmdbClient;

    @Before
    public void setUp() throws Exception {
        gpmdbClient = new GPMDBFTPClient();
    }

    @Test
    public void listAllGPMDBModels() throws Exception {

        List<String> listModels = gpmdbClient.listAllGPMDBModels();
        Assert.assertTrue("More than 0 models", listModels.size() > 0);
    }

}