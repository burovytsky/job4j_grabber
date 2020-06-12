package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    SqlRuDateConverter converter = new SqlRuDateConverter();

    @Override
    public List<Post> list(String link) {
        ArrayList<Post> result = new ArrayList<>();
        int count = 1;
        while (6 > count) {
            try {
                Document doc = Jsoup.connect(link + "/" + count).get();
                Elements postslisttopic = doc.select(".postslisttopic");
                for (Element element : postslisttopic) {
                    String name = element.child(0).text();
                    if (name.matches("(?i).*java.*") && !name.matches("(?i).*script.*")) {
                        String postLink = element.child(0).attr("href");
                        result.add(detail(postLink));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
        }
        return result;
    }

    @Override
    public Post detail(String link) {
        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String name = doc.selectFirst(".messageHeader").text();
        String desc = doc.select(".msgBody").get(1).text();
        String date = doc.selectFirst(".msgFooter").text().split(" \\[")[0];
        LocalDateTime createdDate = converter.getDate(date);
        return new Post(link, name, desc, createdDate);
    }
}
