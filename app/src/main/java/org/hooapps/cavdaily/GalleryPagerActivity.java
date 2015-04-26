package org.hooapps.cavdaily;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import org.hooapps.cavdaily.api.model.ArticleItem;


public class GalleryPagerActivity extends FragmentActivity {
    private static final String EXT_JSON_ARTICLE = "ext_json_article";

    private int numPages;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ArticleItem articleItem;

    public static void startGalleryPagerActivity(Context context, ArticleItem articleItem) {
        Intent intent = new Intent(context, GalleryPagerActivity.class);
        // Put the article data in the intent
        Gson gson = new Gson();
        intent.putExtra(EXT_JSON_ARTICLE, gson.toJson(articleItem, ArticleItem.class));

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_pager);

        Gson gson = new Gson();
        String articleStr = getIntent().getStringExtra(EXT_JSON_ARTICLE);
        articleItem = gson.fromJson(articleStr, ArticleItem.class);

        numPages = articleItem.getMediaUrls().size();

        // Must call this AFTER setting numPages
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString(GalleryPageFragment.ARG_MEDIA_URL, articleItem.getMediaUrls().get(position));
            bundle.putInt(GalleryPageFragment.ARG_POSITION, position+1);
            bundle.putInt(GalleryPageFragment.ART_TOTAL_SIZE, articleItem.getMediaUrls().size());
            Fragment frag = new GalleryPageFragment();
            frag.setArguments(bundle);
            return frag;
        }

        @Override
        public int getCount() {
            return numPages;
        }
    }
}
