package org.hooapps.cavdaily;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.hooapps.cavdaily.api.model.ArticleItem;

import java.util.ArrayList;


public class ArticleDetailActivity extends ActionBarActivity {

    private static final String EXT_TITLE = "ext_title";
    private static final String EXT_DESC = "ext_description";
    private static final String EXT_DATE = "ext_date";
    private static final String EXT_AUTHOR = "ext_author";
    private static final String EXT_MEDIA = "ext_media";

    private TextView titleView;
    private TextView mainDescriptionView;
    private TextView authorView;
    private TextView dateView;
    private ImageView primaryImageView;
    private Toolbar toolbar;

    public static void startArticleDetailActivity(Context context, ArticleItem articleItem) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        // Put the article data in the intent
        intent.putExtra(EXT_TITLE, articleItem.getTitle());
        intent.putExtra(EXT_DESC, articleItem.description);
        intent.putExtra(EXT_AUTHOR, articleItem.getAuthor());
        intent.putExtra(EXT_DATE, articleItem.getDate());
        if (articleItem.hasMedia()) {
            ArrayList<String> mediaLinks = (ArrayList<String>) articleItem.getMediaUrls();
            intent.putStringArrayListExtra(EXT_MEDIA, mediaLinks);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // Retrieve the data from the intent
        String title = getIntent().getStringExtra(EXT_TITLE);
        String author = getIntent().getStringExtra(EXT_AUTHOR);
        String pubDate = getIntent().getStringExtra(EXT_DATE);
        String mainDescriptionHTML = getIntent().getStringExtra(EXT_DESC);
        ArrayList<String> mediaLinks = getIntent().getStringArrayListExtra(EXT_MEDIA);

        // Retrieve the views
        authorView = (TextView) findViewById(R.id.author);
        dateView = (TextView) findViewById(R.id.date);
        titleView = (TextView) findViewById(R.id.title);
        mainDescriptionView = (TextView) findViewById(R.id.main_description);
        primaryImageView = (ImageView) findViewById(R.id.primary_image);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);


        // Configure ActionBar
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Format the author and date strings
        authorView.setText(author);
        dateView.setText(pubDate);

        titleView.setText(Html.fromHtml(title));

        // Handle HTML formatting
        // TODO: CONSIDER HANDLING JS GRAPHS
        mainDescriptionHTML = mainDescriptionHTML.replaceAll("<p>", "")
                .replaceAll("</p>", "<br><br>")             // Still include line breaks without <p></p>
                .replaceAll("<html>.*?</html>", "")           // Remove nested HTML with JS
                .replaceAll("<a.*?>", "")                     // Remove links
                .replaceAll("</a>", "");
        mainDescriptionView.setText(Html.fromHtml(mainDescriptionHTML));

        if (mediaLinks != null && mediaLinks.size() != 0) {
            //Picasso.with(this).load(mediaLinks.get(0)).placeholder(R.drawable.article_filler).into(primaryImageView);
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
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

        } else {
            Picasso.with(this).load(R.drawable.article_filler).placeholder(R.drawable.article_filler).into(primaryImageView);
        }

    }
}
