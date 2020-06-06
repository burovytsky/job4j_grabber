package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements postslisttopic = doc.select(".postslisttopic");
        Elements dates = doc.select(".altCol").select("[style]");
        for (int i = 0; i < postslisttopic.size(); i++) {
            Element href = postslisttopic.get(i).child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            System.out.println(dates.get(i).text());
        }
    }
}
