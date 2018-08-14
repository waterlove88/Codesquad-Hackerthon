package com.developer.codesquad.service.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.developer.codesquad.domain.AccessTokenRequest;
import com.developer.codesquad.service.AccessTokenService;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    private final ParameterizedTypeReference<Map<String, String>> TYPE_REF_MAP_STRING_STRING = new ParameterizedTypeReference<Map<String, String>>() {};

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${github.oauth.client_id}")
    private String clientId;

    @Value("${github.oauth.client_secret}")
    private String clientSecret;

    @Override
    public String getAccessToken(final String code) throws URISyntaxException {
        final AccessTokenRequest accessTokenRequest = new AccessTokenRequest(clientId, clientSecret, code);
        final RequestEntity<AccessTokenRequest> requestEntity = new RequestEntity<>(accessTokenRequest, HttpMethod.POST,
                new URI("https://github.com/login/oauth/access_token"));
        final ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(requestEntity, TYPE_REF_MAP_STRING_STRING);

        final Map<String, String> responseBody = responseEntity.getBody();

        return responseBody.get("access_token");
    }
}
