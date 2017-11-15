package uk.ac.ebi.ddi.api.readers.bioprojects.ws.model;

import uk.ac.ebi.ddi.api.readers.model.IAPIDataset;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.xml.validator.utils.BiologicalDatabases;
import uk.ac.ebi.ddi.xml.validator.utils.Field;
import uk.ac.ebi.ddi.xml.validator.utils.OmicsType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrey Zorin (azorin@ebi.ac.uk)
 * @date 14/11/2017
 */

public class BioprojectDataset implements IAPIDataset{

    private String identifier = null;

    private String repository = null;

    private String name;

    private String description;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }


    public String getDescription() {
        return description;
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
        return new HashMap<String, String>();
    }

    @Override
    public String getSampleProcotol() {
        return null;
    }

    @Override
    public Set<String> getOmicsType() {
        return new HashSet<String>();
    }

    @Override
    public String getRepository() {
        return repository;
    }

    @Override
    public String getFullLink() {
        return null;
    }

    @Override
    public Set<String> getInstruments() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getSpecies() {
        return new HashSet<String>();

    }

    @Override
    public Set<String> getCellTypes() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getDiseases() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getTissues() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getSoftwares() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getSubmitter() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getSubmitterEmails() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getSubmitterAffiliations() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getSubmitterKeywords() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getLabHead() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getLabHeadMail() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getLabHeadAffiliation() {
        return new HashSet<String>();
    }

    @Override
    public Set<String> getDatasetFiles() {
        return new HashSet<String>();
    }

    @Override
    public Map<String, Set<String>> getCrossReferences() {
        return new HashMap<String, Set<String>>();
    }

    @Override
    public Map<String, Set<String>> getOtherAdditionals() {
        return new HashMap<String, Set<String>>();
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
