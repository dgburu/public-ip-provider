package com.dgbsoft.lib.tmdbapi;

import com.dgbsoft.lib.tmdbapi.model.Artwork;
import com.dgbsoft.lib.tmdbapi.model.CollectionInfo;
import com.dgbsoft.lib.tmdbapi.model.MovieImages;
import com.dgbsoft.lib.tmdbapi.tools.ApiUrl;

import java.util.List;

import static com.dgbsoft.lib.tmdbapi.model.ArtworkType.BACKDROP;
import static com.dgbsoft.lib.tmdbapi.model.ArtworkType.POSTER;


public class TmdbCollections extends AbstractTmdbApi {

    public static final String TMDB_METHOD_COLLECTION = "collection";


    TmdbCollections(TmdbApi tmdbApi) {
        super(tmdbApi);
    }


    /**
     * This method is used to retrieve all of the basic information about a movie collection.
     * <p/>
     * You can get the ID needed for this method by making a getMovieInfo request for the belongs_to_collection.
     *
     * @param collectionId
     * @param language
     */
    public CollectionInfo getCollectionInfo(int collectionId, String language) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_COLLECTION, collectionId);

        apiUrl.addLanguage(language);

        return mapJsonResult(apiUrl, CollectionInfo.class);
    }


    /**
     * Get all of the images for a particular collection by collection id.
     *
     * @param collectionId
     * @param language
     */
    public List<Artwork> getCollectionImages(int collectionId, String language) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_COLLECTION, collectionId, "images");

        apiUrl.addLanguage(language);

        return mapJsonResult(apiUrl, MovieImages.class).getAll(POSTER, BACKDROP);
    }
}
