package com.dgbsoft.lib.tmdbapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.dgbsoft.lib.tmdbapi.model.core.AbstractJsonMapping;
import com.dgbsoft.lib.tmdbapi.model.people.Person;
import com.dgbsoft.lib.tmdbapi.model.tv.TvSeries;

import java.util.List;


public class FindResults extends AbstractJsonMapping {

    @JsonProperty("movie_results")
    private List<MovieDb> movieResults;

    @JsonProperty("person_results")
    private List<Person> personResults;

    @JsonProperty("tv_results")
    private List<TvSeries> tvResults;


    public List<MovieDb> getMovieResults() {
        return movieResults;
    }


    public List<Person> getPersonResults() {
        return personResults;
    }


    public List<TvSeries> getTvResults() {
        return tvResults;
    }
}
