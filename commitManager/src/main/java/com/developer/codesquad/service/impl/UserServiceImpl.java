package com.developer.codesquad.service.impl;

import com.developer.codesquad.service.UserService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final ParameterizedTypeReference<List<Object>> TYPE_REF_LIST_OBJECT = new ParameterizedTypeReference<List<Object>>() {};

    private final ParameterizedTypeReference<Map<String, Object>> TYPE_REF_MAP_STRING_OBJ = new ParameterizedTypeReference<Map<String, Object>>() {};

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Object> getUserEmails(final String accessToken) throws URISyntaxException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "token " + accessToken);

        RequestEntity<String> requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.GET,
                new URI("https://api.github.com/user/emails"));

        ResponseEntity<List<Object>> responseEntity = restTemplate.exchange(requestEntity, TYPE_REF_LIST_OBJECT);

        if (responseEntity.getStatusCode().is4xxClientError() ||
        responseEntity.getStatusCode().is5xxServerError()) {
            throw new ResponseStatusException(responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }

    @Override
    public Map<String, Object> getUserInfo(String accessToken) throws URISyntaxException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "token " + accessToken);

        RequestEntity<String> requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.GET,
                new URI("https://api.github.com/user"));

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestEntity, TYPE_REF_MAP_STRING_OBJ);

        if (responseEntity.getStatusCode().is4xxClientError() ||
                responseEntity.getStatusCode().is5xxServerError()) {
            throw new ResponseStatusException(responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }
}
