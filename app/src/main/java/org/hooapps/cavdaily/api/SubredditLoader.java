package org.hooapps.cavdaily.api;

import android.content.Context;
import android.util.Log;

import org.hooapps.cavdaily.api.model.ArticleFeedResponse;
import org.hooapps.cavdaily.api.model.RedditData;

import oak.util.OakAsyncLoader;

/**
 * Created by ericrichardson on 2/18/15.
 */
public class SubredditLoader extends OakAsyncLoader<RedditData>{
    public SubredditLoader(Context context) {
        super(context);
    }

    @Override
    public RedditData loadInBackground() {
        ArticleFeedResponse response = CavDailyFeed.getService().getNewsFeed();
        Log.d("TAG", "=====");
        Log.d("TAG", "" + response.getTitle());
        Log.d("TAG", "" + response.getArticleList().get(0).title);

        RedditData data = Reddit.getService().getSubreddit("android");
        return data;
    }
}
