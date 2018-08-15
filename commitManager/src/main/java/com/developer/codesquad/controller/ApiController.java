package com.developer.codesquad.controller;

import com.developer.codesquad.domain.Commit;
import com.developer.codesquad.domain.Event;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController("/api")
public class ApiController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${github.oauth.client_id}")
    private String clientId;

    @Value("${github.oauth.client_secret}")
    private String clientSecret;

    @RequestMapping("/commit/recent")
    public ResponseEntity getCommitRecent(@RequestParam("login") String loginId) throws URISyntaxException {
        Event event = null;
        Gson gson = new Gson();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("client_id", clientId);
        httpHeaders.set("client_secret", clientSecret);

        StringBuilder stringBuilder = new StringBuilder("https://api.github.com/users/");
        stringBuilder.append(loginId).append("?type=pushEvent");
        ResponseEntity<JsonArray> responseEntity = restTemplate.exchange(new RequestEntity<>(httpHeaders,
                        HttpMethod.GET, new URI(stringBuilder.toString())),
                new ParameterizedTypeReference<JsonArray>() {});

        JsonArray eventList = responseEntity.getBody();

        if (eventList != null && eventList.size() > 0) {
            JsonObject recentEvent = (JsonObject) eventList.get(0);
            JsonObject payload = (JsonObject) recentEvent.get("payload");
            JsonArray commits = payload.getAsJsonArray("commits");
            Type commitType = new TypeToken<List<String>>(){}.getType();
            List<Commit> commitList = gson.fromJson(commits.toString(), commitType);

            event.setLogin(((JsonObject)recentEvent.get("author")).get("login").getAsString());
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
