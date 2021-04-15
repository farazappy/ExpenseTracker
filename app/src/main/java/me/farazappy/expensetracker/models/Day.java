package me.farazappy.expensetracker.models;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

public class Day implements Serializable {
    private int id;
    private String name;
    private String createdAt;

    public Day() {
    }

    public Day(String name) {
        this.name = name;
    }

    public Day(int id, String name, String createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getMonth() {
        HashMap<String,Integer> monthsMapping = new HashMap<String,Integer>();
        monthsMapping.put("January",1);
        monthsMapping.put("February",2);
        monthsMapping.put("March",3);
        monthsMapping.put("April",4);
        monthsMapping.put("May",5);
        monthsMapping.put("June",6);
        monthsMapping.put("July",7);
        monthsMapping.put("August",8);
        monthsMapping.put("September",9);
        monthsMapping.put("October",10);
        monthsMapping.put("November",11);
        monthsMapping.put("December",12);
        String str = new StringBuilder(this.getName()).reverse().toString().substring(6);

        String newStr = "";
        for(int j=0;j<str.length();j++)
        {
            if(str.charAt(j) != ' ')
            {
                newStr += String.valueOf(str.charAt(j));
            }
            else {
                break;
            }
        }

        newStr = new StringBuilder(newStr).reverse().toString();

        return monthsMapping.get(newStr);
    }
}
