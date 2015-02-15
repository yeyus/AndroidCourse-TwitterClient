package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.EndlessScrollListener;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.List;

/**
 * Created by yeyus on 2/14/15.
 */
public class HomeTweetsListFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, container, savedInstanceState);

        populateTimeline();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                client.getHomeTimeline(25, HomeTweetsListFragment.this.getLatestTweet().getUid(), -1, moreTweetsHandler);
            }
        });

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                client.getHomeTimeline(25, -1, HomeTweetsListFragment.this.getOldestTweet().getUid(), moreTweetsHandler);
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
    }

    private void populateTimeline() {
        long since_uid;

        // Request persistence stored tweets
        List<Tweet> tweets = Tweet.getAll();
        this.addAll(tweets);

        // If we don't have anything stored request since beginning
        if (tweets.size() == 0) {
            since_uid = 1;
        } else {
            since_uid = this.getLatestTweet().getUid();
        }

        // Try network request
        client.getHomeTimeline(25, since_uid, -1, moreTweetsHandler);
    }
}
