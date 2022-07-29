package com.se2.alert.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/health")
public class HealthCheckController {

	private final static Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

	@GetMapping(path = "/SmokeTest")
	public ResponseEntity<String> getDashBoardForId() {
		logger.info("health check called");
		return ResponseEntity.status(HttpStatus.OK).body("Up");
	}
}