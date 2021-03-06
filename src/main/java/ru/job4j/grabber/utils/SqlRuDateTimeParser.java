package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MM yy");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MM yy, HH:mm");

    private static final Map<String, String> MONTHS = Map.ofEntries(
            Map.entry("янв", "01"),
            Map.entry("фев", "02"),
            Map.entry("мар", "03"),
            Map.entry("апр", "04"),
            Map.entry("май", "05"),
            Map.entry("июн", "06"),
            Map.entry("июл", "07"),
            Map.entry("авг", "08"),
            Map.entry("сен", "09"),
            Map.entry("окт", "10"),
            Map.entry("ноя", "11"),
            Map.entry("дек", "12"));

    @Override
    public LocalDateTime parse(String parse) {
        if (parse.contains("сегодня")) {
            LocalDate date = LocalDate.now();
            parse = parse.replace("сегодня", date.format(dateFormatter));
        } else if (parse.contains("вчера")) {
            LocalDate date = LocalDate.now().minusDays(1);
            parse = parse.replace("вчера", date.format(dateFormatter));
        } else {
        String month = parse.split(" ")[1];
        parse = parse.replace(month, MONTHS.get(month));
        }
        return LocalDateTime.parse(parse, dateTimeFormatter);
    }
}
