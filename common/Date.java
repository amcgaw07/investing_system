package common;

public class Date{
    private int month;
    private int day;
    private int year;

    public Date(int month, int day, int year)
    {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public int getMonth() { return month; }
    public int getDay() { return day; }
    public int getYear() { return year; }



    public String toStringDashes()
    {
        return String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(year);
    }

    @Override
    public String toString() { return String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year); }


}
