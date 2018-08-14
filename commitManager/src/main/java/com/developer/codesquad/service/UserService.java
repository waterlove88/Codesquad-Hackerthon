package com.developer.codesquad.service;

import java.util.List;

public interface UserService {
    List<Object> getUserEmails(String accessToken);
}
