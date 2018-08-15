package com.developer.codesquad.controller;

import com.developer.codesquad.domain.Commit;
import com.developer.codesquad.domain.Event;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${github.oauth.client_id}")
    private String clientId;

    @Value("${github.oauth.client_secret}")
    private String clientSecret;

    @GetMapping("/commit/recent")
    public ResponseEntity getCommitRecent(@RequestParam("login") String loginId) throws URISyntaxException {
        Event event = new Event();
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
            JsonObject recentEvent = (JsonObject) eventList.get(0);
            LOGGER.debug("recentEvent > " + recentEvent);
            JsonObject payload = (JsonObject) recentEvent.get("payload");
            JsonArray commits = payload.getAsJsonArray("commits");
            Type commitType = new TypeToken<List<Commit>>(){}.getType();
            List<Commit> commitList = gson.fromJson(commits.toString(), commitType);

            event.setLogin(((JsonObject)recentEvent.get("actor")).get("login").getAsString());
            event.setCommitList(commitList);

            ZoneId zoneId = ZoneId.of("Asia/Seoul");
            String createdAt = recentEvent.get("created_at").getAsString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(zoneId);
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(createdAt, formatter).withZoneSameInstant(zoneId);
            DateTimeFormatter realFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            event.setCreatedAt(zonedDateTime.format(realFormatter));

            return new ResponseEntity<>(event, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

}
