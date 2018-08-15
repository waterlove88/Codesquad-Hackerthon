package com.developer.codesquad.controller;

import java.net.URISyntaxException;

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
	public ResultMaster sendPush() throws URISyntaxException {
		return batchService.sendPush();
	}
	
	@PostMapping("/isPushAgree")
	public ResultMaster isPushAgree(@ModelAttribute @Valid TokenRequest tokenRequest, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return new ResultMaster("1001", "Bad request");
		}
		
		return batchService.isPushAgree(tokenRequest);
	}
	
	@PostMapping("/setToken")
	public ResultMaster setToken(@ModelAttribute @Valid TokenRequest tokenRequest, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return new ResultMaster("1001", "Bad request");
		}
		
		return batchService.setToken(tokenRequest);
	}
}
