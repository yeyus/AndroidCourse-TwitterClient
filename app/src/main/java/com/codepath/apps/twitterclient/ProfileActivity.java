package com.codepath.apps.twitterclient;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.twitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.twitterclient.models.User;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends ActionBarActivity {

    UserTimelineFragment userTimelineFragment;
    User profile;

    ImageView ivProfileImage;
    TextView tvScreenName;
    TextView tvName;
    TextView tvFollowing;
    TextView tvFollowers;
    TextView tvTagline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String screenName = getIntent().getStringExtra("screen_name");
        profile = TwitterApplication.getRestClient().getUserProfile();
        getSupportActionBar().setTitle(profile.getName());
        if (savedInstanceState == null) {
            userTimelineFragment = UserTimelineFragment.newInstance(screenName);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit();
        }
        populateProfileHeader();
    }

    private void populateProfileHeader() {
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvName = (TextView) findViewById(R.id.tvName);
        tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvTagline = (TextView) findViewById(R.id.tvTagline);

        Picasso.with(this)
                .load(profile.getProfileImageUrl())
                .into(ivProfileImage);

        tvScreenName.setText(profile.getScreenName());
        tvName.setText(profile.getName());
        tvFollowing.setText(String.valueOf(profile.getFollowing()) + " Following");
        tvFollowers.setText(String.valueOf(profile.getFollowers()) + " Followers");
        tvTagline.setText(profile.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
}
