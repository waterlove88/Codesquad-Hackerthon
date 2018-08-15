package com.developer.codesquad.controller;

import com.developer.codesquad.domain.Event;
import com.developer.codesquad.domain.User;
import com.developer.codesquad.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@SessionAttributes("USER_INFO")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @GetMapping("/commit/recent")
    public ResponseEntity getCommitRecent(@RequestParam(value = "login", required = false) String loginId,
                                          @ModelAttribute(value = "USER_INFO", binding = false) User user) {

        if (StringUtils.isEmpty(loginId)) {
            loginId = user.getLogin();
        }

        Event event = apiService.getRecentEventFromEventList(loginId);

        if (event != null) {
            return new ResponseEntity<>(event, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

}
