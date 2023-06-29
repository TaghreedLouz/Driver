package com.example.driveroutreach.ui.app_utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppUtility {


  public static String getDate(){
        SimpleDateFormat simpleformat = new SimpleDateFormat("dd-MMMM-yyyy");
       String date=simpleformat.format(Calendar.getInstance().getTime());
       return date;
    }


    public static String getDateTime(){
        SimpleDateFormat simpleformat = new SimpleDateFormat("dd-MMMM-yyyy hh-mm-ss");
        String date=simpleformat.format(Calendar.getInstance().getTime());
        return date;
    }






}
