package com.dgbsoft.lib.tmdbapi;

import com.dgbsoft.lib.tmdbapi.tools.MovieDbException;


public class TmdbChanges extends AbstractTmdbApi {

    TmdbChanges(TmdbApi tmdbApi) {
        super(tmdbApi);
    }


    public void getMovieChangesList(int page, String startDate, String endDate) {
        throw new MovieDbException("Not implemented yet");
    }


    public void getPersonChangesList(int page, String startDate, String endDate) {
        throw new MovieDbException("Not implemented yet");
    }
}
