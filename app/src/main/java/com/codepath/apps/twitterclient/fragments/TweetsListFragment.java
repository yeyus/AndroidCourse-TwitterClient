package com.codepath.apps.twitterclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.twitterclient.ComposeActivity;
import com.codepath.apps.twitterclient.EndlessScrollListener;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TimelineActivity;
import com.codepath.apps.twitterclient.TweetActivity;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeyus on 2/14/15.
 */
public class TweetsListFragment extends Fragment implements TweetsArrayAdapter.TweetReplyActionListener {

    private static final int REQUEST_COMPOSE = 1337;

    protected TweetsArrayAdapter aTweets;
    protected ArrayList<Tweet> tweets;

    protected SwipeRefreshLayout swipeContainer;
    protected ListView lvTweets;

    protected JsonHttpResponseHandler moreTweetsHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        setupView(v);
        setupListeners();
        lvTweets.setAdapter(aTweets);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
        aTweets.setReplyListener(this);

    }

    private void setupView(View v) {
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
    }

    private void setupListeners() {
        moreTweetsHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                TweetsListFragment.this.addAll(tweets);

                // Persist in database
                for(Tweet t: tweets) {
                    t.getUser().save();
                    t.save();
                }

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), TweetActivity.class);
                i.putExtra("tweet", aTweets.getItem(position));
                startActivity(i);
            }
        });
    }

    @Override
    public void OnReplyAction(Tweet tweet) {
        Intent i = new Intent(getActivity(), ComposeActivity.class);
        i.putExtra("profile", TwitterApplication.getRestClient().getUserProfile());
        i.putExtra("reply", tweet);
        startActivityForResult(i, REQUEST_COMPOSE);
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void add(Tweet tweet) {
        aTweets.add(tweet);
    }

    protected Tweet getLatestTweet() {
        return tweets.get(0);
    }

    protected Tweet getOldestTweet() {
        return tweets.get(tweets.size()-1);
    }
}
