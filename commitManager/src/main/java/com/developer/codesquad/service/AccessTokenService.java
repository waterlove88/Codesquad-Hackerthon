package com.developer.codesquad.service;

import java.net.URISyntaxException;

public interface AccessTokenService {
    String getAccessToken(String code) throws URISyntaxException;
}
