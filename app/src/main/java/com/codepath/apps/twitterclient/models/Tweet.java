package com.codepath.apps.twitterclient.models;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by yeyus on 2/7/15.
 */
@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {

    private static final long serialVersionUID = 849209183L;

    @Column(name = "uid", index = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "body")
    private String body;

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    @Column(name = "createdAt")
    private String createdAt;

    @Column(name = "retweetCount")
    private int retweetCount;

    @Column(name = "favouritesCount")
    private int favouritesCount;

    public Tweet() { super(); }

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

    public int getRetweetCount() {
        return retweetCount;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public String getRelativeCreatedAt() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(createdAt).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public String getFormattedCreatedAt() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String strDate = "";
        try {
            Date date = sf.parse(createdAt);
            strDate = DateFormat.getDateTimeInstance().format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return strDate;
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tw = new Tweet();

        try {
            tw.body = jsonObject.getString("text");
            tw.uid = jsonObject.getLong("id");
            tw.createdAt = jsonObject.getString("created_at");
            tw.user = User.fromJSON(jsonObject.getJSONObject("user"));
            if(!jsonObject.isNull("retweet_count")) {
                tw.retweetCount = jsonObject.getInt("retweet_count");
            }
            if(!jsonObject.isNull("favourites_count")) {
                tw.favouritesCount = jsonObject.getInt("favourites_count");
            }
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

    public static List<Tweet> getAll() {
        return new Select()
                .from(Tweet.class)
                .orderBy("uid DESC")
                .limit(50)
                .execute();
    }
}
