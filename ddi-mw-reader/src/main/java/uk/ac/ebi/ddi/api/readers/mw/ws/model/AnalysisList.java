package uk.ac.ebi.ddi.api.readers.mw.ws.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 20/05/2015
 */

public class AnalysisList {

    public Map<String, Analysis> analysisMap = new HashMap<String, Analysis>();

    @JsonAnyGetter
    public Map<String, Analysis> any() {
        return analysisMap;
    }

    @JsonAnySetter
    public void set(String name, Analysis value) {
        analysisMap.put(name, value);
    }

    public boolean hasUnknowProperties() {
        return !analysisMap.isEmpty();
    }
}
