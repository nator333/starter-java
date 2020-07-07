package com.twilio.starter.domain;

import java.util.List;

public class JsonResponse<T> {
    private List<String> errors;
    private T data;
    private Object extensions;
    private boolean dataPresent;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Object getExtensions() {
        return extensions;
    }

    public void setExtensions(Object extensions) {
        this.extensions = extensions;
    }

    public boolean isDataPresent() {
        return dataPresent;
    }

    public void setDataPresent(boolean dataPresent) {
        this.dataPresent = dataPresent;
    }
}
