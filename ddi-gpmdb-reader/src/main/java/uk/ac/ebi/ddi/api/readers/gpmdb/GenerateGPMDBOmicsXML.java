package uk.ac.ebi.ddi.api.readers.gpmdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.ddi.api.readers.gpmdb.ws.client.GPMDBClient;
import uk.ac.ebi.ddi.api.readers.gpmdb.ws.client.GPMDBFTPClient;
import uk.ac.ebi.ddi.api.readers.gpmdb.ws.client.GPMDBWsConfigProd;
import uk.ac.ebi.ddi.api.readers.gpmdb.ws.filters.SpeciesCellTypeFilter;
import uk.ac.ebi.ddi.api.readers.gpmdb.ws.model.Model;
import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.api.readers.utils.Transformers;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entries;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;
import uk.ac.ebi.ddi.xml.validator.utils.BiologicalDatabases;
import uk.ac.ebi.ddi.xml.validator.utils.Field;
import uk.ac.ebi.pride.tools.protein_details_fetcher.ProteinDetailFetcher;

import java.io.FileWriter;
import java.util.*;


/**
 * This project takes class Retrieve information from GPMDB, it allows to retrieve
 * the proteins ids for an specific model, etc.
 *
 *
 * @author Yasset Perez-Riverol
 */

public class GenerateGPMDBOmicsXML {

    private static final Logger logger = LoggerFactory.getLogger(GenerateGPMDBOmicsXML.class);

    /**
     * This program generate the massive files in two different type of files MASSIVE and GNPS Files. The MASSIVE
     * files correspond to proteomics datasets and the GNPS correspond to metabolomics datasets.
     *
     * @param args
     */
    public static void main(String[] args) {

        String outputFolder = null;
        String releaseDate  = null;

        if (args != null && args.length > 1 && args[0] != null){
            outputFolder = args[0];
            releaseDate  = args[1];
        } else
            System.exit(-1);


        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/app-context.xml");
        GPMDBWsConfigProd mwWsConfigProd = (GPMDBWsConfigProd) ctx.getBean("gpmdbWsConfig");

        try {
            GenerateGPMDBOmicsXML.generateMWXMLFiles(mwWsConfigProd, outputFolder, releaseDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateMWXMLFiles(AbstractWsConfig configProd, String outputFolder, String releaseDate) throws Exception{

        GPMDBFTPClient modelFTPClient = new GPMDBFTPClient();
        GPMDBClient modelWSClient     = new GPMDBClient(configProd);
        List<String> datasetList = modelFTPClient.listAllGPMDBModelPaths();
        ProteinDetailFetcher detailFetcher = new ProteinDetailFetcher();


        if (datasetList != null && datasetList.size() > 0) {
            Entries gpmdbEntries = new Entries();
            datasetList.parallelStream().forEach( datasetFTP -> {
                String modelID    = modelFTPClient.getModel(datasetFTP);
                Model model       = modelWSClient.getModelInformation(modelID);
                if(model != null && model.getIdentifier() != null && new SpeciesCellTypeFilter().valid(model)){
                    String[] proteins = modelWSClient.getAllProteins(modelID);
                    if(proteins != null && proteins.length > 0){
                        Set<String> uniprotProteins  = new HashSet<>();
                        Set<String> ensemblProteins  = new HashSet<>();
                        Set<String> otherIdentifiers = new HashSet<>();
                        model.setModel(datasetFTP);
                        Arrays.asList(proteins).forEach(protein -> {
                            ProteinDetailFetcher.AccessionType accessionType = detailFetcher.getAccessionType(protein);
                            if(accessionType == ProteinDetailFetcher.AccessionType.UNIPROT_ID || accessionType == ProteinDetailFetcher.AccessionType.UNIPROT_ACC){
                                uniprotProteins.add(protein);
                            }else if(accessionType == ProteinDetailFetcher.AccessionType.ENSEMBL || accessionType == ProteinDetailFetcher.AccessionType.ENSEMBL_TRANSCRIPT){
                                ensemblProteins.add(protein);
                            }else{
                                otherIdentifiers.add(protein);
                            }
                        });
                        if(uniprotProteins.size() > 0){
                            Map<String, Set<String>> proteinMap  = new HashMap<>();
                            proteinMap.put(BiologicalDatabases.UNIPROT.getName(), uniprotProteins);
                            model.addProteins(proteinMap);
                        }
                        if(ensemblProteins.size() > 0){
                            Map<String, Set<String>> proteinMap  = new HashMap<>();
                            proteinMap.put(BiologicalDatabases.ENSEMBL.getName(), ensemblProteins);
                            model.addProteins(proteinMap);
                        }
                        if(otherIdentifiers.size() > 0){
                            Map<String, Set<String>> proteinMap  = new HashMap<>();
                            proteinMap.put(BiologicalDatabases.GPMDB.getName(), otherIdentifiers);
                            model.addProteins(proteinMap);
                        }

                        if(proteins.length > 0){
                            Set<String> proteinSet = new HashSet<>(Arrays.asList(proteins));
                            Map<String, Set<String>> proteinNames = new HashMap<>();
                            proteinNames.put(Field.PROTEIN_NAME.getName(), proteinSet);
                            model.addOtherAdditionals(proteinNames);
                        }

                        Entry entry = Transformers.transformAPIDatasetToEntry(model);
                        gpmdbEntries.addEntry(entry);
                    }
                }
            });

            FileWriter gpmdbFile = new FileWriter(outputFolder + "/gpmdb_data.xml");
            OmicsDataMarshaller mm = new OmicsDataMarshaller();

            Database database = new Database();
            database.setDescription(Constants.GPMDB_DESCRIPTION);
            database.setName(Constants.GPMDB);
            database.setRelease(releaseDate);
            database.setEntries(gpmdbEntries);
            database.setEntryCount(gpmdbEntries.getEntry().size());
            mm.marshall(database, gpmdbFile);
        }
    }




}
