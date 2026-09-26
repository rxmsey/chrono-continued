package com.chrono;

import lombok.Getter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Getter
public enum ReleaseDate {
    //_04_JANUARY_2001("04 January 2001", new GregorianCalendar(2001, Calendar.JANUARY, 5).getTime()),
    JANUARY_2001("28 January 2001",new GregorianCalendar(2001, Calendar.FEBRUARY, 31).getTime()),
    FEBRUARY_2001("February 2001",new GregorianCalendar(2001, Calendar.MARCH, 1).getTime()),
    MARCH_2001("March 2001",new GregorianCalendar(2001, Calendar.MARCH, 31).getTime()),
    APRIL_2001("April 2001",new GregorianCalendar(2001, Calendar.APRIL, 30).getTime()),
    //_08_MAY_2001("08 May 2001",new GregorianCalendar(2001, Calendar.MAY, 8).getTime()),
    MAY_2001("24 May 2001",new GregorianCalendar(2001, Calendar.MAY, 31).getTime()),
    JUNE_2001("June 2001", new GregorianCalendar(2001, Calendar.JUNE, 30).getTime()),
    JULY_2001("July 2001", new GregorianCalendar(2001, Calendar.JULY, 31).getTime()),
    AUGUST_2001("August 2001", new GregorianCalendar(2001, Calendar.AUGUST, 31).getTime()),
    SEPTEMBER_2001("September 2001", new GregorianCalendar(2001, Calendar.DECEMBER, 30).getTime()),
    __("empty slot to separate 01 and 02", new GregorianCalendar(2001, Calendar.DECEMBER, 31).getTime()),
    FEBRUARY_2002("February 2002", new GregorianCalendar(2002, Calendar.FEBRUARY, 28).getTime()),
    //_18_MARCH_2002("18 March 2002", new GregorianCalendar(2002, Calendar.MARCH, 19).getTime()),
    MARCH_2002("March 2002", new GregorianCalendar(2002, Calendar.MARCH, 31).getTime()),
    //_09_APRIL_2002("9 April 2002", new GregorianCalendar(2002, Calendar.MARCH, 31).getTime()),
    APRIL_2002("30 April 2002", new GregorianCalendar(2002, Calendar.APRIL, 30).getTime()),
    //_10_MAY_2002("10 May 2002", new GregorianCalendar(2002, Calendar.MAY, 31).getTime()),
    MAY_2002("28 May 2002", new GregorianCalendar(2002, Calendar.MAY, 31).getTime()),
    JUNE_2002("June 2002", new GregorianCalendar(2002, Calendar.JUNE, 30).getTime()),
    JULY_2002("July 2002", new GregorianCalendar(2002, Calendar.JULY, 31).getTime()),
    AUGUST_2002("August 2002", new GregorianCalendar(2002, Calendar.AUGUST, 31).getTime()),
    SEPTEMBER_2002("September 2002", new GregorianCalendar(2002, Calendar.SEPTEMBER, 30).getTime()),
    OCTOBER_2002("October 2002", new GregorianCalendar(2002, Calendar.OCTOBER, 31).getTime()),
    DECEMBER_2002("December 2002", new GregorianCalendar(2002, Calendar.DECEMBER, 31).getTime()),
    ___("Empty space separating 02-03", new GregorianCalendar(2002, Calendar.DECEMBER, 31).getTime()),
    JANUARY_2003("January 2003", new GregorianCalendar(2003, Calendar.JANUARY, 31).getTime()),
    //_3_MARCH_2003("3 March 2003", new GregorianCalendar(2003, Calendar.MARCH, 3).getTime()),
    MARCH_2003("March 2003", new GregorianCalendar(2003, Calendar.MARCH, 31).getTime()),
    APRIL_2003("April 2003", new GregorianCalendar(2003, Calendar.APRIL, 30).getTime()),
    MAY_2003("May 2003", new GregorianCalendar(2003, Calendar.MAY, 31).getTime()),
    JUNE_2003("June 2003", new GregorianCalendar(2003, Calendar.JUNE, 30).getTime()),
    JULY_2003("July 2003", new GregorianCalendar(2003, Calendar.JULY, 31).getTime()),
    AUGUST_2003("August 2003", new GregorianCalendar(2003, Calendar.AUGUST, 31).getTime()),
    SEPTEMBER_2003("September 2003", new GregorianCalendar(2003, Calendar.DECEMBER, 31).getTime()),
    ____("space between 03-04", new GregorianCalendar(2003, Calendar.DECEMBER, 31).getTime()),
    MARCH_2004("29 March 2004", new GregorianCalendar(2004, Calendar.MARCH, 31).getTime()),
    APRIL_2004("20 April 2004", new GregorianCalendar(2004, Calendar.APRIL, 30).getTime()),
    MAY_2004("18 May 2004", new GregorianCalendar(2004, Calendar.MAY, 31).getTime()),
    JUNE_2004("29 June 2004", new GregorianCalendar(2004, Calendar.JUNE, 30).getTime()),
    JULY_2004("27 July 2004", new GregorianCalendar(2004, Calendar.JULY, 30).getTime()),
    //_9_AUGUST_2004("9 August 2004", new GregorianCalendar(2004, Calendar.AUGUST, 10).getTime()),
    AUGUST_2004("24 August 2004", new GregorianCalendar(2004, Calendar.AUGUST, 30).getTime()),
    SEPTEMBER_2004("September 2004", new GregorianCalendar(2004, Calendar.SEPTEMBER, 30).getTime()),
    OCTOBER_2004("October 2004", new GregorianCalendar(2004, Calendar.OCTOBER, 31).getTime()),
    //_2_NOVEMBER_2004("2 November 2004", new GregorianCalendar(2004, Calendar.NOVEMBER, 4).getTime()),
    //_17_NOVEMBER_2004("17 November 2004", new GregorianCalendar(2004, Calendar.NOVEMBER, 20).getTime()),
    NOVEMBER_2004("November 2004", new GregorianCalendar(2004, Calendar.NOVEMBER, 30).getTime()),
    //_6_DECEMBER_2004("6 December 2004", new GregorianCalendar(2004, Calendar.DECEMBER, 10).getTime()),
    //_13_DECEMBER_2004("13 December 2004", new GregorianCalendar(2004, Calendar.DECEMBER, 20).getTime()),
    DECEMBER_2004("December 2004",new GregorianCalendar(2004, Calendar.DECEMBER, 30).getTime()),
    _____("space betwwen 04-05",new GregorianCalendar(2004, Calendar.DECEMBER, 31).getTime()),
    JANUARY_2005("January 2005",new GregorianCalendar(2005, Calendar.JANUARY, 31).getTime());

    public String name;
    public Date date;

    ReleaseDate(String name, Date date) {
        this.name = name;
        this.date = date;
    }
}
