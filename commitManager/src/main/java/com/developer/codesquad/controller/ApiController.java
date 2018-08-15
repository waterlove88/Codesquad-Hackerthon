package com.developer.codesquad.controller;

import com.developer.codesquad.domain.Event;
import com.developer.codesquad.domain.User;
import com.developer.codesquad.service.ApiService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
@SessionAttributes("USER_INFO")
public class ApiController {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApiService apiService;

    @Value("${github.oauth.client_id}")
    private String clientId;

    @Value("${github.oauth.client_secret}")
    private String clientSecret;

    @GetMapping("/commit/recent")
    public ResponseEntity getCommitRecent(@RequestParam(value = "login", required = false) String loginId,
                                          @ModelAttribute(value = "USER_INFO", binding = false) User user) {

        if (StringUtils.isEmpty(loginId)) {
            loginId = user.getLogin();
        }

        Gson gson = new Gson();

        StringBuilder stringBuilder = new StringBuilder("https://api.github.com/users/");
        stringBuilder.append(loginId).append("/events").append("?type=pushEvent");

        URI uri = UriComponentsBuilder.fromUriString(stringBuilder.toString())
                // Add query parameter
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret).build().toUri();

        RequestEntity requestEntity = new RequestEntity(HttpMethod.GET, uri);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity,
                new ParameterizedTypeReference<String>() {});

        LOGGER.debug("response headers > " + responseEntity.getHeaders());
        JsonArray eventList = gson.fromJson(responseEntity.getBody(), JsonArray.class);
        LOGGER.debug("eventList > " + gson.toJson(eventList));

        if (eventList != null && eventList.size() > 0) {

            Event event = apiService.getRecentEventFromEventList(eventList);

            return new ResponseEntity<>(event, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

}
