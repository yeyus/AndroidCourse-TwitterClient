package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends ActionBarActivity {

    private static final int REQUEST_COMPOSE = 1337;

    private User profile;
    private Tweet replyTo;

    private ImageView ivProfilePic;
    private TextView tvScreenName;
    private EditText etBody;
    private MenuItem txtCharCount;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        profile = (User) getIntent().getSerializableExtra("profile");
        replyTo = (Tweet) getIntent().getSerializableExtra("reply");

        setupView();
        getSupportActionBar().setHomeButtonEnabled(true);

        if (profile != null) {
            Picasso.with(this).load(profile.getProfileImageUrl()).into(ivProfilePic);
            tvScreenName.setText("@" + profile.getScreenName());
        }

        if (replyTo != null) {
            etBody.setText("@" + replyTo.getUser().getScreenName() + " ");
        }

        etBody.requestFocus();
    }

    private void setupView() {
        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        etBody = (EditText) findViewById(R.id.etBody);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
    }

    private void setupListeners() {
        etBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(txtCharCount != null) {
                    txtCharCount.setTitle(Integer.toString(150 - s.length()));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        txtCharCount = menu.findItem(R.id.txtCharsLeft);
        setupListeners();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            client = TwitterApplication.getRestClient();
            client.postTweet(etBody.getText().toString(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Intent i = new Intent();
                    i.putExtra("tweet", Tweet.fromJSON(response));
                    ComposeActivity.this.setResult(REQUEST_COMPOSE, i);
                    ComposeActivity.this.finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(ComposeActivity.this, "Failed sending tweet", Toast.LENGTH_LONG).show();
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}
