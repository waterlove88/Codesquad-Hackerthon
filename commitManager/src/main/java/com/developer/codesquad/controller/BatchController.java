package com.developer.codesquad.controller;

import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.developer.codesquad.common.ResultMaster;
import com.developer.codesquad.domain.TokenRequest;
import com.developer.codesquad.service.BatchService;

@RestController
@RequestMapping("/batch")
public class BatchController {
	@Autowired
	private BatchService batchService;
	
	@GetMapping("/sendPush")
	public String sendPush() {
		return batchService.sendPush();
	}
	
	@PostMapping("/isPushAgree")
	public ResultMaster isPushAgree(@ModelAttribute @Valid TokenRequest tokenRequest, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return new ResultMaster("1001", "Bad request");
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("isPushAgree", "Y");
		ResultMaster rm = new ResultMaster("200", "success");
		rm.setBody(map);
		
		return rm;
		
		//return batchService.isPushAgree(tokenRequest);
	}
	
	@PostMapping("/setToken")
	public String setToken(@ModelAttribute @Valid TokenRequest tokenRequest, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "bad request";
		}
		
		return batchService.setToken(tokenRequest);
	}
}
