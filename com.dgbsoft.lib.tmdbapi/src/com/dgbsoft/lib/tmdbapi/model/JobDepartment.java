package com.dgbsoft.lib.tmdbapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.dgbsoft.lib.tmdbapi.model.core.AbstractJsonMapping;

import java.util.List;


public class JobDepartment extends AbstractJsonMapping {

    @JsonProperty("department")
    private String department;

    @JsonProperty("job_list")
    private List<String> jobs;


    public String getDepartment() {
        return department;
    }


    public List<String> getJobs() {
        return jobs;
    }


    public void setDepartment(String department) {
        this.department = department;
    }


    public void setJobs(List<String> jobs) {
        this.jobs = jobs;
    }

}
