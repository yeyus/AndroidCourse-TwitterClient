package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yeyus on 2/7/15.
 */
@Table(name = "Tweets")
public class Tweet extends Model {

    @Column(name = "uid")
    private long uid;

    @Column(name = "body")
    private String body;

    private User user;

    @Column(name = "createdAt")
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tw = new Tweet();

        try {
            tw.body = jsonObject.getString("text");
            tw.uid = jsonObject.getLong("id");
            tw.createdAt = jsonObject.getString("created_at");
            tw.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tw;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                tweets.add(Tweet.fromJSON(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }
}
