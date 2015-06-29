/**
 * FILE: HomeTimelineAdapter.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ubimobitech.ubitwitter.R;
import com.ubimobitech.ubitwitter.model.TimelineTweet;
import com.ubimobitech.ubitwitter.utils.Utils;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by benakiva on 27/06/15.
 */
public class HomeTimelineAdapter extends BaseAdapter {
    private Context mContext;
    private List<TimelineTweet> mList;

    public HomeTimelineAdapter(Context context, List<TimelineTweet> list) {
        mContext = context;
        mList = list;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_timeline_item, parent, false);

            holder = new ViewHolder();

            holder.profileImg = (ImageView) convertView.findViewById(R.id.profile_img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.screenName = (TextView) convertView.findViewById(R.id.screen_name);
            holder.tweet = (TextView) convertView.findViewById(R.id.tweet);
            holder.tweetAge = (TextView) convertView.findViewById(R.id.tweet_age);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        TimelineTweet tweet = (TimelineTweet)getItem(position);

        holder.name.setText(tweet.getUser().getName());
        holder.screenName.setText("@" + tweet.getUser().getScreenName());
        holder.tweet.setText(tweet.getText());

        Float dimen = mContext.getResources().getDimension(R.dimen.profile_img_size);

        Picasso.with(mContext).load(tweet.getUser().getProfileImageUrl())
                .resize(dimen.intValue(), dimen.intValue())
                .into(holder.profileImg);

        holder.tweetAge.setText(Utils.calculateEllapsedTime(mContext, tweet.getCreatedAt()));

        return convertView;
    }

    static class ViewHolder {
        ImageView profileImg;
        TextView name;
        TextView screenName;
        TextView tweetAge;
        TextView tweet;
    }
}
