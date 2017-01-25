package uk.ac.ebi.ddi.massive.extws.massive.client;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.api.readers.massive.ws.client.ISODetasetsWsClient;
import uk.ac.ebi.ddi.api.readers.massive.ws.client.MassiveWsConfigProd;
import uk.ac.ebi.ddi.api.readers.massive.ws.model.MassiveDatasetList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class ISODetasetsWsClientTest {

    @Autowired
    MassiveWsConfigProd massiveWsConfig;

    ISODetasetsWsClient datasetWsClient;

    @Before
    public void setUp() throws Exception {
        datasetWsClient = new ISODetasetsWsClient(massiveWsConfig);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetAllDatasets() throws Exception {

        MassiveDatasetList datasetList = datasetWsClient.getAllDatasets();

        Assert.assertTrue(datasetList.getDatasets().length > 0);

    }
}