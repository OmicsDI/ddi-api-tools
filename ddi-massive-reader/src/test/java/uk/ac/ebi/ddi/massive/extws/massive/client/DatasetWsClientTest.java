package uk.ac.ebi.ddi.massive.extws.massive.client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.api.readers.massive.ws.client.DatasetWsClient;
import uk.ac.ebi.ddi.api.readers.massive.ws.client.MassiveWsConfigProd;
import uk.ac.ebi.ddi.api.readers.massive.ws.model.MassiveDatasetDetail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class DatasetWsClientTest {

    @Autowired
    MassiveWsConfigProd massiveWsConfig;

    DatasetWsClient datasetWsClient;

    @Before
    public void setUp() throws Exception {
        datasetWsClient = new DatasetWsClient(massiveWsConfig);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetDataset() throws Exception {

        MassiveDatasetDetail dataset = datasetWsClient.getDataset("e6ef1e57368e4282a4b33a15ae94ebbc");

        System.out.println(dataset.toString());

    }
}