package com.rudyn.drones.errors;

import java.util.List;

public class ValidationErrorResponse {
    private List<String> fields;
    private List<String> errors;

    public ValidationErrorResponse() {
    }

    public ValidationErrorResponse(List<String> errors) {
        this.errors = errors;
    }

    public ValidationErrorResponse(List<String> fields, List<String> errors) {
        this.fields = fields;
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
