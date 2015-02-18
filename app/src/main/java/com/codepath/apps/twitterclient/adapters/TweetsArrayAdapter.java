package com.codepath.apps.twitterclient.adapters;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yeyus on 2/7/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public interface TweetReplyActionListener {
        public void OnReplyAction(Tweet tweet);
    }

    public interface ProfileOnClickListener {
        public void OnProfileClick(User user);
    }

    private TweetReplyActionListener replyListener;
    private ProfileOnClickListener profileListener;

    private static class ViewHolder {
        ImageView profileImage;
        TextView username;
        TextView body;
        TextView relativeTime;
        TextView retweetCount;
        TextView favCount;
        TextView handle;
        ImageButton reply;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    public void setReplyListener(TweetReplyActionListener listener) {
        this.replyListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Tweet tweet = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet_item, parent, false);
            viewHolder.profileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.body = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.relativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
            viewHolder.retweetCount = (TextView) convertView.findViewById(R.id.tvRetweetsCount);
            viewHolder.favCount = (TextView) convertView.findViewById(R.id.tvFavCount);
            viewHolder.handle = (TextView) convertView.findViewById(R.id.tvHandle);
            viewHolder.reply = (ImageButton) convertView.findViewById(R.id.btnReply);
            convertView.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(tweet.getUser().getName());
        viewHolder.handle.setText("@"+tweet.getUser().getScreenName());

        viewHolder.body.setText(tweet.getBody());
        viewHolder.body.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.relativeTime.setText(tweet.getRelativeCreatedAt());

        // Call Reply replyListener if replyListener is set
        viewHolder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TweetsArrayAdapter.this.replyListener != null) {
                    replyListener.OnReplyAction(tweet);
                }
            }
        });

        viewHolder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TweetsArrayAdapter.this.profileListener != null) {
                    profileListener.OnProfileClick(tweet.getUser());
                }
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

        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .into(viewHolder.profileImage);

        return convertView;
    }
}
