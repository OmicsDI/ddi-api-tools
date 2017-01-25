package uk.ac.ebi.ddi.api.readers.mw.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/05/2015
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class ChebiID {

    @JsonProperty("pubchem_cid")
    String pubchemId;

    @JsonProperty("chebi_id")
    String chebi_id;

    public String getPubchemId() {
        return pubchemId;
    }

    public void setPubchemId(String pubchemId) {
        this.pubchemId = pubchemId;
    }

    public String getChebi_id() {
        return chebi_id;
    }

    public void setChebi_id(String chebi_id) {
        this.chebi_id = chebi_id;
    }
}
