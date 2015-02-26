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
import org.hooapps.cavdaily.api.SubredditLoader;
import org.hooapps.cavdaily.api.model.RedditData;

/**
 * Created by ericrichardson on 2/18/15.
 */
public class CategoryFragment extends ListFragment implements LoaderManager.LoaderCallbacks<RedditData> {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        getListView().setDrawSelectorOnTop(true);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        int viewType = getListAdapter().getItemViewType(position);
        RedditData data = (RedditData) getListAdapter().getItem(position);
        if (viewType == RedditAdapter.SELF) {
            SelfActivity.startSelfActivity(getActivity(), data);
        } else if (viewType == RedditAdapter.WEB) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.url)));
        } else {
            ImageActivity.startImageActivity(getActivity(), data.thumbnail);
        }
    }

    @Override
    public Loader<RedditData> onCreateLoader(int id, Bundle args) {
        return new SubredditLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<RedditData> loader, RedditData data) {
        setListAdapter(new RedditAdapter(getActivity(), data));
    }

    @Override
    public void onLoaderReset(Loader<RedditData> loader) {

    }

    private static class RedditAdapter extends BaseAdapter {
        public static final int SELF = 0, IMAGE = 1, WEB = 2;
        Context context;
        RedditData data;

        private RedditAdapter(Context context, RedditData data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.data.children.size();
        }

        @Override
        public RedditData getItem(int position) {
            return data.data.children.get(position).data;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            RedditData data = getItem(position);
            return data.isSelf ? SELF : data.url.endsWith(".jpg") || data.url.endsWith(".png") ? IMAGE : WEB;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                switch (getItemViewType(position)) {
                    case SELF:
                        convertView = LayoutInflater.from(context).inflate(R.layout.view_list_item_text, parent, false);
                        break;
                    default:
                        convertView = LayoutInflater.from(context).inflate(R.layout.view_list_item_image, parent, false);
                        break;
                }
                convertView.setTag(new ViewHolder(convertView));
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            RedditData data = getItem(position);
            holder.title.setText(data.title);
            holder.author.setText(data.author);
            if (!data.isSelf) {
                Picasso.with(context).load(data.thumbnail).into(holder.image);
            }
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
