package com.rudyn.drones.errors;

import java.util.List;

public class ValidationOnlyErrorResponse {
    private List<String> errors;

    public ValidationOnlyErrorResponse() {
    }

    public ValidationOnlyErrorResponse(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

}
