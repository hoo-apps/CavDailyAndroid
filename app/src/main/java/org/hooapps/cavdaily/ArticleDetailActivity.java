package org.hooapps.cavdaily;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.hooapps.cavdaily.api.model.ArticleItem;

import java.util.ArrayList;
import java.util.List;


public class ArticleDetailActivity extends ActionBarActivity {

    private static final String EXT_TITLE = "ext_title";
    private static final String EXT_DESC = "ext_description";
    private static final String EXT_DATE = "ext_date";
    private static final String EXT_MEDIA = "ext_media";

    private TextView titleView;
    private TextView mainDescriptionView;
    private ImageView primaryImageView;
    private WebView webView;
    private Toolbar toolbar;

    public static void startArticleDetailActivity(Context context, ArticleItem articleItem) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        // Put the article data in the intent
        intent.putExtra(EXT_TITLE, articleItem.title);
        intent.putExtra(EXT_DESC, articleItem.description);
        intent.putExtra(EXT_DATE, articleItem.pubDate);
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
        String pubDate = getIntent().getStringExtra(EXT_DATE);
        String mainDescriptionHTML = getIntent().getStringExtra(EXT_DESC);
        ArrayList<String> mediaLinks = getIntent().getStringArrayListExtra(EXT_MEDIA);

        // Retrieve the views
        titleView = (TextView) findViewById(R.id.title);
        mainDescriptionView = (TextView) findViewById(R.id.main_description);
        primaryImageView = (ImageView) findViewById(R.id.primary_image);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);


        // Actionbar
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
            setSupportActionBar(toolbar);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /*
        webView = (WebView) findViewById(R.id.web_test);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        String test = "<html><head><script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script><script type=\"text/javascript\">\n" +
                "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});\n" +
                "      google.setOnLoadCallback(drawChart);\n" +
                "      function drawChart() {\n" +
                "        var data = google.visualization.arrayToDataTable([\n" +
                "          ['Task', 'Hours per Day'],\n" +
                "          ['Asian',      8],\n" +
                "          ['Hispanic', 4],\n" +
                "          ['Native American',    1],\n" +
                "          ['Not specified',    2],\n" +
                "          ['White',    37]\n" +
                "        ]);\n" +
                "\n" +
                "        var options = {\n" +
                "          title: 'Profile of 2015-2016 Lawn residents by ethnicity/race',\n" +
                "          pieHole: 0.4,\n" +
                "        };\n" +
                "\n" +
                "        var chart = new google.visualization.PieChart(document.getElementById('donutchart'));\n" +
                "        chart.draw(data, options);\n" +
                "      }\n" +
                "    </script></head><body><div id=\"donutchart\" style=\"width: 600px; height: 500px;\"></div></body></html>";
        webView.loadData(test, "text/html", null);
        */

        titleView.setText(title);
        mainDescriptionView.setText(Html.fromHtml(mainDescriptionHTML));
        if (mediaLinks != null && mediaLinks.size() != 0) {
            Picasso.with(this).load(mediaLinks.get(0)).into(primaryImageView);
        }

    }
}
