package org.hooapps.cavdaily.api;

import android.content.Context;

import org.hooapps.cavdaily.api.model.ArticleFeedResponse;

import oak.util.OakAsyncLoader;

public class CavDailyFeedLoader extends OakAsyncLoader<ArticleFeedResponse>{

    private String category;

    public CavDailyFeedLoader(Context context, String category) {
        super(context);
        this.category = category;
    }

    @Override
    public ArticleFeedResponse loadInBackground() {
        CavDailyFeedService service = CavDailyFeed.getService();

        // Use a different url scheme for the top stories feed
        ArticleFeedResponse response;
        if (category.equals(CavDailyFeedService.TOP)) {
            response = service.getCavDailyTopStoriesFeed();
        } else {
            response = service.getCavDailyFeed(category);
        }
        return response;
    }
}
