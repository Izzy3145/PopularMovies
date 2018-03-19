package com.example.android.popularmoviesstage1.movies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmoviesstage1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by izzystannett on 25/02/2018.
 */

//this class defines the factory methods to be used in the AsyncTask on the MainActivity

public class MovieNetworkUtils {

    private static final String LOG_TAG = MovieNetworkUtils.class.getSimpleName();
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE_URL = "w185";

    //method to build URL for sorting order,; either sort by popularity or top rated
    public static URL buildUrlForMovieDetails(String sortUrl, Context context) {
        String BASE_QUERY;
        if (sortUrl.equals(context.getString(R.string.pref_pop_value))) {
            BASE_QUERY = context.getString(R.string.api_popular);
        } else {
            BASE_QUERY = context.getString(R.string.api_top_rated);
        }
        Uri sortOrderUri = Uri.parse(context.getString(R.string.base_url)).buildUpon()
                .appendEncodedPath(context.getString(R.string.base_path))
                .appendEncodedPath(BASE_QUERY)
                .build();
        try {
            URL sortOrderURL = new URL(sortOrderUri.toString());
            Log.v(LOG_TAG, "URL: " + sortOrderURL);
            return sortOrderURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //method to connect to internet and return jsonResponse
    public static String makeHttpRequest(URL url) throws IOException {
        //the output string starts as an empty string, ready to be 'built'
        String jsonResponse = "";
        //if the string is null, then return early
        if (url == null) {
            return jsonResponse;
        }
        //initialise objects
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            //try to connect
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //if connection successful, then read input stream
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Problem with connection. Error response code: " +
                        urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                //call close() method, which may cause IOExcpetion
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //convert inputStream from byte-form to readable String
    private static String readFromStream(InputStream inputStream) throws IOException {
        //create String builder so the buffered reader can continually add characters until end of inputStream
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            //call the readLine() method, which may cause an IOException
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //JSON parsing method for extracting an Array of MovieItems
    public static ArrayList<MovieItem> extractMovieDetailsFromJson(String jsonResponse) {
        ArrayList<MovieItem> movieItems = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("results");

            String originalTitle;
            String imageUrl;
            String plotSynopsis;
            int userRating;
            String releaseDate;
            int id;

            // If there are results in the features array
            for (int i = 0; i < itemsArray.length(); i++) {
                //Extract book items from the JSON response (parse the JSON)
                JSONObject movieObject = itemsArray.getJSONObject(i);

                if (movieObject.has("original_title")) {
                    originalTitle = movieObject.getString("original_title");
                } else {
                    originalTitle = null;
                }

                if (movieObject.has("poster_path")) {
                    String urlPath = movieObject.getString("poster_path");
                    imageUrl = BASE_IMAGE_URL + IMAGE_SIZE_URL + urlPath;
                } else {
                    imageUrl = null;
                }

                if (movieObject.has("overview")) {
                    plotSynopsis = movieObject.getString("overview");
                } else {
                    plotSynopsis = null;
                }

                if (movieObject.has("vote_average")) {
                    userRating = movieObject.getInt("vote_average");
                } else {
                    userRating = 0;
                }

                if (movieObject.has("release_date")) {
                    releaseDate = (movieObject.getString("release_date")).substring(0, 4);
                } else {
                    releaseDate = null;
                }

                if (movieObject.has("id")) {
                    id = movieObject.getInt("id");
                } else {
                    id = 0;
                }

                //create new BookItem object and add to the Array List
                MovieItem foundMovie = new MovieItem(originalTitle, imageUrl, plotSynopsis,
                        userRating, releaseDate, id);
                movieItems.add(foundMovie);
            }
            return movieItems;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing movie item JSON results", e);
        }
        return null;
    }
}
