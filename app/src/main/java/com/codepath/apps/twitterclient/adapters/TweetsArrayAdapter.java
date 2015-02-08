package com.codepath.apps.twitterclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yeyus on 2/7/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder {
        ImageView profileImage;
        TextView username;
        TextView body;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Tweet tweet = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet_item, parent, false);
            viewHolder.profileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.body = (TextView) convertView.findViewById(R.id.tvBody);
            convertView.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(tweet.getUser().getScreenName());
        viewHolder.body.setText(tweet.getBody());

        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .into(viewHolder.profileImage);

        return convertView;
    }
}
