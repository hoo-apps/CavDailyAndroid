package org.hooapps.cavdaily.api;

import android.net.Uri;

import org.hooapps.cavdaily.api.model.ArticleFeedResponse;

import retrofit.http.GET;
import retrofit.http.Path;

public interface CavDailyFeedService {

    public static final String BASE_ENDPOINT = "http://www.cavalierdaily.com/dart/feed";
    public static final String ALT_ENDPOINT = "http://www.cavalierdaily.com/servlet/feed";

    // Feed Categories
    public static final String TOP = "top-stories-full";
    public static final String NEWS = "news-full";
    public static final String FOCUS = "focus-full";
    public static final String SPORTS = "sports-full";
    public static final String OPINION = "opinion-full";
    public static final String LIFE = "life-full";
    public static final String AE = "ae-full";
    public static final String HS = "health-science";
    public static final String MULTIMEDIA = "multimedia-full";

    @GET("/{category}.xml")
    ArticleFeedResponse getCavDailyFeed(@Path("category") String type);
}