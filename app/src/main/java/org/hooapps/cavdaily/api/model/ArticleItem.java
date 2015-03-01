package org.hooapps.cavdaily.api.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

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
    public String pubDate;

    @Element
    public String author;

    @Element(name = "content", required = false)
    @Namespace(prefix = "media")
    public MediaContent mediaContent;

    public String getMediaUrl() {
        return mediaContent.url;
    }
}

@Root(name = "media", strict = false)
class MediaContent {

    @Attribute
    String url;
}