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
public class Result {

    @JsonProperty("modelling_duplicate_peptide_ids")
    int duplicatePeptides;

    @JsonProperty("modelling_duplicate_protein")
    int duplicateProteins;


    @JsonProperty("modelling_total_spectra_used")
    int totalIdentifiedSpectra;

    @JsonProperty("modelling_total_unique_assigned")
    int uniqueIdentifedSpectra;

    @JsonProperty("process_start_time")
    String submissionDate;

    @JsonProperty("process_version")
    String software;

    public int getDuplicatePeptides() {
        return duplicatePeptides;
    }

    public void setDuplicatePeptides(int duplicatePeptides) {
        this.duplicatePeptides = duplicatePeptides;
    }

    public int getDuplicateProteins() {
        return duplicateProteins;
    }

    public void setDuplicateProteins(int duplicateProteins) {
        this.duplicateProteins = duplicateProteins;
    }

    public int getTotalIdentifiedSpectra() {
        return totalIdentifiedSpectra;
    }

    public void setTotalIdentifiedSpectra(int totalIdentifiedSpectra) {
        this.totalIdentifiedSpectra = totalIdentifiedSpectra;
    }

    public int getUniqueIdentifedSpectra() {
        return uniqueIdentifedSpectra;
    }

    public void setUniqueIdentifedSpectra(int uniqueIdentifedSpectra) {
        this.uniqueIdentifedSpectra = uniqueIdentifedSpectra;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }
}
