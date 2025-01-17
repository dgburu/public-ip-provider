package com.dgbsoft.lib.tmdbapi.model.config;

import com.dgbsoft.lib.tmdbapi.model.core.AbstractJsonMapping;

import java.util.Collection;
import java.util.List;

public class Timezone extends AbstractJsonMapping {

    private String name;
    private String country;


    public Timezone(String name, String country) {
        this.name = name;
        this.country = country;
    }



	public String getCountry() {
        return country;
    }
    

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }
}
