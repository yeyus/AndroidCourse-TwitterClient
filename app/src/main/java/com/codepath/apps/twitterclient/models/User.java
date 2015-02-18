package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by yeyus on 2/7/15.
 */
@Table(name = "Users")
public class User extends Model implements Serializable {

    private static final long serialVersionUID = 849209187L;

    @Column(name = "name")
    private String name;

    @Column(name = "uid", index = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "screenName")
    private String screenName;

    @Column(name = "profileImageUrl")
    private String profileImageUrl;

    @Column(name = "followers")
    private int followers;

    @Column(name = "following")
    private int following;

    @Column(name = "description")
    private String description;

    public User() { super(); }

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

    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }

    public String getDescription() {
        return description;
    }

    public static User fromJSON(JSONObject jsonObject) {
        User u = new User();

        try {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
            u.followers = jsonObject.getInt("followers_count");
            u.following = jsonObject.getInt("friends_count");
            u.description = jsonObject.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return u;
    }

}
