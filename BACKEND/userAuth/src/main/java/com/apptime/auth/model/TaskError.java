package com.apptime.auth.model;
import com.apptime.auth.model.ErrorType;

public class TaskError {
    ErrorType errorType;
    Object payload;
    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public Object getPayload() {
        return payload;
    }


    public TaskError( ErrorType erTyp, Object payload){
    this.errorType = erTyp;
    this.payload = payload;
    }
}
