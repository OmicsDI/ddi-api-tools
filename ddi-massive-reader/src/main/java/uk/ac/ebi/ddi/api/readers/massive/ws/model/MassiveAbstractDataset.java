package uk.ac.ebi.ddi.api.readers.massive.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 06/11/15
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MassiveAbstractDataset {

    @JsonProperty("title")
    private String title;

    @JsonProperty("pi")
    PrincipalInvestigator[] principalInvestigator;

    @JsonProperty("instrument")
    private String instrument;

    @JsonProperty("complete")
    private String complete;

    @JsonProperty("private")
    private String privateStatus;

    @JsonProperty("user")
    private String user;

    @JsonProperty("species")
    private String species;


    @JsonProperty("created")
    String created;

    @JsonProperty("task")
    String task;

    private String url;

    public String getTitle() {
        return title;
    }

    public String getInstrument() {
        return instrument;
    }

    public String getUser() {
        return user;
    }

    String getStringSpecies() {
        return species;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTask() {
        return task;
    }


    @Override
    public String toString() {
        return "MassiveAbstractDataset{" +
                "title='" + title + '\'' +
                ", principalInvestigator='" + Arrays.toString(principalInvestigator) + '\'' +
                ", instrument='" + instrument + '\'' +
                ", complete='" + complete + '\'' +
                ", privateStatus='" + privateStatus + '\'' +
                ", user='" + user + '\'' +
                ", species='" + species + '\'' +
                ", created='" + created + '\'' +
                ", task='" + task + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
