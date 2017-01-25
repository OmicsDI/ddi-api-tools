package uk.ac.ebi.ddi.api.readers.mw.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 24/04/2016
 */
@JsonIgnoreProperties(ignoreUnknown =  true)
public class Tissue {

    @JsonProperty("Study ID")
    String studyId;

    @JsonProperty("Sample source")
    String tissue;

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public String getTissue() {
        return tissue;
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    @Override
    public String toString() {
        return "Tissue{" +
                "studyId='" + studyId + '\'' +
                ", tissue='" + tissue + '\'' +
                '}';
    }
}
