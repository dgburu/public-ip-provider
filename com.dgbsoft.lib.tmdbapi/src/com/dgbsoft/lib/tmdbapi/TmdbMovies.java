package com.dgbsoft.lib.tmdbapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.dgbsoft.lib.tmdbapi.model.*;
import com.dgbsoft.lib.tmdbapi.model.changes.ChangesItems;
import com.dgbsoft.lib.tmdbapi.model.core.IdElement;
import com.dgbsoft.lib.tmdbapi.model.core.MovieResultsPage;
import com.dgbsoft.lib.tmdbapi.model.core.SessionToken;
import com.dgbsoft.lib.tmdbapi.model.keywords.Keyword;
import com.dgbsoft.lib.tmdbapi.tools.ApiUrl;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.dgbsoft.lib.tmdbapi.TmdbAccount.PARAM_SESSION;
import static com.dgbsoft.lib.tmdbapi.TmdbMovies.MovieMethod.videos;
import static com.dgbsoft.lib.tmdbapi.Utils.asStringArray;


public class TmdbMovies extends AbstractTmdbApi {

    // API Methods
    public static final String TMDB_METHOD_MOVIE = "movie";
    private static final String PARAM_START_DATE = "start_date";
    private static final String PARAM_END_DATE = "end_date";
    private static final String PARAM_COUNTRY = "country";


    // account_states and rating are not included as it wouldn't work anyway because of missing session id
    // --> inject session id into tmdb-instance?
    public static enum MovieMethod {
        alternative_titles, credits, images, keywords, releases,
        @Deprecated trailers,
        videos, // replacement for trailers
        translations, similar_movies,
        reviews, lists, changes, latest, upcoming, now_playing, popular, top_rated,
    }


    public TmdbMovies(TmdbApi tmdbApi) {
        super(tmdbApi);
    }


