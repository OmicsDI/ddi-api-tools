package uk.ac.ebi.ddi.api.readers.geo.datasets.ws.model;


import uk.ac.ebi.ddi.api.readers.model.IAPIDataset;

import java.util.Map;
import java.util.Set;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 03/12/2015
 */

public class Dataset implements IAPIDataset {

    String identifier;

    String name;

    String Description;

    String pubmedID;


    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getDataProtocol() {
        return null;
    }

    @Override
    public String getPublicationDate() {
        return null;
    }

    @Override
    public Map<String, String> getOtherDates() {
        return null;
    }

    @Override
    public String getSampleProcotol() {
        return null;
    }

    @Override
    public Set<String> getOmicsType() {
        return null;
    }

    @Override
    public String getRepository() {
        return null;
    }

    @Override
    public String getFullLink() {
        return null;
    }

    @Override
    public Set<String> getInstruments() {
        return null;
    }

    @Override
    public Set<String> getSpecies() {
        return null;
    }

    @Override
    public Set<String> getCellTypes() {
        return null;
    }

    @Override
    public Set<String> getDiseases() {
        return null;
    }

    @Override
    public Set<String> getTissues() {
        return null;
    }

    @Override
    public Set<String> getSoftwares() {
        return null;
    }

    @Override
    public Set<String> getSubmitter() {
        return null;
    }

    @Override
    public Set<String> getSubmitterEmails() {
        return null;
    }

    @Override
    public Set<String> getSubmitterAffiliations() {
        return null;
    }

    @Override
    public Set<String> getSubmitterKeywords() {
        return null;
    }

    @Override
    public Set<String> getLabHead() {
        return null;
    }

    @Override
    public Set<String> getLabHeadMail() {
        return null;
    }

    @Override
    public Set<String> getLabHeadAffiliation() {
        return null;
    }

    @Override
    public Set<String> getDatasetFiles() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getCrossReferences() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getOtherAdditionals() {
        return null;
    }
}
