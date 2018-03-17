package com.example.android.popularmoviesstage1.trailers;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmoviesstage1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by izzystannett on 15/03/2018.
 */

public class TrailerNetworkUtils {

    private static final String LOG_TAG = TrailerNetworkUtils.class.getSimpleName();

    //method to build URL for acquiring trailers
    public static URL buildUrlForTrailers(Context context, int id) {
        Uri trailerUri = Uri.parse(context.getString(R.string.base_url)).buildUpon()
                .appendEncodedPath(context.getString(R.string.base_path))
                .appendEncodedPath(Integer.toString(id))
                .appendEncodedPath(context.getString(R.string.api_videos))
                .build();
        try {
            URL trailerURL = new URL(trailerUri.toString());
            Log.v(LOG_TAG, "URL: " + trailerURL);
            return trailerURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //JSON parsing method for extracting an Array of strings (trailer ids)
    public static ArrayList<String> extractTrailerIdsFromJson(String jsonResponse) {
        ArrayList<String> trailerIDs = new ArrayList<>();
        //if the jsonString is empty, return early
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        //extract key from each trailer object
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("results");

            String key;

            // If there are results in the features array
            for (int i = 0; i < itemsArray.length(); i++) {
                //Extract book items from the JSON response (parse the JSON)
                JSONObject trailerObject = itemsArray.getJSONObject(i);

                if (trailerObject.has("key")) {
                    key = trailerObject.getString("key");
                } else {
                    key = null;
                }
                trailerIDs.add(key);
            }
            return trailerIDs;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing trailer JSON results", e);
        }
        return null;
    }
}
