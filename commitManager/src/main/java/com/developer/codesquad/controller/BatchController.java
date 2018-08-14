package com.developer.codesquad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.developer.codesquad.service.BatchService;

@Controller
@RequestMapping("/batch")
public class BatchController {
	@Autowired
	private BatchService batchService;
	
	@GetMapping("/sendPush")
    @ResponseBody
    public String sendPush() {
        return batchService.sendPush();
    }
}
