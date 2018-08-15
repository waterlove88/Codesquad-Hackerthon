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
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiServiceImpl.class);

    @Override
    public Event getRecentEventFromEventList(JsonArray eventList) {
        Event event = new Event();
        Gson gson = new Gson();

        JsonObject recentEvent = (JsonObject) eventList.get(0);
        LOGGER.debug("recentEvent > " + recentEvent);
        JsonObject payload = (JsonObject) recentEvent.get("payload");

        if (payload != null) {
            JsonArray commits = payload.getAsJsonArray("commits");
            if (commits != null && commits.size() > 0) {
                Type commitType = new TypeToken<List<Commit>>(){}.getType();
                List<Commit> commitList = gson.fromJson(commits.toString(), commitType);
                event.setCommitList(commitList);
            }
        }

        event.setLogin(((JsonObject)recentEvent.get("actor")).get("login").getAsString());

        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        String createdAt = recentEvent.get("created_at").getAsString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.parse(createdAt, formatter), zoneId);
        DateTimeFormatter realFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        event.setCreatedAt(zonedDateTime.format(realFormatter));

        return event;
    }
}
