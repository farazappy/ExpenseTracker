package me.farazappy.expensetracker.models;

import java.io.Serializable;

public class Item implements Serializable {
    private int id;
    private String name;
    private double cost;
    private String createdAt;
    private int dayId;

    public Item() {
    }

    public Item(int id, String name, double cost, String createdAt, int dayId) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.createdAt = createdAt;
        this.dayId = dayId;
    }

    public Item(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public Item(String name, double cost, int dayId) {
        this.name = name;
        this.cost = cost;
        this.dayId = dayId;
    }

    public Item(int id, String name, double cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }
}
