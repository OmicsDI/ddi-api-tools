package uk.ac.ebi.ddi.api.readers.mw.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.mw.ws.utils.Synonyms;
import uk.ac.ebi.ddi.api.readers.model.IAPIDataset;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.xml.validator.utils.Field;
import uk.ac.ebi.ddi.xml.validator.utils.OmicsType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class modeled a Dataset Summary in Metabolomics Workbench. The dataset sumary contains only a
 * few information like the study id, title, type of study
 *
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 18/05/2015
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class MWDataSetDetails implements IAPIDataset {

    private static final Logger LOGGER = LoggerFactory.getLogger(MWDataSetDetails.class);

    @JsonProperty("study_id")
    private String id;

    @JsonProperty("study_title")
    private String title;

    @JsonProperty("study_type")
    private String type;

    @JsonProperty("institute")
    private String institute;

    @JsonProperty("department")
    private String department;

    @JsonProperty("last_name")
    private String last_name;

    @JsonProperty("first_name")
    private String firstname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("submit_date")
    private String submit_date;

    @JsonProperty("study_summary")
    private String description;

    @JsonProperty("subject_species")
    private String subject_species;

    private AnalysisList analysis;
    private Set<String> diseases;
    private FactorList factors;
    private Set<String> tissues;
    private Set<Specie> species;
    private MetaboliteList metabolites;

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public String getName() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getDataProtocol() {
        return Constants.EMPTY_STRING;
    }

    @Override
    public String getPublicationDate() {
        DateFormat formatter = new SimpleDateFormat("yy-MM-dd");
        Date date;
        try {
            date = formatter.parse(submit_date);
            return date.toString();
        } catch (ParseException e) {
            LOGGER.debug(e.getLocalizedMessage());
        }
        return Constants.EMPTY_STRING;
    }

    @Override
    public Map<String, String> getOtherDates() {
        return Collections.emptyMap();
    }

    @Override
    public String getSampleProcotol() {
        String experimentTypes = "";
        if (analysis != null && analysis.analysisMap != null && analysis.analysisMap.size() > 0) {
            for (Analysis analysis : analysis.analysisMap.values()) {
                if (analysis != null && analysis.getSummary() != null) {
                    experimentTypes += analysis.getSummary().trim() + ". ";
                }
            }
        }
        return experimentTypes.trim();
    }

    @Override
    public Set<String> getOmicsType() {
        Set<String> omicsType = new HashSet<>();
        omicsType.add(OmicsType.METABOLOMICS.getName());
        return omicsType;
    }

    @Override
    public String getRepository() {
        return Constants.METABOLOMICS_WORKBENCH;
    }

    @Override
    public String getFullLink() {
        return Constants.METABOLOMEWORKBENCH_LINK + getIdentifier();
    }

    @Override
    public Set<String> getInstruments() {
        Set<String> instruments = new HashSet<>();
        if (analysis != null && analysis.analysisMap != null && analysis.analysisMap.size() > 0) {
            analysis.analysisMap.values().forEach(analysisValue -> {
                if (analysisValue != null
                        && (analysisValue.getInstrument_name() != null || analysisValue.getInstrument_type() != null)) {
                    instruments.add(analysisValue.getInstrument_type().trim());
                }
            });
        }
        return instruments;
    }

    @Override
    public Set<String> getSpecies() {
        Set<String> speciesResult = new HashSet<>();
        if (species != null && !species.isEmpty()) {
            species.forEach(specie -> {
                if (specie.getLantinName() != null) {
                    speciesResult.add(specie.getLantinName());
                }
                if (specie.getName() != null) {
                    speciesResult.add(specie.getName());
                }
            });
        }
        if (subject_species != null && subject_species.length() > 0) {
            speciesResult.add(subject_species);
        }

        return speciesResult;
    }

    @Override
    public Set<String> getCellTypes() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getDiseases() {
        return diseases;
    }

    @Override
    public Set<String> getTissues() {
        return tissues;
    }

    @Override
    public Set<String> getSoftwares() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getSubmitter() {
        Set<String> submitters = new HashSet<>();
        String name = firstname;
        if (last_name != null && last_name.length() > 0) {
            name = name + " " + last_name;
        }
        submitters.add(name);
        return submitters;
    }

    @Override
    public Set<String> getSubmitterEmails() {
        Set<String> emails = new HashSet<>();
        emails.add(email);
        return emails;
    }

    @Override
    public Set<String> getSubmitterAffiliations() {
        Set<String> affiliations = new HashSet<>();
        String affiliation = "";
        if (department != null && department.length() > 0) {
            affiliation += department;
        }
        if (institute != null && institute.length() > 0) {
            if (affiliation.length() > 0) {
                affiliation += ", " + institute;
            } else {
                affiliation += institute;
            }
        }
        affiliations.add(affiliation);
        return affiliations;
    }

    @Override
    public Set<String> getSubmitterKeywords() {
        Set<String> keywords = new HashSet<>();
        keywords.add(type);
        return keywords;
    }

    @Override
    public Set<String> getLabHead() {
        return Collections.EMPTY_SET;
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
        Set<String> datsetFiles = new HashSet<>();
        datsetFiles.add(String.format(Constants.METABOLOMICS_WORKBENCH_DATA, getIdentifier()));
        return datsetFiles;
    }

    @Override
    public Map<String, Set<String>> getCrossReferences() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public Map<String, Set<String>> getOtherAdditionals() {

        Map<String, Set<String>> additionals = new HashMap<>();
        if (factors != null && factors.factors != null) {
            Set<String> factorStrings = new HashSet<>();
            factors.factors.values().forEach(s -> factorStrings.add(s.getFactors().trim()));
            additionals.put(Field.STUDY_FACTORS.getName(), factorStrings);
        }
        if (analysis != null && analysis.analysisMap != null && analysis.analysisMap.size() > 0) {
            Set<String> types = new HashSet<>();
            analysis.analysisMap.values().forEach(s -> {
                String mapTerm = Synonyms.getTermBySynonym(s.getType());
                if (mapTerm == null) {
                    mapTerm = s.getType();
                }
                types.add(mapTerm);
                if (s.getMs_type() != null && s.getMs_type().length() > 0) {
                    types.add(s.getMs_type().trim());
                }
            });
            additionals.put(Field.TECHNOLOGY_TYPE.getName(), types);
        }

        if (metabolites != null && metabolites.metabolites != null && metabolites.metabolites.size() > 0) {
            Set<String> metaboliteNames = new HashSet<>();
            Set<String> pubchemIds = new HashSet<>();
            metabolites.metabolites.values().forEach(metabolite -> {
                if (metabolite.getName() != null && metabolite.getName().length() > 0) {
                    metaboliteNames.add(metabolite.getName().trim());
                }
                if (metabolite.getPubchem() != null && metabolite.getPubchem().length() > 0) {
                    pubchemIds.add(metabolite.getPubchem());
                }
            });
            additionals.put(Field.PUBCHEM_ID.getName(), pubchemIds);
            additionals.put(Field.METABOLITE_NAME.getName(), metaboliteNames);
        }
        return additionals;
    }

    public void setAnalysis(AnalysisList analysis) {
        this.analysis = analysis;
    }

    public void setDiseases(Set<String> diseases) {
        this.diseases = diseases;
    }

    public void setFactors(FactorList factors) {
        this.factors = factors;
    }

    public void setTissues(Set<String> tissues) {
        this.tissues = tissues;
    }

    public void setSpecies(Set<Specie> species) {
        this.species = species;
    }

    public void setMetabolites(MetaboliteList metabolites) {
        this.metabolites = metabolites;
    }
}
