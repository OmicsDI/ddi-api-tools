package uk.ac.ebi.ddi.api.readers.massive.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 06/11/15
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AbstractDataset {

    @JsonProperty("title")
    String title;

    @JsonProperty("pi")
    PrincipalInvestigator[] principalInvestigator;

    @JsonProperty("instrument")
    String instrument;

    @JsonProperty("complete")
    String complete;

    @JsonProperty("private")
    String privateStatus;

    @JsonProperty("user")
    String user;

    @JsonProperty("species")
    String species;


    @JsonProperty("created")
    String created;

    @JsonProperty("task")
    String task;

    String url;

    public String getTitle() {
        return title;
    }

    public String getInstrument() {
        return instrument;
    }

    public String getUser() {
        return user;
    }

    public String getStringSpecies() {
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
        return "AbstractDataset{" +
                "title='" + title + '\'' +
                ", principalInvestigator='" + principalInvestigator + '\'' +
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
