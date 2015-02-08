package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yeyus on 2/7/15.
 */
public class User extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "uid")
    private long uid;

    @Column(name = "screenName")
    private String screenName;

    @Column(name = "profileImageUrl")
    private String profileImageUrl;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public static User fromJSON(JSONObject jsonObject) {
        User u = new User();

        try {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return u;
    }

}
