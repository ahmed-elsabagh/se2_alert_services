package com.se2.alert.controller;

import com.se2.alert.dto.AlertEmailHistoryDto;
import com.se2.alert.service.IAlertEmailHistoryService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/emailHistory")
public class AlertEmailHistoryController {
	
	private final  Logger logger = LoggerFactory.getLogger(AlertEmailHistoryController.class);

	@Autowired
	private IAlertEmailHistoryService alertEmailHistoryService;

	@GetMapping(path = { "/forSetting/{settingId:[\\d]+}", "/forSetting/{settingId:[\\d]+}/{pageNumber:[1-9][\\d]*}",
			"/forSetting/{settingId:[\\d]+}/{pageNumber:[1-9][\\d]*}/{pageSize:[1-9][\\d]*}" })
	public @ResponseBody Iterable<AlertEmailHistoryDto> getAllFeatures(
			@PathVariable(value = "settingId", required = true) int settingId,
			@PathVariable(value = "pageNumber", required = false) Optional<Integer> pageNumber,
			@PathVariable(value = "pageSize", required = false) Optional<Integer> pageSize) {

		if (pageNumber.isPresent() && pageSize.isPresent())
			return alertEmailHistoryService.getAllEmailHistoryForSetting(settingId, pageNumber.get(), pageSize.get());

		if (pageNumber.isPresent())
			return alertEmailHistoryService.getAllEmailHistoryForSetting(settingId, pageNumber.get());

		return alertEmailHistoryService.getAllEmailHistoryForSetting(settingId);
	}

	@GetMapping("/downloadFile/{historyId:[\\d]+}/{fileType}")
	@ApiImplicitParam(name = "historyId", required = true, dataTypeClass = Long.class)
	@ApiResponse(code = 200, message = "downloadFile returned successfully.", response = ResponseEntity.class)
	@ApiOperation(value = "Download attachments of alert mail history " )
	public ResponseEntity<Resource> downloadFile(@ApiParam(value = "history id to retrive excel file from DB to be downloaded", required = true)
												 @PathVariable Long historyId, HttpServletRequest request, @PathVariable String fileType) {
		Resource resource = alertEmailHistoryService.loadFileAsResource(historyId,fileType);
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.error("error while get resource content: {}" , ex.getMessage());
			return ResponseEntity.noContent().header("Content-Length", "0").build();
		}
		if (contentType == null ) {
			if(fileType != null && fileType.equals("excel")){
				contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			} else if(fileType != null && fileType.equals("pcn")) {
				contentType = "application/zip";
			}
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	@GetMapping(path = { "/forSettingCount/{settingId:[\\d]+}" })
	@ApiOperation(value = "Get the count of email history")
	public @ResponseBody Integer getAllEmailHistoryCount(
			@ApiParam(value = "setting id to retrive the count of email history", required = true) @PathVariable(value = "settingId", required = true) int settingId) {

		return alertEmailHistoryService.getAllEmailHistoryCount(settingId);
	}
	
	@GetMapping("/emailBodyHistory/{historyId:[\\d]+}")
	@ApiOperation(value = "load alert mail history " )
	@ApiImplicitParam
	@ApiResponses(value = {
	        @ApiResponse(code = 201, message = "Check if history is retrieved or not") })
	public String emailBodyHistory(@ApiParam(value = "history id to retrive excel file from DB to be downloaded",required = true)
			@PathVariable Long historyId, HttpServletRequest request) {
		return alertEmailHistoryService.loadEmailHistoryBody(historyId);
		
	}
}