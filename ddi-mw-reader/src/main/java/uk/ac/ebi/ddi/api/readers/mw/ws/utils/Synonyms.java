package uk.ac.ebi.ddi.api.readers.mw.ws.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 20/05/2015
 */
public enum Synonyms {

    MASS_SPECTROMETRY("Mass Spectrometry", "MS", "MS/MS");

    private String term;

    private List<String> synonyms = new ArrayList<>();

    Synonyms(String term, String... synonym) {

        this.term = term;
        for (String value : synonym) {
            if (value != null) {
                synonyms.add(value);
            }
        }
    }

    public String getTerm() {
        return term;
    }

    public List<String> getSynomyms() {
        return synonyms;
    }

    public static String getTermBySynonym(String synonym) {
        if (synonym != null) {
            for (Synonyms value : Synonyms.values()) {
                for (String valueSyn : value.getSynomyms()) {
                    if (synonym.compareToIgnoreCase(valueSyn) == 0) {
                        return value.getTerm();
                    }
                }
            }
        }
        return null;
    }
}
