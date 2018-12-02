package com.base.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ApiErrorMsg implements Serializable {

    private static final long serialVersionUID = 1478345902893450774L;

    private String errorMessage;
    private static class Detail implements Serializable{

        private static final long serialVersionUID = -4641836873290728807L;

        private final String target;
        private final String message;

        private Detail(String target, String message){
            this.target = target;
            this.message = message;
        }

        public String getTarget() {
            return target;
        }

        public String getMessage() {
            return message;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<Detail> details = new ArrayList<>();

    public void addDetail(String target, String message){
        details.add(new Detail(target, message));
    }

    public List<Detail> getDetails() {
        return details;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ApiErrorMsg{");
        sb.append("errorMessage='").append(errorMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

