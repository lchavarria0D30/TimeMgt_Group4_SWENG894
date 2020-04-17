package com.apptime.auth.model;

public class Prediction {
    public String getConfidance() {
        return Confidance;
    }

    public void setConfidance(String confidance) {
        Confidance = confidance;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    String Confidance;
    String Duration;
}