    /**
     * This method is used to retrieve all of the basic movie information.
     * <p/>
     * It will return the single highest rated poster and backdrop.
     *
     * @param movieId
     * @param language
     */
    public MovieDb getMovie(int movieId, String language, MovieMethod... appendToResponse) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, movieId);

        apiUrl.addLanguage(language);

        apiUrl.appendToResponse(asStringArray(appendToResponse));

        return mapJsonResult(apiUrl, MovieDb.class);
    }


    /**
     * This method is used to retrieve all of the alternative titles we have for a particular movie.
     *
     * @param movieId
     * @param country
     */
    public List<AlternativeTitle> getAlternativeTitles(int movieId, String country) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, movieId, "alternative_titles");

        if (StringUtils.isNotBlank(country)) {
            apiUrl.addParam(PARAM_COUNTRY, country);
        }


        return mapJsonResult(apiUrl, MoviesAlternativeTitles.class).getTitles();
    }


    public Credits getCredits(int movieId) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, movieId, "credits");

        return mapJsonResult(apiUrl, Credits.class);
    }


    /**
     * This method should be used when you’re wanting to retrieve all of the images for a particular movie.
     *
     * @param movieId
     * @param language
     */
    public MovieImages getImages(int movieId, String language) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, movieId, "images");

        apiUrl.addLanguage(language);

        return mapJsonResult(apiUrl, MovieImages.class);
    }


    /**
     * This method is used to retrieve all of the keywords that have been added to a particular movie.
     * <p/>
     * Currently, only English keywords exist.
     *
     * @param movieId
     */
    public List<Keyword> getKeywords(int movieId) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, movieId, "keywords");


        return mapJsonResult(apiUrl, KeywordResults.class).results;
    }


    private static class KeywordResults extends IdElement {

        @JsonProperty("keywords")
        List<Keyword> results;
    }


    /**
     * This method is used to retrieve all of the release and certification data we have for a specific movie.
     *
     * @param movieId
     * @param language
     */
    public List<ReleaseInfo> getReleaseInfo(int movieId, String language) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, movieId, "releases");


        apiUrl.addLanguage(language);

        return mapJsonResult(apiUrl, ReleaseInfoResults.class).countries;
    }


    public static class ReleaseInfoResults extends IdElement {

        @JsonProperty("countries")
        private List<ReleaseInfo> countries;


        public List<ReleaseInfo> getCountries() {
            return countries;
        }
    }


    /**
     * This method is used to retrieve all of the videos for a particular movie.
     * <p/>
     * Supported sites are YouTube and QuickTime.
     *
     * @param movieId
     * @param language
     */
    public List<Video> getVideos(int movieId, String language) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, movieId, videos);

        apiUrl.addLanguage(language);

        return mapJsonResult(apiUrl, Video.Results.class).getVideos();
    }


    /**
     * This method is used to retrieve a list of the available translations for a specific movie.
     *
     * @param movieId
     */
    public List<Translation> getTranslations(int movieId) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, movieId, "translations");

        return mapJsonResult(apiUrl, MovieTranslations.class).getTranslations();
    }


    /**
     * The similar movies method will let you retrieve the similar movies for a particular movie.
     * <p/>
     * This data is created dynamically but with the help of users votes on TMDb.
     * <p/>
     * The data is much better with movies that have more keywords
     *
     * @param movieId
     * @param language
     * @param page
     */
    public MovieResultsPage getSimilarMovies(int movieId, String language, Integer page) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, movieId, "similar");

        apiUrl.addLanguage(language);

        apiUrl.addPage(page);

        return mapJsonResult(apiUrl, MovieResultsPage.class);
    }


    /**
     * Get the lists that the movie belongs to
     *
     * @param movieId
     * @param sessionToken
     * @param language
     * @param page
     */
    public TmdbAccount.MovieListResultsPage getListsContaining(int movieId, SessionToken sessionToken, String language, Integer page) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, movieId, "lists");

        apiUrl.addParam(PARAM_SESSION, sessionToken);

        apiUrl.addLanguage(language);

        apiUrl.addPage(page);

        return mapJsonResult(apiUrl, TmdbAccount.MovieListResultsPage.class);
    }


    /**
     * Get the changes for a specific movie id.
     * <p/>
     * Changes are grouped by key, and ordered by date in descending order.
     * <p/>
     * By default, only the last 24 hours of changes are returned.
     * <p/>
     * The maximum number of days that can be returned in a single request is 14.
     * <p/>
     * The language is present on fields that are translatable.
     * <p/>
     * TODO: DOES NOT WORK AT THE MOMENT. This is due to the "value" item changing type in the ChangeItem
     *
     * @param movieId
     * @param startDate the start date of the changes, optional
     * @param endDate   the end date of the changes, optional
     */
    public ChangesItems getChanges(int movieId, String startDate, String endDate) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, movieId, "changes");

        if (StringUtils.isNotBlank(startDate)) {
            apiUrl.addParam(PARAM_START_DATE, startDate);
        }

        if (StringUtils.isNotBlank(endDate)) {
            apiUrl.addParam(PARAM_END_DATE, endDate);
        }

        return mapJsonResult(apiUrl, ChangesItems.class);

    }


    /**
     * This method is used to retrieve the newest movie that was added to TMDb.
     */
    public MovieDb getLatestMovie() {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, "latest");


        return mapJsonResult(apiUrl, MovieDb.class);
    }


    /**
     * Get the list of upcoming movies.
     * <p/>
     * This list refreshes every day.
     * <p/>
     * The maximum number of items this list will include is 100.
     */
    public MovieResultsPage getUpcoming(String language, Integer page) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, "upcoming");

        apiUrl.addLanguage(language);

        apiUrl.addPage(page);


        return mapJsonResult(apiUrl, MovieResultsPage.class);

    }


    /**
     * This method is used to retrieve the movies currently in theatres.
     * <p/>
     * This is a curated list that will normally contain 100 movies. The default response will return 20 movies.
     * <p/>
     *
     * @param language
     * @param page
     */
    public MovieResultsPage getNowPlayingMovies(String language, Integer page) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, "now-playing");

        apiUrl.addLanguage(language);

        apiUrl.addPage(page);

        return mapJsonResult(apiUrl, MovieResultsPage.class);
    }


    /**
     * This method is used to retrieve the daily movie popularity list.
     * <p/>
     * This list is updated daily. The default response will return 20 movies.
     * <p/>
     *
     * @param language
     * @param page
     */
    public MovieResultsPage getPopularMovies(String language, Integer page) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, "popular");

        apiUrl.addLanguage(language);

        apiUrl.addPage(page);

        return mapJsonResult(apiUrl, MovieResultsPage.class);
    }


    /**
     * This method is used to retrieve the top rated movies that have over 10 votes on TMDb.
     * <p/>
     * The default response will return 20 movies.
     * <p/>
     *
     * @param language
     * @param page
     */
    public MovieResultsPage getTopRatedMovies(String language, Integer page) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_MOVIE, "top-rated");

        apiUrl.addLanguage(language);

        apiUrl.addPage(page);


        return mapJsonResult(apiUrl, MovieResultsPage.class);
    }


}
