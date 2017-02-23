package util;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.Calendar.*;

public class DateUtil {
    private static Calendar calendar = GregorianCalendar.getInstance();
    private static Calendar calendar2 = GregorianCalendar.getInstance();

    private static final Clock DEFAULT_CLOCK = Clock.systemDefaultZone();
    private static Clock clock = DEFAULT_CLOCK;

    public static void fixClockAt(Date date) {
        clock = Clock.fixed(date.toInstant(), ZoneId.systemDefault());
    }

    public static void resetClock() {
        clock = DEFAULT_CLOCK;
    }

    public static Date getCurrentDate() {
        return toDate(getCurrentLocalDate());
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now(clock);
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date create(int year, int month, int dayOfMonth) {
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        calendar.set(MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date addDays(Date date, int days) {
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    public static Date tomorrow() {
        return addDays(new Date(), 1);
    }

    // this stinks
    public static int daysFrom(Date start, Date stop) {
        calendar.setTime(start);
        Calendar stopCalendar = GregorianCalendar.getInstance();
        stopCalendar.setTime(stop);

        int startDays = calendar.get(DAY_OF_YEAR);
        int startYear = calendar.get(YEAR);
        int stopYear = stopCalendar.get(YEAR);
        int stopDays = stopCalendar.get(DAY_OF_YEAR);

        if (startYear == stopYear) return stopDays - startDays;

        int days = calendar.getActualMaximum(DAY_OF_YEAR) - startDays;
        for (int i = startYear + 1; i < stopYear; i++) {
            stopCalendar.set(YEAR, i);
            days += stopCalendar.getActualMaximum(DAY_OF_YEAR);
        }
        days += stopDays;
        return days;
    }

    public static int daysAfter(Date first, Date second) {
        if (!second.after(first))
            return 0;

        calendar.setTime(first);
        int dayOfYear1 = calendar.get(DAY_OF_YEAR);

        calendar2.setTime(second);
        int dayOfYear2 = calendar2.get(DAY_OF_YEAR);

        for (int year = calendar.get(YEAR); year < calendar2.get(YEAR); year++) {
            calendar.set(YEAR, year);
            dayOfYear2 += calendar.getActualMaximum(DAY_OF_YEAR);
        }
        return dayOfYear2 - dayOfYear1;
    }

    public static int ageInYears(LocalDate earlierDate, LocalDate laterDate) {
        return Period.between(earlierDate, laterDate).getYears();
    }
}
