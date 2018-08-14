package com.developer.codesquad.service;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface UserService {
    List<Object> getUserEmails(String accessToken) throws URISyntaxException;

    Map<String, Object> getUserInfo(String accessToken) throws URISyntaxException;
}
