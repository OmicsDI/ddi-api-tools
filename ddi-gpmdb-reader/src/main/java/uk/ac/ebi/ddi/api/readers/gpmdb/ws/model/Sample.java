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
public class Sample {

    @JsonProperty("gpmdb_BRENDA_cell_culture")
    String cell_culture;

    @JsonProperty("gpmdb_BRENDA_tissue")
    String tissue;

    @JsonProperty("gpmdb_CELL_cell_type")
    String cell_type;

    @JsonProperty("gpmdb_GO_subcellular")
    String go_subcellular;

    @JsonProperty("gpmdb_email")
    String email;

    @JsonProperty("gpmdb_institution")
    String affialiation;

    @JsonProperty("gpmdb_name")
    String submitter;

    @JsonProperty("gpmdb_project")
    String title;

    @JsonProperty("gpmdb_project_comment")
    String description;

    public String getCell_culture() {
        return cell_culture;
    }

    public void setCell_culture(String cell_culture) {
        this.cell_culture = cell_culture;
    }

    public String getTissue() {
        return tissue;
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public String getCell_type() {
        return cell_type;
    }

    public void setCell_type(String cell_type) {
        this.cell_type = cell_type;
    }

    public String getGo_subcellular() {
        return go_subcellular;
    }

    public void setGo_subcellular(String go_subcellular) {
        this.go_subcellular = go_subcellular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAffialiation() {
        return affialiation;
    }

    public void setAffialiation(String affialiation) {
        this.affialiation = affialiation;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
