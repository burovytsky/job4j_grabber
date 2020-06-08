package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.Map;


public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements postslisttopic = doc.select(".postslisttopic");
        Elements dates = doc.select(".altCol").select("[style]");
        for (int i = 0; i < postslisttopic.size(); i++) {
            Element href = postslisttopic.get(i).child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            String stringTime = dates.get(i).text();
            System.out.println(getDate(stringTime));
        }
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
}
