package uk.ac.ebi.ddi.api.readers.gpmdb.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ebi.ddi.api.readers.model.IAPIDataset;
import uk.ac.ebi.ddi.api.readers.utils.Constants;
import uk.ac.ebi.ddi.xml.validator.utils.OmicsType;

import java.util.*;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 03/12/2015
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Model implements IAPIDataset {

    @JsonProperty("parameter")
    Parameter parameter;

    @JsonProperty("result")
    Result result;

    @JsonProperty("sample")
    Sample sample;

    String model;

    Map<String, Set<String>> proteins;

    Map<String, Set<String>> additionals;

    @Override
    public String getIdentifier() {
        if (parameter != null && parameter.getIdentifier() != null) {
            return parameter.getIdentifier();
        }
        return null;
    }

    @Override
    public String getName() {
        if (sample != null && sample.getTitle() != null) {
            return sample.getTitle();
        }
        return getIdentifier();
    }

    @Override
    public String getDescription() {
        if (sample != null && sample.getDescription() != null) {
            return sample.getDescription();
        }
        return Constants.EMPTY_STRING;
    }

    @Override
    public String getDataProtocol() {
        return Constants.EMPTY_STRING;
    }

    @Override
    public String getPublicationDate() {
        if (result != null && result.getSubmissionDate() != null) {
            return result.getSubmissionDate();
        }
        return null;
    }

    @Override
    public Map<String, String> getOtherDates() {
        return Collections.emptyMap();
    }

    @Override
    public String getSampleProcotol() {
        return Constants.EMPTY_STRING;
    }

    @Override
    public Set<String> getOmicsType() {
        Set<String> omicsType = new HashSet<>();
        omicsType.add(OmicsType.PROTEOMICS.getName());
        return omicsType;
    }

    @Override
    public String getRepository() {
        return Constants.GPMDB;
    }

    @Override
    public String getFullLink() {
        return Constants.GPMDB_MODEL_LINK + getIdentifier();
    }

    @Override
    public Set<String> getInstruments() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getSpecies() {
        Set<String> species = new HashSet<>();
        if (parameter != null && parameter.getTaxonomy() != null) {
            String[] taxonomys = parameter.getTaxonomy().split(",");
            Arrays.asList(taxonomys).forEach(s -> {
                s = s.replace("_", " ");
                species.add(s);
            });
        }
        return species;
    }

    @Override
    public Set<String> getCellTypes() {
        Set<String> terms = new HashSet<>();
        if (sample != null && sample.cell_type != null && sample.cell_type.length() > 0) {
            terms.addAll(termToSet(sample.cell_type));
        }
        if (sample != null && sample.cell_culture != null && sample.cell_culture.length() > 0) {
            terms.addAll(termToSet(sample.cell_culture));
        }

        if (sample != null && sample.go_subcellular != null && sample.go_subcellular.length() > 0) {
            terms.addAll(termToSetSplit(sample.go_subcellular));
        }
        return terms;
    }

    @Override
    public Set<String> getDiseases() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getTissues() {
        Set<String> tissues = new HashSet<>();
        if (sample != null && sample.getTissue() != null) {
            tissues.addAll(termToSet(sample.getTissue()));
        }
        return tissues;
    }

    @Override
    public Set<String> getSoftwares() {
        Set<String> softwares = new HashSet<>();
        if (result != null && result.software != null) {
            softwares.add(result.software);
        }
        return softwares;
    }

    @Override
    public Set<String> getSubmitter() {
        Set<String> names = new HashSet<>();
        names.add(Constants.GPMDB_CONTACT_TEAM);
        if (sample != null && sample.submitter != null && sample.submitter.length() > 0) {
            names.add(sample.submitter);
        }
        return names;
    }

    @Override
    public Set<String> getSubmitterEmails() {
        Set<String> names = new HashSet<>();
        names.add(Constants.GPMDB_CONTACT);
        if (sample != null && sample.email != null && sample.email.length() > 0) {
            names.add(sample.email);
        }
        return names;
    }

    @Override
    public Set<String> getSubmitterAffiliations() {
        Set<String> names = new HashSet<>();
        names.add(Constants.GPMDB_AFILLIATION);
        if (sample != null && sample.affialiation != null && sample.affialiation.length() > 0) {
            names.add(sample.affialiation);
        }
        return names;
    }

    @Override
    public Set<String> getSubmitterKeywords() {
        return new HashSet<>(Arrays.asList(Constants.GPMDB_TAGS));
    }

    @Override
    public Set<String> getLabHead() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getLabHeadMail() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getLabHeadAffiliation() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getDatasetFiles() {
        Set<String> files = new HashSet<>();
        if (model != null) {
            files.add(model);
        }
        return files;
    }

    @Override
    public Map<String, Set<String>> getCrossReferences() {
        Map<String, Set<String>> crossReferences = new HashMap<>();
        if (proteins != null) {
            proteins.forEach(crossReferences::put);
        }
        return crossReferences;
    }

    @Override
    public Map<String, Set<String>> getOtherAdditionals() {
        if (additionals != null && additionals.size() > 0) {
            return additionals;
        }
        return Collections.emptyMap();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    private Set<String> termToSet(String sTerm) {
        Set<String> terms = new HashSet<>();
        if (sTerm != null && !sTerm.equalsIgnoreCase(Constants.GPMDB_UKNOKNOWN_FILTER)) {
            String[] termArr = sTerm.split(",");
            Arrays.asList(termArr).forEach(s -> {
                s = s.trim();
                if (s.length() > 0) {
                    int index = s.indexOf(" ");
                    if (index != -1) {
                        terms.add(s.substring(0, index - 1));
                        terms.add(s.substring(index + 1, s.length()));
                    } else {
                        terms.add(s);
                    }
                }

            });

        }
        return terms;
    }

    private Set<String> termToSetSplit(String sTerm) {
        Set<String> terms = new HashSet<>();
        if (sTerm != null && !sTerm.equalsIgnoreCase(Constants.GPMDB_UKNOKNOWN_FILTER)) {
            String[] termArr = sTerm.split(",");
            Arrays.asList(termArr).forEach(s -> {
                if (s.trim().length() > 0) {
                    terms.add(s);
                }
            });
        }
        return terms;
    }

    public void addProteins(Map<String, Set<String>> proteins) {
        if (this.proteins == null) {
            this.proteins = new HashMap<>();
        }
        if (proteins != null && proteins.size() > 0) {
            proteins.forEach((k, s) -> {
                Set<String> values = new HashSet<>();
                if (this.proteins.containsKey(k)) {
                    values = this.proteins.get(k);
                }
                values.addAll(s);
                this.proteins.put(k, values);
            });
        }
    }

    public void addOtherAdditionals(Map<String, Set<String>> proteinNames) {
        if (proteinNames != null && proteinNames.size() > 0) {
            if (additionals == null) {
                additionals = new HashMap<>();
            }
            proteinNames.forEach((k, s) -> additionals.put(k, s));
        }
    }
}
