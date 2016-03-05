package com.dgbsoft.lib.tmdbapi.tools;

import java.net.URL;


public interface UrlReader {

    String request(URL url, String jsonBody, RequestMethod requestMethod);
}
