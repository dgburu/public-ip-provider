package com.dgbsoft.lib.tmdbapi;

import com.dgbsoft.lib.tmdbapi.model.Credits;
import com.dgbsoft.lib.tmdbapi.model.tv.TvEpisode;
import com.dgbsoft.lib.tmdbapi.tools.ApiUrl;

import static com.dgbsoft.lib.tmdbapi.TmdbTV.TMDB_METHOD_TV;
import static com.dgbsoft.lib.tmdbapi.TmdbTvEpisodes.EpisodeMethod.credits;
import static com.dgbsoft.lib.tmdbapi.TmdbTvSeasons.TMDB_METHOD_TV_SEASON;
import static com.dgbsoft.lib.tmdbapi.Utils.asStringArray;


public class TmdbTvEpisodes extends AbstractTmdbApi {


    public static final String TMDB_METHOD_TV_EPISODE = "episode";


    public static enum EpisodeMethod {
        // base method shared by all tv apis
        credits, external_ids, images, videos
        // specific method for episodes
        // ...tbd
    }


    TmdbTvEpisodes(TmdbApi tmdbApi) {
        super(tmdbApi);
    }


    public TvEpisode getEpisode(int seriesId, int seasonNumber, int episodeNumber, String language, EpisodeMethod... appendToResponse) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_TV, seriesId, TMDB_METHOD_TV_SEASON, seasonNumber, TMDB_METHOD_TV_EPISODE, episodeNumber);

        apiUrl.addLanguage(language);

        apiUrl.appendToResponse(asStringArray(appendToResponse));

        return mapJsonResult(apiUrl, TvEpisode.class);
    }


    public Credits getCredits(int seriesId, int seasonNumber, int episodeNumber, String language) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_TV, seriesId, TMDB_METHOD_TV_SEASON, seasonNumber, TMDB_METHOD_TV_EPISODE, episodeNumber, credits);

        apiUrl.addLanguage(language);

        return mapJsonResult(apiUrl, Credits.class);
    }
}
