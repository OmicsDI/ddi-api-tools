package uk.ac.ebi.ddi.api.readers.mw.ws.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.api.readers.mw.ws.model.*;
import uk.ac.ebi.ddi.api.readers.ws.AbstractClient;
import uk.ac.ebi.ddi.api.readers.ws.AbstractWsConfig;

import java.net.URI;
import java.util.Map;


/**
 * @author Yasset Perez-Riverol ypriverol
 */
public class DatasetWsClient extends AbstractClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetWsClient.class);

    /**
     * Default constructor for Ws clients
     *
     * @param config
     */
    public DatasetWsClient(AbstractWsConfig config) {
        super(config);

    }

    /**
     * Returns the Datasets from MtabolomeWorbench
     * @return A list of entries and the facets included
     */
    public DatasetList getAllDatasets() {

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme(config.getProtocol())
                .host(config.getHostName())
                .path("/rest/study/study_id/ST/summary");

        URI uri = builder.build().encode().toUri();
        return getRetryTemplate().execute(ctx -> restTemplate.getForObject(uri, DatasetList.class));
    }

    /**
     * Return the Analysis information for one particular dataset.
     * @param id
     * @return
     */
    public AnalysisList getAnalysisInformantion(String id) throws Exception {

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme(config.getProtocol())
                .host(config.getHostName())
                .path("/rest/study/study_id")
                .path("/" + id)
                .path("/analysis");
        URI uri = builder.build().encode().toUri();
        JsonNode node = getRetryTemplate().execute(ctx -> restTemplate.getForObject(uri, JsonNode.class));
        if (node.has("1")) {
            return objectMapper.treeToValue(node, AnalysisList.class);
        } else if (node.has("study_id")) {
            Analysis analysisSingle = objectMapper.treeToValue(node, Analysis.class);
            AnalysisList analysisList = new AnalysisList();
            analysisList.set("1", analysisSingle);
            return analysisList;
        }
        return null;
    }

    public MetaboliteList getMataboliteList(String id) throws Exception {

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme(config.getProtocol())
                .host(config.getHostName())
                .path("/rest/study/study_id")
                .path("/" + id)
                .path("/metabolites");
        URI uri = builder.build().encode().toUri();
        JsonNode node = getRetryTemplate().execute(ctx -> restTemplate.getForObject(uri, JsonNode.class));
        if (node.has("1")) {
            return objectMapper.treeToValue(node, MetaboliteList.class);
        } else if (node.has("study_id")) {
            MetaboliteList metaboliteList = new MetaboliteList();
            Metabolite metabolite = objectMapper.treeToValue(node, Metabolite.class);
            metaboliteList.set("1", metabolite);
            return metaboliteList;
        }
        return null;
    }

    private ChebiID getChebiId(String pubchemId) {

        String url = String.format("%s://%s/rest/compound/pubchem_cid/%s/chebi_id/",
                config.getProtocol(), config.getHostName(), pubchemId);
        ChebiID id = null;
        try {
            id =this.restTemplate.getForObject(url, ChebiID.class);
        } catch (Exception e) {
            LOGGER.error("Exception occurred, id: {}, ", pubchemId, e);
        }

        return id;
    }

    /**
     * Return the list of metaboligths identified in the current experiment
     * @param metabolites
     * @return
     */
    public MetaboliteList updateChebiId(MetaboliteList metabolites) {
        if(metabolites != null && metabolites.metabolites != null && metabolites.metabolites.size() > 0){
            System.out.println(metabolites.metabolites.size());
            for(Map.Entry entry: metabolites.metabolites.entrySet()){
                String key = (String) entry.getKey();
                Metabolite met = (Metabolite) entry.getValue();
                if(met != null && met.getPubchem() != null){
                    ChebiID id = getChebiId(met.getPubchem());
                    if(id != null)
                        met.setChebi(id.getChebi_id());
                }
                metabolites.metabolites.put(key,met);
            }
        }
        return metabolites;
    }

    /**
     * Return Experiment Factors
     * @param id
     * @return List of Experiment Factors
     */
    public FactorList getFactorList(String id) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme(config.getProtocol())
                .host(config.getHostName())
                .path("/rest/study/study_id")
                .path("/" + id)
                .path("/factors");
        URI uri = builder.build().encode().toUri();
        JsonNode node = getRetryTemplate().execute(ctx -> restTemplate.getForObject(uri, JsonNode.class));
        if (node.has("study_id")) {
            Factor factor = objectMapper.treeToValue(node, Factor.class);
            FactorList factorList = new FactorList();
            factorList.set("1", factor);
            return factorList;
        } else if (node.has("1")) {
            return objectMapper.treeToValue(node, FactorList.class);
        }
        return null;
    }

    /**
     * This function retrieve all the specie information for each dataset.
     * @return Specie List
     */
    public SpecieList getSpecies(){

        String url = String.format("%s://%s/rest/study/study_id/ST/species",
                config.getProtocol(), config.getHostName());
        return getRetryTemplate().execute(ctx -> restTemplate.getForObject(url, SpecieList.class));
    }

    public TissueList getTissues(){

        String url = String.format("%s://%s/rest/study/study_id/ST/source",
                config.getProtocol(), config.getHostName());
        return getRetryTemplate().execute(ctx -> restTemplate.getForObject(url, TissueList.class));

    }

    public DiseaseList getDiseases(){

        String url = String.format("%s://%s/rest/study/study_id/ST/disease",
                config.getProtocol(), config.getHostName());
        return getRetryTemplate().execute(ctx -> this.restTemplate.getForObject(url, DiseaseList.class));
    }

}
