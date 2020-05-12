package com.ge.dashboard.utils.config.builder;

import com.ge.dashboard.utils.config.builder.enums.ResponseType;

import java.util.List;
import java.util.Objects;

public class CustomResponse {

    private ResponseType responseType;
    private String message;
    private List<?> result;

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomResponse response = (CustomResponse) o;
        return responseType == response.responseType &&
                Objects.equals(message, response.message) &&
                Objects.equals(result, response.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseType, message, result);
    }
}
