package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;


public class TweetActivity extends ActionBarActivity {

    private static final int REQUEST_COMPOSE = 1337;

    private Tweet tweet;
    private User userProfile;
    private ViewHolder viewHolder;

    private static class ViewHolder {
        ImageView profileImage;
        TextView username;
        TextView body;
        TextView time;
        TextView retweetCount;
        TextView favCount;
        TextView handle;
        ImageButton reply;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tweet = (Tweet) getIntent().getSerializableExtra("tweet");

        getSupportActionBar().setHomeButtonEnabled(true);

        fetchUserProfile();
        setupView();

        viewHolder.username.setText(tweet.getUser().getName());
        viewHolder.handle.setText("@"+tweet.getUser().getScreenName());

        viewHolder.body.setText(tweet.getBody());
        viewHolder.body.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.time.setText(tweet.getFormattedCreatedAt());

        // Call Reply listener if listener is set
        viewHolder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TweetActivity.this, ComposeActivity.class);
                i.putExtra("profile", userProfile);
                i.putExtra("reply", tweet);
                startActivityForResult(i, REQUEST_COMPOSE);
            }
        });

        if(tweet.getRetweetCount() > 0) {
            viewHolder.retweetCount.setText(Integer.toString(tweet.getRetweetCount()));
        } else {
            viewHolder.retweetCount.setText("");
        }

        if(tweet.getFavouritesCount() > 0) {
            viewHolder.favCount.setText(Integer.toString(tweet.getFavouritesCount()));
        } else {
            viewHolder.favCount.setText("");
        }

        Picasso.with(this)
                .load(tweet.getUser().getProfileImageUrl())
                .into(viewHolder.profileImage);
    }

    private void setupView() {
        viewHolder = new ViewHolder();
        viewHolder.profileImage = (ImageView) findViewById(R.id.ivProfileImage);
        viewHolder.username = (TextView) findViewById(R.id.tvUsername);
        viewHolder.body = (TextView) findViewById(R.id.tvBody);
        viewHolder.time = (TextView) findViewById(R.id.tvTime);
        viewHolder.retweetCount = (TextView) findViewById(R.id.tvRetweetsCount);
        viewHolder.favCount = (TextView) findViewById(R.id.tvFavCount);
        viewHolder.handle = (TextView) findViewById(R.id.tvHandle);
        viewHolder.reply = (ImageButton) findViewById(R.id.btnReply);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchUserProfile() {
        TwitterApplication.getRestClient().getAccount(new JsonHttpResponseHandler() {
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
}
