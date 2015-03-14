package org.hooapps.cavdaily.api.model;

import android.util.Log;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "item", strict = false)
public class ArticleItem {

    @Element
    public String title;

    @Element
    public String description;

    @Element
    public String link;

    @Element
    public String guid;

    @Element
    private String pubDate;

    public String getDate() {
        return pubDate.substring(0, pubDate.indexOf(":") + 3);
    }

    @Element(required = false)
    private String author;

    public String getAuthor() {
        return author.split("[\\(\\)]")[1];
    }

    @ElementList(entry = "content", required = false, inline = true)
    @Namespace(prefix = "media")
    public List<Content> contentList;

    public boolean hasMedia() {
        return (contentList != null);
    }

    public List<String> getMediaUrls() {
        // Transfer the urls to List of Strings
        List<String> urlList = new ArrayList<>();
        for (Content c : contentList) {
            urlList.add(c.url);
        }
        return urlList;
    }
}

@Root(name = "content", strict = false)
@Namespace(prefix = "media")
class Content {
    @Attribute(name = "url", required = false)
    public String url;
}