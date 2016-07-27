package core.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.LocalDate;

/**
 * Created by Desmin on 6/5/2016.
 */
public class Util {

    public static DateTime timeStringToDateTime(String date) {
        String[] split = date.split("-");
        int year = Integer.valueOf(split[0]);
        int month = Integer.valueOf(split[1]);
        int day = Integer.valueOf(split[2]);
        return new DateTime(year, month, day, 0, 0);
    }

    public static String localDateToString(LocalDate localDate) {
        DateTime dateTime = new DateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0);
        return dateTimeToSQLString(dateTime);
    }

    public static String dateTimeToString(DateTime time) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM-dd-yyyy");
        return fmt.print(time);
    }

    public static String dateTimeToSQLString(DateTime time) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        return fmt.print(time);
    }
}
