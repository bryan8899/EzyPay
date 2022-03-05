package com.ezypay.restservices;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.StringJoiner;

import static java.time.temporal.ChronoUnit.WEEKS;

@Service
public class InvoiceService {
    public String create(String json) {
        String statement = "";
        Double amount_invoice = 0.0;
        String subscription_type = "";
        String date_of_month = "";
        String day_of_week = "";
        String start_date = "";
        String end_date = "";

        JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
        amount_invoice = Double.parseDouble(convertedObject.get("invoice_amount").getAsString());
        subscription_type = convertedObject.get("subscription_type").getAsString();

        if (subscription_type.equalsIgnoreCase("monthly")){
            date_of_month = convertedObject.get("date_of_month").getAsString();
        }else if (subscription_type.equalsIgnoreCase("weekly")){
            day_of_week = convertedObject.get("day_of_week").getAsString();
        }

        start_date = convertedObject.get("start_date").getAsString();
        end_date = convertedObject.get("end_date").getAsString();
        ArrayList <LocalDate> invoice_dates = new ArrayList<LocalDate>();
        invoice_dates =processDate(subscription_type,start_date,end_date);

        StringJoiner joiner = new StringJoiner(",");
        invoice_dates.forEach(item -> joiner.add(item.toString()));
        joiner.toString();

        statement = "Details of Invoice: "+"\n"+
                "Amount of Invoice: "+amount_invoice+"\n"+
                "Subscription Type: "+subscription_type+"\n"+
                "Invoice dates: "+ joiner.toString();

        return statement;
    }

    public ArrayList<LocalDate> processDate(String subscriptionType, String startDate, String endDate){
        LocalDate startdate = LocalDate.parse(startDate);
        LocalDate enddate = LocalDate.parse(endDate);

        ArrayList <LocalDate> invoice_date_array = new ArrayList<LocalDate>();
        if (subscriptionType.equals("monthly")){
            Period diff = Period.between(
                    LocalDate.parse(startDate).withDayOfMonth(1),
                    LocalDate.parse(endDate).withDayOfMonth(1));

            if (diff.toTotalMonths()>3 && !(diff.toTotalMonths() <=0)){
                return null;
            }else{
                invoice_date_array.add(startdate);
                for (int b=0;b< diff.toTotalMonths();b++){
                    startdate = startdate.plusMonths(1);
                    invoice_date_array.add(startdate);
                }
            }

        }else if (subscriptionType.equals("weekly")){
            invoice_date_array.add(startdate);
            long duration = WEEKS.between(startdate, enddate);
            for (int b=1;b<duration+1;b++){
                startdate = startdate.plusDays(7);
                invoice_date_array.add(startdate);
            }
        }
        return invoice_date_array;
    }

}
