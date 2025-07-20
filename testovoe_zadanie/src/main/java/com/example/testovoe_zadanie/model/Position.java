package com.example.testovoe_zadanie.model;

public class Position {
    private String depCode;
    private String depJob;
    private String description;

    public Position(String depCode, String depJob, String description) {
        this.depCode = depCode;
        this.depJob = depJob;
        this.description = description;
    }

    public String getDepCode() {
        return depCode;
    }

    public String getDepJob() {
        return depJob;
    }

    public String getDescription() {
        return description;
    }
}