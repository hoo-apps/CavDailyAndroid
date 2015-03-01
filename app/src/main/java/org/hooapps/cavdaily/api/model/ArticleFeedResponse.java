package org.hooapps.cavdaily.api.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "rss", strict = false)
public class ArticleFeedResponse {

    @Element(name = "channel")
    public Channel channel;

    public String getTitle() {
        return channel.title;
    }

    public List<ArticleItem> getArticleList() {
        return channel.articles;
    }
}

@Root(name = "channel", strict = false)
class Channel {

    @Element
    public String title;

    @ElementList(entry = "item", inline = true)
    public List<ArticleItem> articles;

}