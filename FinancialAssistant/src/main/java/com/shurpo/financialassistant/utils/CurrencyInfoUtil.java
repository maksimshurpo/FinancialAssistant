package com.shurpo.financialassistant.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrencyInfoUtil {
    private int id;
    private Date date;
    private Double rate;

    public CurrencyInfoUtil(int id, Date date, Double rate) {
        this.id = id;
        this.date = date;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Double getRate() {
        return rate;
    }

    public String toString(){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return "currencyId = " + id + ", date" + format.format(date) + ", rate = " + rate;
    }
}
