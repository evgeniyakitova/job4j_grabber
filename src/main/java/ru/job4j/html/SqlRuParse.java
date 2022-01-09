package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        for (int i = 1; i < 6; i++) {
            String url = String.format("https://www.sql.ru/forum/job-offers/%s", i);
            Document doc = Jsoup.connect(url).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                getDetails(href.attr("href"));
            }
        }
    }

    private static void getDetails(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Elements headers = doc.select(".messageHeader");
        System.out.println(headers.first().ownText());
        Elements bodies = doc.select(".msgBody");
        System.out.println(bodies.get(1).text());
        Elements footers = doc.select(".msgFooter");
        System.out.println(footers.first().ownText().split("\\s\\[")[0]);
    }
}
