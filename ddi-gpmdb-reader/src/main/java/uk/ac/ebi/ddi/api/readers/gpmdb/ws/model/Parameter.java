package uk.ac.ebi.ddi.api.readers.gpmdb.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

@JsonIgnoreProperties(ignoreUnknown = true)
public class Parameter {

    @JsonProperty("output_title")
    String identifier;

    @JsonProperty("protein_taxon")
    String taxonomy;

    @JsonProperty("refine_potential_modification_mass")
    String rawModification;

    @JsonProperty("protein_cleavage_site")
    String enzyme;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getRawModification() {
        return rawModification;
    }

    public void setRawModification(String rawModification) {
        this.rawModification = rawModification;
    }

    public String getEnzyme() {
        return enzyme;
    }

    public void setEnzyme(String enzyme) {
        this.enzyme = enzyme;
    }
}
