package uk.ac.ebi.ddi.api.readers.mw;

import org.junit.After;
import org.junit.Before;
import uk.ac.ebi.ddi.api.readers.mw.ws.client.DatasetWsClient;
import uk.ac.ebi.ddi.api.readers.mw.ws.client.MWWsConfigProd;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 24/04/2016
 */


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"/test-context.xml"})

public class GenerateMWOmicsXMLTest {

    MWWsConfigProd mwWsConfig = new MWWsConfigProd();

    DatasetWsClient datasetWsClient;

    @Before
    public void setUp() throws Exception {
        datasetWsClient = new DatasetWsClient(mwWsConfig);
    }

    @After
    public void tearDown() throws Exception {

    }

    // Todo: This should be Integration Test
//    @Test
//    public void countDatasets() throws Exception{
//        DatasetList list = datasetWsClient.getAllDatasets();
//        Assert.assertTrue(list.datasets.size() > 0);
//    }
//
//    @Test
//    public void countSpecies() throws Exception{
//        SpecieList list = datasetWsClient.getSpecies();
//        Assert.assertTrue(list.species.size() > 0);
//        System.out.println(list.species.values());
//    }
//
//    @Test
//    public void testAnalysisData() throws  Exception{
//        AnalysisList analysis = datasetWsClient.getAnalysisInformantion("ST000001");
//        Assert.assertTrue(analysis.analysisMap.get("1").getInstrument_type().equalsIgnoreCase("GC-TOF"));
//    }
//
//    @Test
//    public void countTissues() throws Exception{
//        TissueList list = datasetWsClient.getTissues();
//        Assert.assertTrue(list.tissues.size() > 0);
//        System.out.println(list.tissues.values());
//    }
//
//    @Test
//    public void countDiseases() throws Exception{
//        DiseaseList list = datasetWsClient.getDiseases();
//        Assert.assertTrue(list.diseases.size() > 0);
//        System.out.println(list.diseases.values());
//    }
//

}