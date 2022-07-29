package com.se2.alert.controller;

import com.se2.alert.dto.FileDto;
import com.se2.alert.dto.FilterDto;
import com.se2.alert.service.impl.AlertDashboardExportServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/export")
public class AlertExportController {

    private final static Logger logger = LoggerFactory.getLogger(AlertDashboardController.class);

    @Autowired
    private AlertDashboardExportServiceImpl alertDashboardExportServiceImpl;


    @ApiOperation(value = "excel sheet with timeline data")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 200, message = "download timeline success.")
    @PostMapping(path = {"/timeline/{userId:[\\d]+}"})
    public ResponseEntity<?> exportAlerts(@PathVariable(value = "userId", required = true) int userId,
                                          @RequestHeader("Host") String host,
                                          @RequestBody(required = false) Map<Integer, List<FilterDto>> filterDto)  {

        logger.debug("----------------------- Start exportAlerts  with userId: ---------------------- {}" , userId);

        FileDto fileDto = alertDashboardExportServiceImpl.getExportedFile(userId, host , filterDto);


        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=%s", fileDto.getFileName() + ".xlsx"));
        MediaType mediaType = new MediaType("application", "vnd.ms-excel");
        logger.debug("----------------------- End exportAlerts  with userId: ---------------------- {}" , userId);

        return ResponseEntity.ok().headers(headers).contentType(mediaType).body(fileDto.getFileData());
    }
}
