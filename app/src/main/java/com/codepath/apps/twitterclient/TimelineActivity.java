package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends ActionBarActivity implements TweetsArrayAdapter.TweetReplyActionListener {

    private static final int REQUEST_COMPOSE = 1337;

    private TwitterClient client;

    private User userProfile;

    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;

    private SwipeRefreshLayout swipeContainer;
    private ListView lvTweets;

    private JsonHttpResponseHandler moreTweetsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();

        setupView();
        setupListeners();

        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        aTweets.setReplyListener(this);
        lvTweets.setAdapter(aTweets);

        fetchUserProfile();
        populateTimeline();
    }

    private void fetchUserProfile() {
        client.getAccount(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                userProfile = User.fromJSON(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void setupView() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvTweets = (ListView) findViewById(R.id.lvTweets);
    }

    private void setupListeners() {
        moreTweetsHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                aTweets.addAll(tweets);

                // Persist in database
                for(Tweet t: tweets) {
                    t.getUser().save();
                    t.save();
                }

                aTweets.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeContainer.setRefreshing(false);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                client.getHomeTimeline(25, tweets.get(0).getUid(), -1, moreTweetsHandler);
            }
        });

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                client.getHomeTimeline(25, -1, tweets.get(tweets.size()-1).getUid(), moreTweetsHandler);
            }
        });
    }

    private void populateTimeline() {
        long since_uid;

        // Request persistence stored tweets
        List<Tweet> tweets = Tweet.getAll();
        aTweets.addAll(tweets);

        // If we don't have anything stored request since beginning
        if (tweets.size() == 0) {
            since_uid = 1;
        } else {
            since_uid = tweets.get(0).getUid();
        }

        aTweets.notifyDataSetChanged();

        // Try network request
        client.getHomeTimeline(25, since_uid, -1, moreTweetsHandler);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_compose) {
            Intent i = new Intent(this, ComposeActivity.class);
            i.putExtra("profile", userProfile);
            startActivityForResult(i, REQUEST_COMPOSE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == REQUEST_COMPOSE) {
            Tweet t = (Tweet) data.getSerializableExtra("tweet");
            tweets.add(0, t);
            aTweets.notifyDataSetChanged();
        }
    }

    public void OnReplyAction(Tweet tweet) {
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("profile", userProfile);
        i.putExtra("reply", tweet);
        startActivityForResult(i, REQUEST_COMPOSE);
    }
}
