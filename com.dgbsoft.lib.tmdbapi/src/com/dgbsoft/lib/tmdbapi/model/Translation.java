package com.dgbsoft.lib.tmdbapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.dgbsoft.lib.tmdbapi.model.core.NamedElement;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class Translation extends NamedElement {

    @JsonProperty("iso_639_1")
    private String isoCode;


    @JsonProperty("english_name")
    private String englishName;


    public String getEnglishName() {
        return englishName;
    }


    public String getIsoCode() {
        return isoCode;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
