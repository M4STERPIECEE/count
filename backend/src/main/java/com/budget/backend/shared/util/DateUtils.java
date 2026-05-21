package com.budget.backend.shared.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public final class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_NAME_FORMATTER = DateTimeFormatter.ofPattern("MMMM", Locale.FRENCH);

    private DateUtils() {}

    public static String format(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }

    public static LocalDate parse(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date invalide : " + dateStr, e);
        }
    }

    public static String getMonthName(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Le mois doit être entre 1 et 12 : " + month);
        }
        return LocalDate.of(2000, month, 1).format(MONTH_NAME_FORMATTER);
    }

    public static boolean isBeforeOrEqual(LocalDate date, LocalDate reference) {
        return date != null && reference != null && !date.isAfter(reference);
    }

    public static boolean isCurrentYear(int year) {
        return year == LocalDate.now().getYear();
    }

    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }
}
