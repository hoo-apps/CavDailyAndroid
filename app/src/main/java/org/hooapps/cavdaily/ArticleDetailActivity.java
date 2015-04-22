package org.hooapps.cavdaily;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.hooapps.cavdaily.api.model.ArticleItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ArticleDetailActivity extends ActionBarActivity {
    private static final String EXT_JSON_ARTICLE = "ext_json_article";
    private static final String EXT_JSON_NEXT_ARTICLES = "ext_json_next_articles";

    private ShareActionProvider mShareActionProvider;

    private ArticleItem articleItem;
    private List<ArticleItem> nextArticles;
    private List<View> nextArticleViews;

    private TextView titleView;
    private TextView mainDescriptionView;
    private TextView authorView;
    private TextView dateView;
    private ImageView primaryImageView;
    private Toolbar toolbar;

    private Context context;

    public static void startArticleDetailActivity(Context context, ArticleItem articleItem, ArrayList<ArticleItem> nextArticles) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        // Put the article data in the intent
        Gson gson = new Gson();
        intent.putExtra(EXT_JSON_ARTICLE, gson.toJson(articleItem, ArticleItem.class));

        Type listType = new TypeToken<ArrayList<ArticleItem>>(){}.getType();
        intent.putExtra(EXT_JSON_NEXT_ARTICLES, gson.toJson(nextArticles, listType));

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        this.context = this;

        // Retrieve the data from the intent
        Gson gson = new Gson();
        String articleStr = getIntent().getStringExtra(EXT_JSON_ARTICLE);
        articleItem = gson.fromJson(articleStr, ArticleItem.class);

        Type listType = new TypeToken<ArrayList<ArticleItem>>(){}.getType();
        String nextArticleStr = getIntent().getStringExtra(EXT_JSON_NEXT_ARTICLES);
        nextArticles = gson.fromJson(nextArticleStr, listType);

        // Retrieve the views
        authorView = (TextView) findViewById(R.id.author);
        dateView = (TextView) findViewById(R.id.date);
        titleView = (TextView) findViewById(R.id.title);
        mainDescriptionView = (TextView) findViewById(R.id.main_description);
        primaryImageView = (ImageView) findViewById(R.id.primary_image);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Add all views for the next articles to a list
        nextArticleViews = new ArrayList<>();
        nextArticleViews.add(findViewById(R.id.next_article_1));
        nextArticleViews.add(findViewById(R.id.next_article_2));
        nextArticleViews.add(findViewById(R.id.next_article_3));

        configureNextArticleViews();

        // Configure ActionBar
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Format the author and date strings
        authorView.setText(articleItem.getAuthor());
        dateView.setText(articleItem.getDate());

        titleView.setText(articleItem.getTitle());

        // Handle HTML formatting
        // TODO: CONSIDER HANDLING JS GRAPHS
        String mainDescriptionHTML = articleItem.description.replaceAll("<p>", "")
                .replaceAll("</p>", "<br><br>")             // Still include line breaks without <p></p>
                .replaceAll("<html>.*?</html>", "")           // Remove nested HTML with JS
                .replaceAll("<a.*?>", "")                     // Remove links
                .replaceAll("</a>", "");
        mainDescriptionView.setText(Html.fromHtml(mainDescriptionHTML));

        if (articleItem.hasMedia()) {
            List<String> mediaLinks = articleItem.getMediaUrls();
            Picasso.with(this).load(mediaLinks.get(0)).placeholder(R.drawable.article_filler).into(new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    // ImageView has aspect ratio 4:3, so image should match that
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();

                    // Find the desired height of the image
                    int adjustedHeight = (w/4)*3;

                    // Check to see if the rescaling is necessary
                    if (adjustedHeight < h) {
                        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, adjustedHeight);
                        primaryImageView.setImageBitmap(croppedBitmap);
                    } else {
                        primaryImageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {}

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {}
            });

        } else {
            Picasso.with(this).load(R.drawable.article_filler).placeholder(R.drawable.article_filler).into(primaryImageView);
        }

    }

    private void configureNextArticleViews() {
        ImageView image, imageback;
        TextView title, author, date;
        ArticleItem article;

        for (int i = 0; i < nextArticles.size(); i++) {
            article = nextArticles.get(i);

            // Get the views from the include layout
            View rootIncludeView = nextArticleViews.get(i);
            image = (ImageView) rootIncludeView.findViewById(R.id.image);
            imageback = (ImageView) rootIncludeView.findViewById(R.id.imageback);
            title = (TextView) rootIncludeView.findViewById(R.id.title);
            author = (TextView) rootIncludeView.findViewById(R.id.author);
            date = (TextView) rootIncludeView.findViewById(R.id.date);

            // Set the data for the include layout views
            title.setText(article.getTitle());
            date.setText(article.getDate());
            if (article.getAuthor() != null && !article.getAuthor().isEmpty()) {
                author.setText(article.getAuthor());
            }
            if (article.contentList == null) {
                Picasso.with(this).load(R.drawable.article_filler).placeholder(R.drawable.article_filler).into(image);
                Picasso.with(this).load(R.drawable.article_filler).placeholder(R.drawable.article_filler).into(imageback);
            } else {
                Picasso.with(this).load(article.getMediaUrls().get(0)).placeholder(R.drawable.article_filler).resize(64*4, 64*4).centerInside().into(image);
                Picasso.with(this).load(article.getMediaUrls().get(0)).placeholder(R.drawable.article_filler).resize(64*4, 64*4).centerInside().into(imageback);
            }

            // Bind the click listener for the ViewGroup
            final int index = i;
            rootIncludeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<ArticleItem> newNextArticles = new ArrayList<>();
                    for (int j = 0; j < nextArticles.size(); j++) {
                        // Add articles not equal to current one
                        if (j != index) {
                            newNextArticles.add(nextArticles.get(j));
                        }
                    }
                    newNextArticles.add(articleItem);

                    ArticleItem newArticleItem = nextArticles.get(index);

                    // Launch a new ArticleDetailActivity to display the article
                    ArticleDetailActivity.startArticleDetailActivity(context, newArticleItem, newNextArticles);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_detail, menu);

        // Get Share MenuItem and set intent for the ShareActionProvider
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(getShareIntent());

        // Display the menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent getShareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String subject = String.format("CavDaily Article: %s", articleItem.getTitle());
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, articleItem.link);
        shareIntent.setType("text/plain");
        return shareIntent;
    }
}
