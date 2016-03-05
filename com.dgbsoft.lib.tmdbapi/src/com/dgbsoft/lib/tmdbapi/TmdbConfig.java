package com.dgbsoft.lib.tmdbapi;

import com.dgbsoft.lib.tmdbapi.model.config.ConfigResults;
import com.dgbsoft.lib.tmdbapi.tools.ApiUrl;


class TmdbConfig extends AbstractTmdbApi {

    public static final String TMDB_METHOD_CONFIGURATION = "configuration";


    TmdbConfig(TmdbApi tmdbApi) {
        super(tmdbApi);
    }


    public ConfigResults getConfig() {
        return mapJsonResult(new ApiUrl(TMDB_METHOD_CONFIGURATION), ConfigResults.class);
    }

}
