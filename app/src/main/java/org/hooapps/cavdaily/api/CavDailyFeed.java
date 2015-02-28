package org.hooapps.cavdaily.api;

import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;

public class CavDailyFeed {

    static CavDailyFeedService cavDailyFeedService;

    public static CavDailyFeedService getService() {
        if (cavDailyFeedService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(CavDailyFeedService.BASE_ENDPOINT)
                    .setConverter(new SimpleXMLConverter())
                    .build();
            cavDailyFeedService = restAdapter.create(CavDailyFeedService.class);
        }
        return cavDailyFeedService;
    }

}
