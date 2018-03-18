package com.example.android.popularmoviesstage1.reviews;

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

public class ReviewNetworkUtils {
    private static final String LOG_TAG = ReviewNetworkUtils.class.getSimpleName();

    //method to build URL for acquiring trailers
    public static URL buildUrlForReviews(Context context, int id) {
        Uri reviewUri = Uri.parse(context.getString(R.string.base_url)).buildUpon()
                .appendEncodedPath(context.getString(R.string.base_path))
                .appendEncodedPath(Integer.toString(id))
                .appendEncodedPath(context.getString(R.string.api_reviews))
                .build();
        try {
            URL reviewURL = new URL(reviewUri.toString());
            Log.v(LOG_TAG, "URL: " + reviewURL);
            return reviewURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //JSON parsing method for extracting an Array of strings (trailer ids)
    public static ArrayList<ReviewItem> extractReviewsFromJson(String jsonResponse) {
        ArrayList<ReviewItem> reviewItems = new ArrayList<>();
        //if the jsonString is empty, return early
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        //extract key from each trailer object
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("results");

            String author;
            String content;

            // If there are results in the features array
            for (int i = 0; i < itemsArray.length(); i++) {
                //Extract book items from the JSON response (parse the JSON)
                JSONObject reviewObject = itemsArray.getJSONObject(i);

                if (reviewObject.has("author")) {
                    author = reviewObject.getString("author");
                } else {
                    author = null;
                }

                if (reviewObject.has("content")) {
                    content = reviewObject.getString("content");
                } else {
                    content = null;
                }

                ReviewItem foundReview = new ReviewItem(author, content);
                reviewItems.add(foundReview);
            }
            return reviewItems;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing review JSON results", e);
        }
        return null;
    }
}
