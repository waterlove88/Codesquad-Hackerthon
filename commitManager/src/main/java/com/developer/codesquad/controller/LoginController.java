package com.developer.codesquad.controller;

import java.net.URISyntaxException;
import java.util.Map;

import com.developer.codesquad.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.developer.codesquad.service.AccessTokenService;
import com.developer.codesquad.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private final AccessTokenService accessTokenService;
    private final UserService userService;

    private final String GITHUB_OAUTH_URI = "https://github.com/login/oauth/authorize";

    private final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GITHUB_OAUTH_URI);

    @Autowired
    public LoginController(final AccessTokenService accessTokenService,
                           final UserService userService,
                           @Value("${github.oauth.client_id}") String clientId,
                           @Value("${github.oauth.scope}") String scope,
                           @Value("${github.oauth.redirect_uri}") String redirectUri) {
        this.accessTokenService = accessTokenService;
        this.userService = userService;

        builder.queryParam("client_id", clientId);
        builder.queryParam("scope", scope);
        builder.queryParam("redirect_uri", redirectUri);
    }

    @GetMapping(value = {"/", "/login"})
    public String viewLogin(Model model) {
        model.addAttribute("oauthUrl", builder.toUriString());
        return "login";
    }

    @GetMapping("/oauth/authorized")
    public String getUserInfo(@RequestParam("code") final String code,
                              final HttpSession httpSession) throws URISyntaxException {
        Map<String, Object> userMap = userService.getUserInfo(accessTokenService.getAccessToken(code));
        httpSession.setAttribute("USER_INFO", makeUserInfo(userMap));
        return "main";
    }

    private User makeUserInfo(Map<String, Object> userMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(userMap, User.class);
    }

}
