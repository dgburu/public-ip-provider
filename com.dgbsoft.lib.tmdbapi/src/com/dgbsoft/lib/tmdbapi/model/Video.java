package com.dgbsoft.lib.tmdbapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.dgbsoft.lib.tmdbapi.model.core.IdElement;
import com.dgbsoft.lib.tmdbapi.model.core.NamedStringIdElement;

import java.util.List;


@JsonRootName("video")
public class Video extends NamedStringIdElement {

    @JsonProperty("site")
    private String site;

    @JsonProperty("key")
    private String key;

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("type")
    private String type;


    public static class Results extends IdElement {

        @JsonProperty("results")
        private List<Video> videos;


        public List<Video> getVideos() {
            return videos;
        }
    }


    public String getSite() {
        return site;
    }


    public Integer getSize() {
        return size;
    }


    public String getKey() {
        return key;
    }


    public String getType() {
        return type;
    }
}
