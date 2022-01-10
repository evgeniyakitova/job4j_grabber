package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        try {
            for (int i = 1; i < 6; i++) {
                Document doc = Jsoup.connect(link + "/" + i).get();
                Elements row = doc.select(".postslisttopic");
                for (Element td : row) {
                    Element href = td.child(0);
                    String text = href.text();
                    if (text.matches(".*[Jj]ava\\b.*") && !text.matches(".*[Jj]ava[Ss]cript.*")) {
                        posts.add(detail(href.attr("href")));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        Post post = new Post();
        try {
            Document doc = Jsoup.connect(link).get();
            String description = doc.select(".msgBody").get(1).text();
            String created = doc.select(".msgFooter").first().ownText().split("\\s\\[")[0];
            String title = doc.select(".messageHeader").first().ownText();
            post.setLink(link);
            post.setDescription(description);
            post.setCreated(dateTimeParser.parse(created));
            post.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }
}
