package uk.ac.ebi.ddi.api.readers.mw.ws.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 24/04/2016
 */
public class DiseaseList {

    public Map<String, Disease> diseases = new HashMap<String, Disease>();

    @JsonAnyGetter
    public Map<String, Disease> any() {
        return diseases;
    }

    @JsonAnySetter
    public void set(String name, Disease value) {
        diseases.put(name, value);
    }

    public boolean hasUnknowProperties() {
        return !diseases.isEmpty();
    }

    public Set<String> getDiseasesByDataset(String id) {
        Set<String> tissuesResult = new HashSet<String>();
        if(diseases != null && !diseases.isEmpty())
            for(Disease disease: diseases.values())
                if(disease.getStudyId().equalsIgnoreCase(id))
                    tissuesResult.add(disease.getDisease());
        return tissuesResult;
    }
}
