package com.dgbsoft.lib.tmdbapi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.dgbsoft.lib.tmdbapi.model.core.ResponseStatus;
import com.dgbsoft.lib.tmdbapi.model.core.ResponseStatusException;
import com.dgbsoft.lib.tmdbapi.tools.ApiUrl;
import com.dgbsoft.lib.tmdbapi.tools.MovieDbException;
import com.dgbsoft.lib.tmdbapi.tools.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;


public abstract class AbstractTmdbApi {

    public static final String PARAM_YEAR = "year";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_LANGUAGE = "language";
    public static final String PARAM_ID = "id";
    public static final String PARAM_ADULT = "include_adult";
    public static final String PARAM_API_KEY = "api_key";

    protected static final ObjectMapper jsonMapper = new ObjectMapper();

    protected final TmdbApi tmdbApi;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    AbstractTmdbApi(TmdbApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }


    public <T> T mapJsonResult(ApiUrl apiUrl, Class<T> someClass) {
        return mapJsonResult(apiUrl, someClass, null);
    }


    public <T> T mapJsonResult(ApiUrl apiUrl, Class<T> someClass, String jsonBody) {
        return mapJsonResult(apiUrl, someClass, jsonBody, RequestMethod.GET);
    }


    // see https://www.themoviedb.org/documentation/api/status-codes
    private static final Collection<Integer> SUCCESS_STATUS_CODES = Arrays.asList(
            1, // Success
            12, // The item/record was updated successfully.
            13 // The item/record was updated successfully.
    );


    public <T> T mapJsonResult(ApiUrl apiUrl, Class<T> someClass, String jsonBody, RequestMethod requestMethod) {
        String webpage = tmdbApi.requestWebPage(apiUrl, jsonBody, requestMethod);

//        System.out.println(webpage);

        try {
//            if(someClass.equals(TmdbTimezones.class)) {
//            	return (T) new TimezoneJsonMapper(webpage);
//            }

            // check if was error responseStatus
            ResponseStatus responseStatus = jsonMapper.readValue(webpage, ResponseStatus.class);
            // work around the problem that there's no status code for suspected spam names yet
            String suspectedSpam = "Unable to create list because: Description is suspected to be spam.";
            if (webpage.contains(suspectedSpam)) {
                responseStatus = new ResponseStatus(-100, suspectedSpam);
            }

            // if null, the json response was not a error responseStatus code, and but something else
            Integer statusCode = responseStatus.getStatusCode();
            if (statusCode != null && !SUCCESS_STATUS_CODES.contains(statusCode)) {
                throw new ResponseStatusException(responseStatus);
            }

            return jsonMapper.readValue(webpage, someClass);
        } catch (IOException ex) {
            throw new MovieDbException("mapping failed:\n" + webpage);
        }
    }
}
