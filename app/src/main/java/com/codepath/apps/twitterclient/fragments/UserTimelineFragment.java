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

import java.util.List;

/**
 * Created by yeyus on 2/15/15.
 */
public class UserTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);

        return  userFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, container, savedInstanceState);

        populateTimeline();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                client.getHomeTimeline(25, UserTimelineFragment.this.getLatestTweet().getUid(), -1, moreTweetsHandler);
            }
        });

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                client.getHomeTimeline(25, -1, UserTimelineFragment.this.getOldestTweet().getUid(), moreTweetsHandler);
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
        // Try network request
        client.getUserTimeline(getArguments().getString("screen_name"), 25, 1, -1, moreTweetsHandler);
    }
}
