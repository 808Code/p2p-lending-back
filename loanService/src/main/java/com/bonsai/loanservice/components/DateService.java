package com.bonsai.loanservice.components;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-05-25
 */
@Component
public class DateService {

    static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String getStringFromDate(Date date) {
        return FORMAT.format(date);
    }

    public static Date getDateFromString(String dateString) throws ParseException {
       return FORMAT.parse(dateString);
    }

    public static Date findEndingDate(Date startingDate, Integer noOfMonths) {
        LocalDate localFrom = startingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localTo = localFrom.plusMonths(noOfMonths);
        return Date.from(localTo.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
