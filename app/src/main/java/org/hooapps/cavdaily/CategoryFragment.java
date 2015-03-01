package org.hooapps.cavdaily;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.hooapps.cavdaily.api.CavDailyFeed;
import org.hooapps.cavdaily.api.CavDailyFeedLoader;
import org.hooapps.cavdaily.api.model.ArticleFeedResponse;
import org.hooapps.cavdaily.api.model.ArticleItem;
import org.hooapps.cavdaily.api.model.RedditData;

import java.util.List;

public class CategoryFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArticleFeedResponse> {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        getListView().setDrawSelectorOnTop(true);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    }

    @Override
    public Loader<ArticleFeedResponse> onCreateLoader(int id, Bundle args) {
        return new CavDailyFeedLoader(getActivity());
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
                convertView = LayoutInflater.from(context).inflate(R.layout.view_list_item_text, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            ArticleItem article = getItem(position);
            holder.title.setText(article.title);
            holder.author.setText(article.author);
            return convertView;
        }

        private static class ViewHolder {
            ImageView image;
            TextView title, author;

            public ViewHolder(View v) {
                image = (ImageView) v.findViewById(R.id.image);
                title = (TextView) v.findViewById(R.id.title);
                author = (TextView) v.findViewById(R.id.author);
            }
        }
    }
}
