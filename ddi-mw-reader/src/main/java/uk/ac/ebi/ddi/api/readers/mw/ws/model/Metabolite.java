package uk.ac.ebi.ddi.api.readers.mw.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/05/2015
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class Metabolite {

    @JsonProperty("metabolite_name")
    String name;

    @JsonProperty("metabolite_id")
    String id;

    @JsonProperty("pubchem_id")
    String pubchem;

    String chebi;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPubchem() {
        return pubchem;
    }

    public void setPubchem(String pubchem) {
        this.pubchem = pubchem;
    }

    public String getChebi() {
        return chebi;
    }

    public void setChebi(String chebi) {
        this.chebi = chebi;
    }
}
