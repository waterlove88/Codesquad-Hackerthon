package com.developer.codesquad.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {
    private String login;
    private String createdAt;
    private List<Commit> commitList = new ArrayList<>();

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Commit> getCommitList() {
        return commitList;
    }

    public void setCommitList(List<Commit> commitList) {
        this.commitList = commitList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Event{");
        sb.append("login='").append(login).append('\'');
        sb.append(", createdAt='").append(createdAt).append('\'');
        sb.append(", commitList=").append(commitList);
        sb.append('}');
        return sb.toString();
    }
}
