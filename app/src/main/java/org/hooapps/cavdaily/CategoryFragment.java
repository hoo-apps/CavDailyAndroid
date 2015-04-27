package org.hooapps.cavdaily;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openx.view.AdBanner;
import com.squareup.picasso.Picasso;

import org.hooapps.cavdaily.api.CavDailyFeed;
import org.hooapps.cavdaily.api.CavDailyFeedLoader;
import org.hooapps.cavdaily.api.CavDailyFeedService;
import org.hooapps.cavdaily.api.model.ArticleFeedResponse;
import org.hooapps.cavdaily.api.model.ArticleItem;
import org.hooapps.cavdaily.api.model.RedditData;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.List;

import retrofit.converter.SimpleXMLConverter;

public class CategoryFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArticleFeedResponse> {

    public static final String ARG_CATEGORY = "arg_category";
    private String category;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the category from the arguments
        this.category = getArguments().getString(ARG_CATEGORY);

        // Configure the ListView and load the data
        getListView().setDivider(null);
        getListView().setDrawSelectorOnTop(true);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ListAdapter adapter = getListAdapter();

        ArticleItem articleItem = (ArticleItem) adapter.getItem(position);

        ArrayList<ArticleItem> nextArticles = new ArrayList<>();
        int numArticles = adapter.getCount();
        for (int i = 0; i < 3; i++) {
            nextArticles.add((ArticleItem) adapter.getItem((position+1+i) % numArticles));
        }

        ArticleDetailActivity.startArticleDetailActivity(getActivity(), articleItem, nextArticles);
    }

    @Override
    public Loader<ArticleFeedResponse> onCreateLoader(int id, Bundle args) {
        return new CavDailyFeedLoader(getActivity(), this.category);
    }

    @Override
    public void onLoadFinished(Loader<ArticleFeedResponse> loader, ArticleFeedResponse data) {
        setListAdapter(new CavDailyFeedAdapter(getActivity(), data));
    }

    @Override
    public void onLoaderReset(Loader<ArticleFeedResponse> loader) {

    }

    private static class CavDailyFeedAdapter extends BaseAdapter {
        Context context;
        List<ArticleItem> articleList;

        private CavDailyFeedAdapter(Context context, ArticleFeedResponse data) {
            this.context = context;
            this.articleList = data.getArticleList();
        }

        @Override
        public int getCount() {
            return articleList.size();
        }

        @Override
        public ArticleItem getItem(int position) {
            return articleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.view_list_item_image, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            ArticleItem article = getItem(position);
            holder.title.setText(article.getTitle());
            holder.date.setText(article.getDate());
            if (article.getAuthor() != null && !article.getAuthor().isEmpty()) {
                holder.author.setText(article.getAuthor());
            }
            if (article.contentList == null) {
                Picasso.with(context).load(R.drawable.article_filler).placeholder(R.drawable.article_filler).into(holder.image);
                Picasso.with(context).load(R.drawable.article_filler).placeholder(R.drawable.article_filler).into(holder.imageback);
            } else {
                Picasso.with(context).load(article.getMediaUrls().get(0)).placeholder(R.drawable.article_filler).resize(64*4, 64*4).centerInside().into(holder.image);
                Picasso.with(context).load(article.getMediaUrls().get(0)).placeholder(R.drawable.article_filler).resize(64*4, 64*4).centerInside().into(holder.imageback);
            }

            return convertView;
        }

        private static class ViewHolder {
            ImageView image, imageback;
            TextView title, author, date;

            public ViewHolder(View v) {
                View rootIncludeView = v.findViewById(R.id.interior_view);
                image = (ImageView) rootIncludeView.findViewById(R.id.image);
                imageback = (ImageView) rootIncludeView.findViewById(R.id.imageback);
                title = (TextView) rootIncludeView.findViewById(R.id.title);
                author = (TextView) rootIncludeView.findViewById(R.id.author);
                date = (TextView) rootIncludeView.findViewById(R.id.date);

            }
        }
    }
}
