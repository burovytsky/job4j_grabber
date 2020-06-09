package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SqlRuParse implements Parse{
    public static void main(String[] args) {
        List<Post> posts= new SqlRuParse().list("https://www.sql.ru/forum/job-offers");
    }

    private static LocalDateTime getDate(String stringTime) {
        Map<String, Integer> months = Map.ofEntries(Map.entry("янв", 1), Map.entry("фев", 2),
                Map.entry("мар", 3), Map.entry("апр", 4), Map.entry("май", 5), Map.entry("июн", 6),
                Map.entry("июл", 7), Map.entry("авг", 8), Map.entry("сен", 9), Map.entry("окт", 10),
                Map.entry("ноя", 11), Map.entry("дек", 12));
        int year;
        int month;
        int day;
        if (stringTime.matches("^\\d+.+")) {
            year = Integer.parseInt("20" + stringTime.split(",")[0].split(" ")[2]);
            month = months.get(stringTime.split(" ")[1]);
            day = Integer.parseInt(stringTime.split(" ")[0]);
        } else {
            year = LocalDateTime.now().getYear();
            month = LocalDateTime.now().getMonth().getValue();
            day = stringTime.split(" ")[0].equals("сегодня") ? LocalDateTime.now().getDayOfMonth() : LocalDateTime.now().getDayOfMonth() - 1;
        }
        int[] time = {Integer.parseInt(stringTime.split(",")[1].trim().split(":")[0]),
                Integer.parseInt(stringTime.split(",")[1].trim().split(":")[1])};
        int hours = time[0];
        int minutes = time[1];
        return LocalDateTime.of(year, month, day, hours, minutes);
    }

    @Override
    public List<Post> list(String link) {
        ArrayList<Post> result = new ArrayList<>();
        int count = 0;
        while (5 > count) {
            try {
                Document doc = Jsoup.connect(link + "/" + count).get();
                Elements postslisttopic = doc.select(".postslisttopic");
                for (Element element: postslisttopic){
                    String postLink =  element.child(0).attr("href");
                    result.add(detail(postLink));
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
        String name = doc.select(".messageHeader").text();
        String desc = doc.select(".msgBody").get(1).text();
        String date = doc.selectFirst(".msgFooter").text().split(" \\[")[0];
        LocalDateTime createdDate = getDate(date);
        return new Post(link, name, desc, createdDate);
    }
}
