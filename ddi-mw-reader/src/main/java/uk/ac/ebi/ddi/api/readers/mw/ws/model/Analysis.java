package uk.ac.ebi.ddi.api.readers.mw.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/05/2015
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class Analysis {

    @JsonProperty("study_id")
    String id;

    @JsonProperty("analysis_id")
    String analysis_id;

    @JsonProperty("analysis_summary")
    String summary;

    @JsonProperty("analysis_type")
    String type;

    @JsonProperty("instrument_name")
    String instrument_name;

    @JsonProperty("instrument_type")
    String instrument_type;

    @JsonProperty("ms_type")
    String ms_type;

    @JsonProperty("ion_mode")
    String ion_mode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnalysis_id() {
        return analysis_id;
    }

    public void setAnalysis_id(String analysis_id) {
        this.analysis_id = analysis_id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInstrument_name() {
        return instrument_name;
    }

    public void setInstrument_name(String instrument_name) {
        this.instrument_name = instrument_name;
    }

    public String getInstrument_type() {
        return instrument_type;
    }

    public void setInstrument_type(String instrument_type) {
        this.instrument_type = instrument_type;
    }

    public String getMs_type() {
        return ms_type;
    }

    public void setMs_type(String ms_type) {
        this.ms_type = ms_type;
    }

    public String getIon_mode() {
        return ion_mode;
    }

    public void setIon_mode(String ion_mode) {
        this.ion_mode = ion_mode;
    }
}
