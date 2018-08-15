package com.developer.codesquad.service.impl;

import com.developer.codesquad.domain.Commit;
import com.developer.codesquad.domain.Event;
import com.developer.codesquad.service.ApiService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${github.oauth.client_id}")
    private String clientId;

    @Value("${github.oauth.client_secret}")
    private String clientSecret;


    @Override
    public Event getRecentEventFromEventList(final String loginId) {

        Event event = null;
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

            event = new Event();

            JsonObject recentEvent = (JsonObject) eventList.get(0);
            LOGGER.debug("recentEvent > " + recentEvent);
            JsonObject payload = (JsonObject) recentEvent.get("payload");

            if (payload != null) {
                JsonArray commits = payload.getAsJsonArray("commits");
                if (commits != null && commits.size() > 0) {
                    Type commitType = new TypeToken<List<Commit>>() {
                    }.getType();
                    List<Commit> commitList = gson.fromJson(commits.toString(), commitType);
                    event.setCommitList(commitList);
                }
            }

            event.setLogin(((JsonObject) recentEvent.get("actor")).get("login").getAsString());

            ZoneId zoneId = ZoneId.of("Asia/Seoul");
            String createdAt = recentEvent.get("created_at").getAsString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.parse(createdAt, formatter), zoneId);
            DateTimeFormatter realFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            event.setCreatedAt(zonedDateTime.format(realFormatter));

        }

        return event;
    }
}
