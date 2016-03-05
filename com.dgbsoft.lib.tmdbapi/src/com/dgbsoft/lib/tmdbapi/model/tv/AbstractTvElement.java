package com.dgbsoft.lib.tmdbapi.model.tv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.dgbsoft.lib.tmdbapi.model.Credits;
import com.dgbsoft.lib.tmdbapi.model.ExternalIds;
import com.dgbsoft.lib.tmdbapi.model.MovieImages;
import com.dgbsoft.lib.tmdbapi.model.Video;
import com.dgbsoft.lib.tmdbapi.model.core.NamedIdElement;

import java.util.List;


public class AbstractTvElement extends NamedIdElement {


    // Appendable responses for all tv elements

    @JsonProperty("credits")
    private Credits credits;

    @JsonProperty("external_ids")
    private ExternalIds externalIds;

    @JsonProperty("images")
    private MovieImages images;

    @JsonProperty("videos")
    private Video.Results videos;


    public Credits getCredits() {
        return credits;
    }


    public ExternalIds getExternalIds() {
        return externalIds;
    }


    public MovieImages getImages() {
        return images;
    }

    public void setExternalIds(ExternalIds e) {
    	externalIds = e;
    }
    
    public void setCredits(Credits c) {
    	credits = c;
    }
    
    public List<Video> getVideos() {
        return videos != null ? videos.getVideos() : null;
    }
}
