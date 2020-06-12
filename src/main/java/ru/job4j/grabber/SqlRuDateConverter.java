package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SqlRuDateConverter{

    private final Map<String, Integer> months = new HashMap<>();

    public SqlRuDateConverter() {
        months.put("янв", 1);
        months.put("фев", 2);
        months.put("мар", 3);
        months.put("апр", 4);
        months.put("май", 5);
        months.put("июн", 6);
        months.put("июл", 7);
        months.put("авг", 8);
        months.put("сен", 9);
        months.put("окт", 10);
        months.put("ноя", 11);
        months.put("дек", 12);
    }


    public LocalDateTime getDate(String stringTime) {

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
