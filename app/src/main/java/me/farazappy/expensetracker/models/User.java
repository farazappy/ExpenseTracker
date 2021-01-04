package me.farazappy.expensetracker.models;

public class User {
    private String id;
    private String displayName;

    public User() {
    }

    public User(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String _id) {
        this.id = _id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
