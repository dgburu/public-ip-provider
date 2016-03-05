package com.dgbsoft.lib.tmdbapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.dgbsoft.lib.tmdbapi.model.core.ResponseStatus;


public class MovieListCreationStatus extends ResponseStatus {

    @JsonProperty("list_id")
    private String listId;


    public String getListId() {
        return listId;
    }
}
