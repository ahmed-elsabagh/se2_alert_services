package com.se2.alert.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.se2.alert.dto.PasswordJson;
import com.se2.alert.service.IRunAlertService;

@RestController
@RequestMapping(path = "/stop")
public class StopAlertController {

	@Autowired
	private IRunAlertService runAlertService;

	private final static Logger logger = LoggerFactory.getLogger(StopAlertController.class);

	@PostMapping(path = "/online")
	public @ResponseBody String stopOnline(@RequestBody(required = true) PasswordJson passwordJson) {
		logger.debug("stop online run called");
		return runAlertService.stopOnline(passwordJson);
	}

	@PostMapping(path = "/offline")
	public @ResponseBody String stopOffline(@RequestBody(required = true) PasswordJson passwordJson) {
		logger.debug("stop offline run called");
		return runAlertService.stopOffline(passwordJson);
	}
}
