package com.developer.codesquad.service;

import com.developer.codesquad.domain.Event;
import com.google.gson.JsonArray;

public interface ApiService {
    Event getRecentEventFromEventList(String loginId);
}
