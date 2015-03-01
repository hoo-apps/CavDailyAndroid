package org.hooapps.cavdaily.api;

import android.content.Context;

import org.hooapps.cavdaily.api.model.ArticleFeedResponse;

import oak.util.OakAsyncLoader;

/**
 * Created by ericrichardson on 2/18/15.
 */
public class CavDailyFeedLoader extends OakAsyncLoader<ArticleFeedResponse>{
    public CavDailyFeedLoader(Context context) {
        super(context);
    }

    @Override
    public ArticleFeedResponse loadInBackground() {
        ArticleFeedResponse response = CavDailyFeed.getService().getNewsFeed();
        return response;
    }
}
