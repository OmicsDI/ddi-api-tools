package uk.ac.ebi.ddi.api.readers.mw.ws.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ddi.api.readers.mw.ws.model.FactorList;

import static org.junit.Assert.*;

public class DatasetWsClientTest {

    private DatasetWsClient client;

    @Before
    public void setUp() throws Exception {
        client = new DatasetWsClient(new MWWsConfigProd());
    }

    @Test
    public void getFactorList_SingleFactor() throws Exception {
        FactorList factorList = client.getFactorList("ST000004");
        Assert.assertEquals(1, factorList.factors.size());
    }

    @Test
    public void getFactorList_MultipleFactor() throws Exception {
        FactorList factorList = client.getFactorList("ST000001");
        Assert.assertEquals(24, factorList.factors.size());
    }
}