package com.developer.codesquad.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
import com.developer.codesquad.domain.Event;
import com.developer.codesquad.domain.TokenRequest;

@Service
public class BatchService {
	@Autowired
	private BatchDao batchDao;
	
	@Autowired
	private ApiService apiService;
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final ParameterizedTypeReference<Map<String, Object>> TYPE_REF_MAP_STRING_OBJ = new ParameterizedTypeReference<Map<String, Object>>() {};
	
	@Value("${push.serverKey}")
	private String serverKey;
	
	@Value("${email.from.address}")
	private String fromAddress;
	
	@Value("${email.host}")
	private String emailHost;
	
	@Value("${email.password}")
	private String emailPassword;
	
	private String pushApi = "https://fcm.googleapis.com/fcm/send";
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	public ResultMaster sendPush() throws URISyntaxException, ParseException {
		ResultMaster rm = new ResultMaster("200", "success");
		List<BatchRequest> target = batchDao.sendPush();
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", "application/json");
		httpHeaders.set("Authorization", "key=" + serverKey);
		
		for(int i=0; i<target.size(); i++) {
			Event event = apiService.getRecentEventFromEventList(target.get(i).getId());
			
			Date currentTime = new Date();
			String thisTime = format.format(currentTime);
			
			Date date1 = format.parse(thisTime);
			Date date2 = format.parse(event.getCreatedAt());
			
			if(date1.compareTo(date2) > 0) {
				long calDate = date1.getTime() - date2.getTime();
				long calDateDays = calDate / (24*60*60*1000); 
				
				HashMap<String, Object> body = new HashMap<String, Object>();
				body.put("to", target.get(i).getToken());
				
				HashMap<String, String> notification = new HashMap<String, String>();
				notification.put("title","commitManager 알림");
				notification.put("body", "최근 commit 시간 : "+event.getCreatedAt() +"\n마지막으로 commit 한지 "+calDateDays+"일 지났습니다.\n어서 commit 하세요!");
				body.put("notification", notification);
				
				RequestEntity<HashMap<String, Object>> requestEntity = new RequestEntity<>(body, httpHeaders, HttpMethod.POST, new URI(pushApi));
				ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestEntity, TYPE_REF_MAP_STRING_OBJ);

				if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
					throw new ResponseStatusException(responseEntity.getStatusCode());
				}
			}
		}
		
		return rm;
	}
	
	public ResultMaster sendMail() throws AddressException, MessagingException, ParseException {
		ResultMaster rm = new ResultMaster("200", "success");
		List<BatchRequest> target = batchDao.sendMail();
		
		//properties 설정
		Properties props = new Properties();
		props.put("mail.smtps.auth", "true");
		// 메일 세션
        Session session = Session.getDefaultInstance(props);
        Transport transport = session.getTransport("smtps");
		transport.connect(emailHost, fromAddress, emailPassword);
		
		for(int i=0; i<target.size(); i++) {
			Event event = apiService.getRecentEventFromEventList(target.get(i).getId());
			
			Date currentTime = new Date();
			String thisTime = format.format(currentTime);
			
			Date date1 = format.parse(thisTime);
			Date date2 = format.parse(event.getCreatedAt());
			
			if(date1.compareTo(date2) > 0) {
				long calDate = date1.getTime() - date2.getTime();
				long calDateDays = calDate / (24*60*60*1000);
				
				MimeMessage msg = new MimeMessage(session);
				 // 메일 관련
		        msg.setSubject("commitManager 알림");
		        msg.setText("최근 commit 시간 : "+event.getCreatedAt() +"\n마지막으로 commit 한지 "+calDateDays+"일 지났습니다.\n어서 commit 하세요!");
		        msg.setFrom(new InternetAddress(fromAddress));
		        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(target.get(i).getEmail()));
				 
				// 발송
				transport.sendMessage(msg, msg.getAllRecipients());
			}
		}
       
		transport.close();
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
