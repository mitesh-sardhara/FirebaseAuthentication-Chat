package com.bytesnmaterials.zro.util;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * This is DateTime Util class, used to format date and time.
 *
 * @author mahya
 * @version 1.0
 * @since 25/11/2015
 */
public class ZeroDateTimeUtil {
    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Method used to get utc date time as date value.
     *
     * @return UTC date and time as Date.
     */
    public static Date getUTCdatetimeAsDate()
    {
        //note: doesn't check for null
        return stringDateToDate(getUTCDateTimeAsString());
    }

    /**
     * Method used to get utc date time as string value.
     *
     * @return UTC date and time as string.
     */
    public static String getUTCDateTimeAsString()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        return utcTime;
    }

    public static long getUTCDateTimeAsMillis()
    {
        return System.currentTimeMillis();
    }


    public static String getUTCDateTimeAsStringTemp()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        return utcTime;
    }


    public static Date stringDateToDate(String StrDate)
    {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);

        try
        {
            dateToReturn = (Date)dateFormat.parse(StrDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return dateToReturn;
    }
}
