package org.hooapps.cavdaily.api;

import org.hooapps.cavdaily.api.model.ArticleFeedResponse;

import retrofit.http.GET;

public interface CavDailyFeedService {

    public static final String BASE_ENDPOINT = "http://www.cavalierdaily.com/dart/feed";

    @GET("/news-full.xml")
    ArticleFeedResponse getNewsFeed();
}