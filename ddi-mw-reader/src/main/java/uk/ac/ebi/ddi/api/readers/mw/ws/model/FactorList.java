package uk.ac.ebi.ddi.api.readers.mw.ws.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/05/2015
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class FactorList {

    public Map<String, Factor> factors = new HashMap<String, Factor>();

    @JsonAnyGetter
    public Map<String, Factor> any() {
        return factors;
    }

    @JsonAnySetter
    public void set(String name, Factor value) {
        factors.put(name, value);
    }

    public boolean hasUnknowProperties() {
        return !factors.isEmpty();
    }


}
