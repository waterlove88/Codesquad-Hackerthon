package com.developer.codesquad.domain;

import java.io.Serializable;

public class Commit implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Commit{");
        sb.append("message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
