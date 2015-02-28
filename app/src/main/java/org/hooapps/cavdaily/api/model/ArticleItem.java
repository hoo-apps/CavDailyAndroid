package org.hooapps.cavdaily.api.model;

import org.simpleframework.xml.Element;
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

    // Ignore category for now
}
