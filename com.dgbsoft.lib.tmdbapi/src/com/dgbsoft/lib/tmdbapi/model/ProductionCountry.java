package com.dgbsoft.lib.tmdbapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.dgbsoft.lib.tmdbapi.model.core.AbstractJsonMapping;


@JsonRootName("production_country")
public class ProductionCountry extends AbstractJsonMapping {


    @JsonProperty("iso_3166_1")
    private String isoCode;
    @JsonProperty("name")
    private String name;


    public String getIsoCode() {
        return isoCode;
    }


    public String getName() {
        return name;
    }
}
