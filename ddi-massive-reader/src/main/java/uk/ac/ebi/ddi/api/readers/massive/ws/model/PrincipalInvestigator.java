package uk.ac.ebi.ddi.api.readers.massive.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This code is licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * == General Description ==
 * <p>
 * This class Provides a general information or functionalities for
 * <p>
 * ==Overview==
 * <p>
 * How to used
 * <p>
 * Created by yperez (ypriverol@gmail.com) on 19/12/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrincipalInvestigator {

    @JsonProperty("name")
    String name;

    @JsonProperty("email")
    String email;

    @JsonProperty("institution")
    String institution;

    @JsonProperty("country")
    String country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
