package com.developer.codesquad.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.developer.codesquad.common.ResultMaster;
import com.developer.codesquad.dao.BatchDao;
import com.developer.codesquad.domain.BatchRequest;
import com.developer.codesquad.domain.TokenRequest;

@Service
public class BatchService {
	@Autowired
	private BatchDao batchDao;
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final ParameterizedTypeReference<Map<String, Object>> TYPE_REF_MAP_STRING_OBJ = new ParameterizedTypeReference<Map<String, Object>>() {};
	
	@Value("${push.serverKey}")
	private String serverKey;
	
	private String pushApi = "https://fcm.googleapis.com/fcm/send";
	
	public ResultMaster sendPush() throws URISyntaxException {
		ResultMaster rm = new ResultMaster("200", "success");
		List<BatchRequest> target = batchDao.sendPush();
		
		String[] registration_ids = new String[target.size()];
		for(int i=0; i<target.size(); i++) {
			registration_ids[i] = target.get(i).getToken();
		}
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json");
		httpHeaders.set("Authorization", "key=" + serverKey);
		
		HashMap<String, Object> body = new HashMap<String, Object>();
		body.put("registration_ids", registration_ids);
		
		HashMap<String, String> notification = new HashMap<String, String>();
		notification.put("title","commitManager 알림");
		notification.put("body", "최근 commit 시간 : ");
		body.put("notification", notification);
		
		
		RequestEntity<HashMap<String, Object>> requestEntity = new RequestEntity<>(body, httpHeaders, HttpMethod.POST, new URI(pushApi));
		ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestEntity, TYPE_REF_MAP_STRING_OBJ);

		if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
			throw new ResponseStatusException(responseEntity.getStatusCode());
		}
		
		rm.setBody(responseEntity.getBody());
		return rm;
	}
	
	public ResultMaster isPushAgree(TokenRequest tokenRequest) {
		HashMap<String, String> map = new HashMap<String, String>();
		ResultMaster rm = null;
		
		int target = batchDao.isPushAgree(tokenRequest);
		if(target > 0) {
			map.put("isPushAgree", "Y");
			rm = new ResultMaster("200", "success");
		} else {
			map.put("isPushAgree", "N");
			rm = new ResultMaster("1002", "no select");
		}
		rm.setBody(map);
		
		return rm;
	}
	
	public ResultMaster setToken(TokenRequest tokenRequest) {
		int target = batchDao.setToken(tokenRequest);
		
		if(target > 0) {
			return new ResultMaster("200", "success");
		}
		
		return new ResultMaster("1002", "fail setToken");
	}
}
