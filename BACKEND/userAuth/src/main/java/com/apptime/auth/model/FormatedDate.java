package com.apptime.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class FormatedDate {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date getDate() {
        return date;
    }
    public Date setDate() {
        return date;
    }
}
