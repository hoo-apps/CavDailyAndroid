package org.hooapps.cavdaily;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hooapps.cavdaily.api.CavDailyFeedLoader;
import org.hooapps.cavdaily.api.model.ArticleFeedResponse;
import org.hooapps.cavdaily.api.model.ArticleItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchFragment extends ListFragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configure the ListView and load the data
        getListView().setDrawSelectorOnTop(true);
        beginSearch("cavaliers");
    }

    public void beginSearch(String searchText) {
        new DownloadFilesTask().execute(searchText);
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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

    private static class CavDailySearchAdapter extends BaseAdapter {
        Context context;
        List<ArticleItem> articleList;

        private CavDailySearchAdapter(Context context, List<ArticleItem> articleList) {
            this.context = context;
            this.articleList = articleList;
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
                image = (ImageView) v.findViewById(R.id.image);
                imageback = (ImageView) v.findViewById(R.id.imageback);
                title = (TextView) v.findViewById(R.id.title);
                author = (TextView) v.findViewById(R.id.author);
                date = (TextView) v.findViewById(R.id.date);

            }
        }
    }

    class DownloadFilesTask extends AsyncTask<String, Integer, List<ArticleItem>> {

        protected List<ArticleItem> doInBackground(String ...searchText) {
            int count = searchText.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
                String searchURL = "http://www.cavalierdaily.com/search?q=" + searchText[i];
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(searchURL);
                HttpResponse response;
                try {
                    response = httpclient.execute(httpget);
                    // Get hold of the response entity
                    HttpEntity entity = response.getEntity();
                    // If the response does not enclose an entity, there is no need
                    // to worry about connection release
                    if (entity != null) {
                        // A Simple JSON Response Read
                        InputStream instream = entity.getContent();
                        String result = convertStreamToString(instream);
                        // now you have the string representation of the HTML request
                        instream.close();

                        List<ArticleItem> articles = new ArrayList<ArticleItem>();
                        Matcher articleMatcher = Pattern.compile("<article(.*?)>(.*?)</article>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  | Pattern.DOTALL | Pattern.COMMENTS).matcher(result);
                        while (articleMatcher.find()) {
                            ArticleItem article = new ArticleItem();
                            String full =  articleMatcher.group(2);

                            Matcher titleMatcher = Pattern.compile("<h[0-9]><(.*)>(.*)</a>", Pattern.CASE_INSENSITIVE).matcher(full);
                            if (titleMatcher.find())
                                article.title = titleMatcher.group(2);

                            Matcher thumbnailMatcher = Pattern.compile("<img src=\"(.*?)\"", Pattern.CASE_INSENSITIVE).matcher(full);
                            if (thumbnailMatcher.find())
                                article.addMediaUrl(thumbnailMatcher.group(1));

                            Matcher linkMatcher = Pattern.compile("<h[0-9]><a href=\"(.*?)\"", Pattern.CASE_INSENSITIVE).matcher(full);
                            if (linkMatcher.find())
                                article.link = linkMatcher.group(1);

                            Matcher captionMatcher = Pattern.compile("<p>(.*)</p>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  | Pattern.DOTALL | Pattern.COMMENTS).matcher(full);
                            if (captionMatcher.find())
                                article.description = captionMatcher.group(1);

                            articles.add(article);
                        }
                        return articles;
                    }
                } catch (Exception e) {
                }
            }
            return new ArrayList<ArticleItem>();
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(List<ArticleItem> result) {
            setListAdapter(new CavDailySearchAdapter(getActivity(), result));
        }
    }
}

