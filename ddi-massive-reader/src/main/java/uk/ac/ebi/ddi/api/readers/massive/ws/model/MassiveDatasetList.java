package uk.ac.ebi.ddi.api.readers.massive.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 18/05/2015
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class MassiveDatasetList {

    @JsonProperty("datasets")
    public MassiveDatasetSummary[] datasets;

    public MassiveDatasetSummary[] getDatasets() {
        return datasets;
    }
}
