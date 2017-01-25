package uk.ac.ebi.ddi.api.readers.mw.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/05/2015
 */

@JsonIgnoreProperties(ignoreUnknown =  true)
public class Factor {

    @JsonProperty("subject_type")
    String type;

    @JsonProperty("factors")
    String factors;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFactors() {
        return factors;
    }

    public void setFactors(String factors) {
        this.factors = factors;
    }
}
