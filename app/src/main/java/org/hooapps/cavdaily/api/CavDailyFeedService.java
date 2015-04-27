package org.hooapps.cavdaily.api;

import org.hooapps.cavdaily.api.model.ArticleFeedResponse;

import retrofit.http.GET;
import retrofit.http.Path;

public interface CavDailyFeedService {

    String BASE_ENDPOINT = "http://www.cavalierdaily.com/dart/feed";
    String ALT_ENDPOINT = "http://www.cavalierdaily.com/servlet/feed";

    // Feed Categories
    String TOP = "top-stories-full";
    String NEWS = "news-full";
    String FOCUS = "focus-full";
    String SPORTS = "sports-full";
    String OPINION = "opinion-full";
    String LIFE = "life-full";
    String AE = "ae-full";
    String HS = "health-science-full";
    String MULTIMEDIA = "multimedia-full";

    @GET("/{category}.xml")
    ArticleFeedResponse getCavDailyFeed(@Path("category") String type);
}