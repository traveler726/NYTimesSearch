package com.example.jjbeck.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jjbeck on 11/15/16.
 */

public class Article implements Serializable{

    public static String NYTimesDomain = "http://www.nytimes.com/";
    public static String EMPTY_STRING  = "";

    private String webUrl;
    private String headline;
    private String thumbnail;

    /* --------------------------------------------------
       Hide the default constructor -
       only construct from json objects for now!
       ---------------------------------------------------*/
    protected Article() {}

    /* --------------------------------------------------
        Construct object from a json object.
       ---------------------------------------------------*/
    public Article(JSONObject jsonObject) {
        try {
            webUrl   = jsonObject.getString("web_url");
            headline = jsonObject.getJSONObject("headline").getString("main");

            // Initially just get the first entry.
            JSONArray multiMedia = jsonObject.getJSONArray("multimedia");
            if (multiMedia.length() > 0) {
                JSONObject multiMediaJson = multiMedia.getJSONObject(0);
                thumbnail = NYTimesDomain + multiMediaJson.getString("url"); // relativeURL.
            } else {
                thumbnail = EMPTY_STRING;
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /* ----------------------------------------------------
        Factory static to generate a list from json array
       ---------------------------------------------------- */
    public static ArrayList<Article> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Article> articles = new ArrayList<Article>(jsonArray.length());

        for (int index = 0; index < jsonArray.length(); index ++) {
            try {
                articles.add(new Article(jsonArray.getJSONObject(index)));
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
        return articles;
    }


    /* --------------------------------------------------
        Getters for the fields of the object
       --------------------------------------------------- */
    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
