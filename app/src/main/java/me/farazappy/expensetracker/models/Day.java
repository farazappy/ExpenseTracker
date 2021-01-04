package me.farazappy.expensetracker.models;

import java.io.Serializable;

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
}
