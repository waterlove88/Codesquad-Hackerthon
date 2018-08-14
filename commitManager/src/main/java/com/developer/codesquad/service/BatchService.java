package com.developer.codesquad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.developer.codesquad.dao.BatchDao;
import com.developer.codesquad.domain.BatchRequest;

@Service
public class BatchService {
	@Autowired
	private BatchDao batchDao;
	
	public String sendPush() {
		
		List<BatchRequest> a = batchDao.sendPush();
		
		for(int i=0; i<a.size(); i++) {
			System.out.println(a.get(i).getId());
		}
		
		return "sendPush";
	}
}
