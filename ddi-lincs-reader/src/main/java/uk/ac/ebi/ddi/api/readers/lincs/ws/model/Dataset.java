package uk.ac.ebi.ddi.api.readers.lincs.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import uk.ac.ebi.ddi.api.readers.model.IAPIDataset;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.xml.validator.utils.Field;
import uk.ac.ebi.ddi.xml.validator.utils.OmicsType;

import java.util.*;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 03/12/2015
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Dataset implements IAPIDataset{

    @JsonProperty("datasetid")
    private String id;

    @JsonProperty("datasetname")
    private String name;

    @JsonProperty("ldplink")
    private String datasetLink;

    @JsonProperty("projectname") String projectName;

    @JsonProperty("principalinvestigator")
    private String principalInvestigator;

    @JsonProperty("centerfullname")
    private String affiliation;

    @JsonProperty("datereleased")
    private String publicationDate;

    @JsonProperty("datemodified")
    private String modifiedDate;

    @JsonProperty("screeninglabinvestigator")
    private String screeninglabinvestigator;

    @JsonProperty("cellline")
    String[] cellLine;

    @JsonProperty("tool")
    private String[] software;

    @JsonProperty("biologicalbucket")
    private String omicsType;

    @JsonProperty("assayname")
    String[] assayName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("assayoverview")
    private String sampleDataProtocol;

    @JsonProperty("funding")
    private String funding;

    @JsonProperty("genes")
    private String[] genes;

    @JsonProperty("smallmolecules")
    private String[] smallMolecules;

    @JsonProperty("physicaldetection")
    private String physicalDetection;

    @JsonProperty("publication")
    private String pubmedID;

    @JsonProperty("protein")
    private String[] proteins;

    @JsonProperty("assaydesignmethod")
    private String[] assayMethod;


    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getDataProtocol() {
        return Constants.EMPTY_STRING;
    }

    @Override
    public String getPublicationDate() {
        return publicationDate;
    }

    @Override
    public Map<String, String> getOtherDates() {
        Map<String, String> dates = new HashMap<>();
        if(modifiedDate != null)
            dates.put(Field.PUBLICATION_UPDATED.getName(), modifiedDate);
        return dates;
    }

    @Override
    public String getSampleProcotol() {
        return sampleDataProtocol;
    }

    @Override
    public Set<String> getOmicsType() {
        Set<String> omicsTypes = new HashSet<>();
        omicsTypes.add(OmicsType.getOmicsType(omicsType).getName());
        return omicsTypes;
    }

    @Override
    public String getRepository() {
        return Constants.LINCS;
    }

    @Override
    public String getFullLink() {
        return datasetLink;
    }

    @Override
    public Set<String> getInstruments() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getSpecies() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getCellTypes() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getDiseases() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getTissues() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getSoftwares() {
        if(software != null)
            return new HashSet<>(Arrays.asList(software));
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getSubmitter() {
        Set<String> submitters = new HashSet<>();
        if(screeninglabinvestigator != null)
            submitters.add(screeninglabinvestigator);
        return submitters;
    }

    @Override
    public Set<String> getSubmitterEmails() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getSubmitterAffiliations() {
        Set<String> affiliations = new HashSet<>();
        if(this.affiliation != null)
            affiliations.add(this.affiliation);
        return affiliations;
    }

    @Override
    public Set<String> getSubmitterKeywords() {
        return null;
    }

    @Override
    public Set<String> getLabHead() {
        Set<String> labHead  = new HashSet<>();
        if(principalInvestigator != null)
            labHead.add(principalInvestigator);
        return labHead;
    }

    @Override
    public Set<String> getLabHeadMail() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getLabHeadAffiliation() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Set<String> getDatasetFiles() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Map<String, Set<String>> getCrossReferences() {
        Map<String, Set<String>> crossReferences = new HashMap<>();
        if(pubmedID != null){
            Set<String> publications = new HashSet<>();
            publications.add(pubmedID);
            crossReferences.put(Field.PUBMED.getName(), publications);
        }
        return crossReferences;
    }

    @Override
    public Map<String, Set<String>> getOtherAdditionals() {
        Map<String, Set<String>> otherAdditionals = new HashMap<>();
        if(smallMolecules != null && smallMolecules.length > 0){
            Set<String> smallMoleculesSet = new HashSet<>();
            smallMoleculesSet.addAll(Arrays.asList(smallMolecules));
            otherAdditionals.put(Field.METABOLITE_NAME.getName(), smallMoleculesSet);
        }
        if(proteins != null && proteins.length > 0){
            Set<String> proteinSet = new HashSet<>();
            proteinSet.addAll(Arrays.asList(proteins));
            otherAdditionals.put(Field.PROTEIN_NAME.getName(), proteinSet);
        }

        if(genes != null && genes.length > 0){
            Set<String> geneSet = new HashSet<>();
            geneSet.addAll(Arrays.asList(genes));
            otherAdditionals.put(Field.GENE_NAME.getName(), geneSet);
        }
        Set<String> tecnologyTypes = new HashSet<>();
        if(physicalDetection != null && physicalDetection.length() >0){
            tecnologyTypes.add(physicalDetection);
        }
        if(assayMethod != null)
            tecnologyTypes.addAll(Arrays.asList(assayMethod));

        if(tecnologyTypes.size() >0)
            otherAdditionals.put(Field.TECHNOLOGY_TYPE.getName(), tecnologyTypes);

        if(funding != null){
            Set<String> fundings = new HashSet<>();
            fundings.add(funding);
            otherAdditionals.put(Field.FUNDING.getName(), fundings);
        }



        return otherAdditionals;

    }
}
