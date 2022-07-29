package com.se2.alert.controller;

import com.se2.alert.dto.PasswordJson;
import com.se2.alert.dto.RunningOnlineJson;
import com.se2.alert.exception.RunningAlertException;
import com.se2.alert.service.IRunAlertService;
import com.se2.alert.service.IRunAlertStreamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/run")
public class RunAlertController {

	@Autowired
	private IRunAlertService runAlertService;

	@Autowired
	private IRunAlertStreamingService runAlertStreamingService;

	private final static Logger logger = LoggerFactory.getLogger(RunAlertController.class);

	/**
	 * @param from
	 *            ISO 8601 version without hyphens and colons YYYYMMDDThhmmssZ
	 *            instead of YYYY-MM-DDThh:mm:ssZ
	 * @param to
	 *            ISO 8601 version without hyphens and colons YYYYMMDDThhmmssZ
	 *            instead of YYYY-MM-DDThh:mm:ssZ
	 * @param settingIds
	 * @param isTest
	 * @param password
	 * @return
	 * @throws RunningAlertException
	 * @throws JSONException
	 */
	@PostMapping(path = "/online")
	public @ResponseBody String runOnline(@RequestBody(required = true) RunningOnlineJson runningOnlineJson) throws RunningAlertException {
		logger.debug("start online run called with runningOnlineJson: {}" , runningOnlineJson);
		return "{\"result\":\"" + runAlertService.runOnline(runningOnlineJson) + "\"}";
	}
	@PostMapping(path = "/onlineStreaming")
	public @ResponseBody void runOnlineStreaming(@RequestBody(required = true) RunningOnlineJson runningOnlineJson) throws RunningAlertException {
		logger.debug("start onlineStreaming run called");
		 runAlertStreamingService.runStreamingBatches();
	}

	@PostMapping(path = "/offline")
	public @ResponseBody String runOffline(@RequestBody(required = true) PasswordJson passwordJson) throws RunningAlertException {
		logger.debug("start run alert called with runningOfflineJson: {}" , passwordJson);
		return "{\"result\":\"" + runAlertService.runOffline(passwordJson) + "\"}";
	}

	@PostMapping(path = "/resend")
	public @ResponseBody String reSendFailedEmail(@RequestBody(required = true) PasswordJson passwordJson) throws RunningAlertException {
		logger.debug("start re send failed email called with runningOfflineJson: {} " , passwordJson);
		return "{\"result\":\"" + runAlertService.reSendFailedEmail(passwordJson) + "\"}";
	}

	@GetMapping(path = "/streaming")
	public ResponseEntity<String> runStreaming(@RequestParam("batchIDs") int batchId) {
		logger.debug("start streaming run called with batchId: {}", batchId);
		String result = "Fail";
		try {
			result = runAlertStreamingService.runStreaming(batchId);
			return ResponseEntity.status(HttpStatus.OK).body("{\"result\":\"" + result + "\"}");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}

	@GetMapping(path = "/streaming/status")
	public ResponseEntity<String> getStreamingStatus() {
		logger.debug("streamingStatus called");
		String status = "Fail";
		try {
			status = runAlertStreamingService.getStreamingStatus();
			return ResponseEntity.status(HttpStatus.OK).body(status);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(status);
		}
	}

	@PostMapping(path = "/offline/inventory")
	public @ResponseBody String runInventoryOffline(@RequestBody(required = true) PasswordJson passwordJson) throws RunningAlertException {
		logger.debug("start run alert called with runningOfflineJson: {}" , passwordJson);
		return "{\"result\":\"" + runAlertService.runInventoryOffline(passwordJson) + "\"}";
	}

}