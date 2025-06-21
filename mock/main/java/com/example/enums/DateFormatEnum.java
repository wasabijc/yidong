package com.example.enums;

import java.text.SimpleDateFormat;

public enum DateFormatEnum
{
    DATE_TIME("yyyy-MM-dd HH:mm:ss"),
    DATE("yyyy-MM-dd"),
    TIME("HH:mm:ss"),
    YEAR_MONTH("yyyy-MM");

    private String formatPattern;
    private ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<>();

    DateFormatEnum(String formatPattern)
    {
        this.formatPattern = formatPattern;
    }

    public String getFormatPattern()
    {
        return formatPattern;
    }

    public SimpleDateFormat getFormatter()
    {
        SimpleDateFormat formatterLocal = formatter.get();
        if (formatterLocal == null)
        {
            formatterLocal = new SimpleDateFormat(formatPattern);
            formatter.set(formatterLocal);
        }
        return formatterLocal;
    }
}
