package com.developer.codesquad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.developer.codesquad.dao.BatchDao;
import com.developer.codesquad.domain.BatchRequest;
import com.developer.codesquad.domain.TokenRequest;

@Service
public class BatchService {
	@Autowired
	private BatchDao batchDao;
	
	public String sendPush() {
		List<BatchRequest> target = batchDao.sendPush();
		
		for(int i=0; i<target.size(); i++) {
			System.out.println(target.get(i).getId());
		}
		
		return "sendPush";
	}
	
	public String isPushAgree(TokenRequest tokenRequest) {
		int target = batchDao.isPushAgree(tokenRequest);
		
		if(target > 0) {
			return "Y";
		}
		
		return "N";
	}
	
	public String setToken(TokenRequest tokenRequest) {
		int target = batchDao.setToken(tokenRequest);
		
		if(target > 0) {
			return "setToken";
		}
		
		return "fail setToken";
	}
}
