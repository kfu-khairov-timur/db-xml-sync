package com.example.testovoe_zadanie.model;

import java.util.Objects;

public class PositionKey {
    private final String depCode;
    private final String depJob;

    public PositionKey(String depCode, String depJob) {
        this.depCode = depCode;
        this.depJob = depJob;
    }

    public String getDepCode() {
        return depCode;
    }

    public String getDepJob() {
        return depJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionKey)) return false;
        PositionKey that = (PositionKey) o;
        return depCode.equals(that.depCode) && depJob.equals(that.depJob);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depCode, depJob);
    }
}