package com.codepath.apps.twitterclient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.apps.twitterclient.models.User;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "R37ou6CF9ybUZijkCvxj2oxdl";       // Change this
	public static final String REST_CONSUMER_SECRET = "UDKmTrxGXyKE2x2B3qfSserRr0UZNnZ8bd7mkHTfK3zW5rrVCy"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://tweetsappcallback"; // Change this (here and in manifest)

    private User userProfile;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(int count, long since_id, long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", String.valueOf(count));
        if (since_id > 0) {
            params.put("since_id", String.valueOf(since_id));
        }
        if(max_id > 0) {
            params.put("max_id", String.valueOf(max_id));
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(int count, long since_id, long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", String.valueOf(count));
        if (since_id > 0) {
            params.put("since_id", String.valueOf(since_id));
        }
        if(max_id > 0) {
            params.put("max_id", String.valueOf(max_id));
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, int count, long since_id, long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", String.valueOf(count));
        if (since_id > 0) {
            params.put("since_id", String.valueOf(since_id));
        }
        if(max_id > 0) {
            params.put("max_id", String.valueOf(max_id));
        }
        if(screenName != null) {
            params.put("screen_name", screenName);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void postTweet(String body, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", body);
        getClient().post(apiUrl, params, handler);
    }

    public void getAccount(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, null, handler);
    }

    public void setUserProfile(User userProfile) {
        this.userProfile = userProfile;
    }

    public User getUserProfile() {
        return  userProfile;
    }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}