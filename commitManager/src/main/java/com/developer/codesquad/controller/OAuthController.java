package com.developer.codesquad.controller;

import com.developer.codesquad.service.AccessTokenService;
import com.developer.codesquad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/oauth")
public class OAuthController {

    private final AccessTokenService accessTokenService;
    private final UserService userService;

    private final String GITHUB_OAUTH_URI = "https://github.com/login/oauth/authorize";

    private final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GITHUB_OAUTH_URI);

    @Autowired
    public OAuthController(final AccessTokenService accessTokenService,
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

    @GetMapping("/authorized")
    @ResponseBody
    public List<Object> getUserEmails(@RequestParam("code") final String code) {
        return userService.getUserEmails(accessTokenService.getAccessToken(code));
    }

}
