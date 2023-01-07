package com.mvc.comercialplus.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtils {
	    
    public static String getStringDateHourFromMilli(Long instantMilli) {

		Instant now = Instant.ofEpochMilli(instantMilli);
        ZonedDateTime zdt = ZonedDateTime.ofInstant(now, ZoneId.systemDefault());
		
		return zdt.toString();
	}

    public static String getStringDate() {
		
        String formatedDate = getStringDateHour();
		
        return formatedDate.substring(0, 10);
	}
	
	public static String getStringHour() {
		
        String hour = getStringDateHour();
		
        return hour.substring(hour.indexOf("T")+1, hour.indexOf("."));
	}

    public static Long getLongInstant() {
		
        Long nowInMilliseconds = Instant.now().toEpochMilli();
		
        return nowInMilliseconds;
	}
    
    /**
    * Private method used in this class to serve as
    * a base for building an human-readable "date" and
    * "hour", like a clock. 
    * Example: "2022-08-28T13:21:18.474409400-05:00[America/Rio_Branco]". 
    * It takes in count the timezone of the host computer.
    * @return  an string like this 
    */
    private static String getStringDateHour() {

		Instant now = Instant.now();
        ZonedDateTime zdt = ZonedDateTime.ofInstant(now, ZoneId.systemDefault());
		
		return zdt.toString();
	}

}

