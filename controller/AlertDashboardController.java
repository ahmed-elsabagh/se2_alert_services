package com.se2.alert.controller;

import com.se2.alert.dto.*;
import com.se2.alert.entity.IssuePriority;
import com.se2.alert.entity.IssueStatus;
import com.se2.alert.entity.Site;
import com.se2.alert.entity.User;
import com.se2.alert.exception.InvalidRequestParameterException;
import com.se2.alert.service.*;
import com.se2.alert.utility.RequestUtils;
import com.se2.om.cacheLayer.CacheService;
import io.swagger.annotations.*;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.se2.alert.constant.Constants.*;

@RestController
@RequestMapping(path = "/dashboard")
public class AlertDashboardController {

    private final static Logger logger = LoggerFactory.getLogger(AlertDashboardController.class);

    @Qualifier("alertDashboardServiceImpl")
    @Autowired
    private IAlertDashboardService alertDashboardService;

    @Autowired
    private IUserService userService;

    private CacheService cacheService;

    @Autowired
    private ISiteService siteService;

    @Autowired
    private IssueAssigneeService issueAssigneeService;

    @Autowired
    public void setCacheService(@Qualifier("redisServiceImpl") CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Qualifier("alertStatusServiceImpl")
    @Autowired
    private IAlertStatusService alertStatusService;

    @GetMapping(path = {"/{dashboardId:[\\d]+}"})
    @ApiOperation(value = "getDashBoardForId")
    @ApiImplicitParam
    @ApiResponse(code = 200, message = "getDashBoardForId returned successfully.", response = AlertDashboardDetailDto.class)
    public AlertDashboardDetailDto getDashBoardForId(@PathVariable(value = "dashboardId", required = true) long dashboardId) {
        logger.debug("getDashBoardForId called with dashboardId: {}", dashboardId);
        return alertDashboardService.getDashboardById(dashboardId);
    }

    @ApiOperation(value = "getNotSeenDashboardForSiteByUserId by userId from DB ")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 200, message = "getNotSeenDashboardForSiteByUserId returned successfully.", response = Integer.class)
    @GetMapping(path = {"/notseen/{userId:[\\d]+}"})
    public int getNotSeenDashboardForSiteByUserId(@PathVariable(value = "userId", required = true) int userId,
    		@RequestHeader(name ="access_token", required = false) String token) {
        logger.info("getNotSeenDashboardForSiteByUserId called with userId {} ", userId);
        
        int siteId = 0;
        
        if(token != null) {
           siteId = alertDashboardService.getSiteIdFromToken(token,userId);
        }
        
		Integer notSeenCached = (Integer) cacheService.fetchFromCache(CACHED_DASHBOARD_NOTSEEN, CACHED_DASHBOARD_NOTSEEN + "_" + siteId);
        if(notSeenCached != null) {
        	return notSeenCached;
        }else {
        	if(siteId > 0) {
        		return alertDashboardService.getNotSeenDashboardBySiteId(siteId);        		     		
        	}else {
        		User user = userService.findUserByUserId(userId);
        		if (user != null) {
    	            siteId = user.getSiteId();
    	            return alertDashboardService.getNotSeenDashboardBySiteId(siteId);
    	        } else {
    	            return 0;
    	        }   
        	}        	
        }
    }
    @GetMapping(path = {"/allReceivedAlert/{siteId:[\\d]+}"})
    @ApiOperation(value = "getAllReceivedAlertDashboardBySiteId by siteId from DB ")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 200, message = "getNotSeenDashboardForSiteByUserId returned successfully.", response = Integer.class)
    public int getAllReceivedAlertDashboardBySiteId(@PathVariable(value = "siteId", required = true) int siteId) {
        logger.debug("getAllReceivedAlertDashboardBySiteId called with siteId: {}", siteId);
        return alertDashboardService.getAllReceivedAlertDashboardBySiteId(siteId);
    }

    /**
     * @param userId
     * @param pageNumber
     * @param pageSize
     * @return List<AlertDashboardDto>
     */
    @ApiOperation(value = "Returns List of AlertDashboardDto")
    @GetMapping(path = {"/all/{userId:[\\d]+}", "/all/{userId:[\\d]+}/{pageNumber:[1-9][\\d]*}",
            "/all/{userId:[\\d]+}/{pageNumber:[1-9][\\d]*}/{pageSize:[1-9][\\d]*}"})
    public List<AlertDashboardDto> getAllDashboardsForSiteByUserId(@PathVariable(value = "userId", required = true) int userId,
                                                                   @PathVariable(value = "pageNumber", required = false) Optional<Integer> pageNumber,
                                                                   @PathVariable(value = "pageSize", required = false) Optional<Integer> pageSize) {

        logger.debug("getAllDashboardsForSiteByUserId called with userId: {} ", userId);

        User user = userService.findUserByUserId(userId);
        if (user != null) {
            if (pageNumber.isPresent() && pageSize.isPresent())
                return alertDashboardService.getAllDashboardsForSite(false,user.getSiteId(), pageNumber.get(), pageSize.get());

            if (pageNumber.isPresent())
                return alertDashboardService.getAllDashboardsForSite(false,user.getSiteId(), pageNumber.get());

            return alertDashboardService.getAllDashboardsForSite(false,user.getSiteId());

        } else {
            return new ArrayList<>();
        }
    }

    @ApiOperation(value = "get FirstFourDashboardsForUser by userId from DB ")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 200, message = "get FirstFourDashboardsForUser returned successfully.", response = ArrayList.class)
    @GetMapping(path = "/FirstFourDashboardsForUser/{userId:[\\d]+}")
    public List<AlertDashboardDto> getFirstFourDashboardsForUser(@PathVariable(value = "userId", required = true) int userId,
                                                                 @RequestHeader(name = "access_token", required = false) String token) {
        logger.info("FirstFourDashboardsForUser called with userId: {}", userId);
        int siteId = 0;

        if(token != null) {
            siteId = alertDashboardService.getSiteIdFromToken(token,userId);
        }
        List<AlertDashboardDto> firstFourDashboardsForUserCached = (List<AlertDashboardDto>) cacheService.fetchFromCache(CACHED_DASHBOARD_DATA, CACHED_DASHBOARD_FOR_SITE + "_" + siteId);
        if (firstFourDashboardsForUserCached != null) {
            return firstFourDashboardsForUserCached;
        } else {
            if (siteId > 0) {
                return alertDashboardService.getAllDashboardsForSite(false, siteId, 1, 4);
            } else {
                User user = userService.findUserByUserId(userId);
                if (user != null) {
                    siteId = user.getSiteId();
                    return alertDashboardService.getAllDashboardsForSite(false, siteId, 1, 4);
                } else {
                    return new ArrayList<>();
                }
            }
        }
    }


    /**
     * @param userId
     * @param isArchive
     * @return Map<Integer, List < FilterDto>>
     */
    @ApiOperation(value = "Returns List of filters options for timeline filters")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 200, message = "getInitialFilterDto returned successfully.", response = Map.class)
    @PostMapping("/filter/{userId:[\\d]+}/{isArchive:[0-1]}")
    public Map<Integer, List<FilterDto>> getInitialFilterDto(@PathVariable(value = "userId", required = true) int userId,
                                                             @PathVariable(value = "isArchive", required = false) boolean isArchive,
                                                             @RequestBody(required = false) Map<Integer, List<FilterDto>> filterDto) {
        logger.debug("getInitialFilterDto called with userId: {} and isArchive: {} ", userId, isArchive);

        User user = userService.findUserByUserId(userId);

        Optional<Site> siteOptional = siteService.findSiteById(user.getSiteId());
        Site site = (siteOptional.orElseGet(Site::new));

        return alertDashboardService.getInitialFilterDto(site.getSiteId(),user.getUserEmail(),filterDto);
    }


    /**
     * @param userId
     * @param pageNumber
     * @param pageSize
     * @param isArchive
     * @return List<AlertDashboardDto>
     */
    @ApiOperation(value = "Returns List of AlertDashboardDto for timeline filters")
    @PostMapping(path = {"/applyFilter/{userId:[\\d]+}/{isArchive:[0-1]}",
            "/applyFilter/{userId:[\\d]+}/{isArchive:[0-1]}/{pageNumber:[1-9][\\d]*}",
            "/applyFilter/{userId:[\\d]+}/{isArchive:[0-1]}/{pageNumber:[1-9][\\d]*}/{pageSize:[1-9][\\d]*}"})
    public List<AlertDashboardDto> applyFilter(@PathVariable(value = "userId", required = true) int userId,
                                               @PathVariable(value = "isArchive", required = false) boolean isArchive,
                                               @PathVariable(value = "pageNumber", required = false) Optional<Integer> pageNumber,
                                               @PathVariable(value = "pageSize", required = false) Optional<Integer> pageSize,
                                               @RequestBody(required = true) Map<Integer, List<FilterDto>> filterDto) {
        logger.debug("applyFilter called with userId: {} and filterDto:  {} ", userId, filterDto);
        User user = userService.findUserByUserId(userId);
        if (user != null) {
            if (pageNumber.isPresent() && pageSize.isPresent())
                return alertDashboardService.applyFilter(user.getUserEmail(), user.getSiteId(), filterDto, pageNumber.get(), pageSize.get());

            if (pageNumber.isPresent())
                return alertDashboardService.applyFilter(user.getUserEmail(), user.getSiteId(),  filterDto, pageNumber.get());

            return alertDashboardService.applyFilter(user.getUserEmail(), user.getSiteId(), filterDto);
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/fillDashboardWithItemsNames")
    @ApiOperation(value = "items names DB column migration")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "companyId", required = false, dataType = "List<Integer>")
    })
    @ApiResponse(code = 200, message = "fillDashboardWithItemsNames get succ. ")
    public ResponseEntity<Boolean> fillDashboardWithItemsNames(@RequestParam(value = "companyId" , required = false) List<Integer> companyId) {
        boolean succeed = alertDashboardService.fillDashboardWithItemsNames(companyId);
        return new ResponseEntity<>(succeed, HttpStatus.OK);
    }

    @PatchMapping("/flag")
    public ResponseEntity<Boolean> flagByDashboardIds(@RequestBody(required = true) List<Long> dashboardIds) {
        logger.debug("flagByDashboardIds called with dashboardIds: {}", dashboardIds);
        boolean succeed = alertDashboardService.updateFlagForDashboards(true, dashboardIds);
        return new ResponseEntity<Boolean>(succeed, succeed ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/unflag")
    public ResponseEntity<Boolean> unFlagByDashboardIds(@RequestBody(required = true) List<Long> dashboardIds) {
        logger.debug("unFlagByDashboardIds called with dashboardIds: {}", dashboardIds);
        boolean succeed = alertDashboardService.updateFlagForDashboards(false, dashboardIds);
        return new ResponseEntity<Boolean>(succeed, succeed ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Mark dashboard items as deleted ")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 200, message = "Items was deleted successfully ", response = Boolean.class)
    @PatchMapping("/delete")
    public ResponseEntity<Boolean> deleteItemsByDashboardIds(@RequestBody(required = true) List<Long> dashboardIds) {
        logger.debug("deleteByDashboardIds called with dashboardIds: {}", dashboardIds);
        boolean succeed = alertDashboardService.deleteDashboardItems( dashboardIds);
        return new ResponseEntity<Boolean>(succeed, succeed ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * @return String
     */
    @ApiOperation(value = "Clear Cache")
    @ApiImplicitParam
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cache cleared successfully")})
    @GetMapping("/clearCache")
    public String clearCache() {
        return alertDashboardService.clearDashboardCache();
    }

    /**
     * @param issueStatusDto
     * @return ResponseEntity
     */
    @ApiOperation(value = "save an AlertStatusDto in DB ")
    @PostMapping(value = "/status/save")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 204, message = "AlertStatusDto is saved", response = IssueStatusDto.class)
    public @ResponseBody
    IssueStatus saveAlertStatusDTO(
            @RequestBody(required = true) IssueStatusDto issueStatusDto,
            @RequestHeader(name = "access_token") String token, BindingResult errors) {
        if (errors.hasErrors()) {
            logger.error("BindingResult error during configure saveAlertStatus  : {}", issueStatusDto);
            throw new InvalidRequestParameterException("BindingResult error during configure saveAlertStatus  : " + issueStatusDto);
        }
        UserInfo userInfo = RequestUtils.extractUserInfoFromAccessToken(token);
        issueStatusDto.setLoggedUser(userInfo.getJti());
        issueStatusDto.setSiteId(Integer.parseInt(userInfo.getSiteId()));
        logger.debug("saveAlertStatus called with alertStatusDto: {}", issueStatusDto);
        return alertStatusService.saveAlertStatus(issueStatusDto);
    }

    /**
     * @param issuePriorityDto
     * @return IssuePriority
     */
    @ApiOperation(value = "save an Issue Priority in DB ")
    @PostMapping(value = "/priority/save")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 200, message = "IssuePriorityDto is saved", response = IssuePriority.class)
    public @ResponseBody
    IssuePriority saveIssuePriorityDTO(
            @RequestBody(required = true) IssuePriorityDto issuePriorityDto,
            @RequestHeader(name = "access_token", required = false) String token, BindingResult errors) {
        if (errors.hasErrors()) {
            logger.error("BindingResult error during configure saveIssuePriority  : {}", issuePriorityDto);
            throw new InvalidRequestParameterException("BindingResult error during configure saveIssuePriority  : " + issuePriorityDto);
        }
        logger.debug("saveIssuePriority called with issuePriorityDto: {}", issuePriorityDto);
        return alertStatusService.saveIssuePriority(issuePriorityDto);

    }

    /**
     * @return List <IssueSieStatusListDto>
     */
    @ApiOperation(value = "Get status list by siteId")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 200, message = "get issue site list done from db", response = ArrayList.class)
    @GetMapping(value = "/statusSite")
    public @ResponseBody
    List <IssueSieStatusListDto> statusSite(
            @RequestHeader(name = "access_token") String token) {
        UserInfo userInfo = RequestUtils.extractUserInfoFromAccessToken(token);
        int siteId = Integer.parseInt(userInfo.getSiteId());
        logger.debug("statusSite called with siteId: {}", siteId);

        return alertStatusService.getIssueStatus(siteId);
    }

    /**
     * @return List <IssueSitePriorityListDto>
     */
    @ApiOperation(value = "Get priority list by siteId")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 200, message = "get priority site list done from db", response = ArrayList.class)
    @GetMapping(value = "/prioritySite")
    public @ResponseBody
    List <IssueSitePriorityListDto> prioritySite(
            @RequestHeader(name = "access_token") String token) {
        UserInfo userInfo = RequestUtils.extractUserInfoFromAccessToken(token);
        int siteId = Integer.parseInt(userInfo.getSiteId());
        logger.debug("prioritySite called with siteId: {}", siteId);

        return alertStatusService.getIssuePriority(siteId);
    }

    @PostMapping(path = {"/{dashboardId:[\\d]+}/assignees"})
    @ApiOperation(value = "Add new Assignee to issue")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 201, message = "Assignee Added successfully.", response = Boolean.class)
    public ResponseEntity<Boolean> addAssigneeToIssue(@PathVariable(value = "dashboardId") Integer dashboardId,@RequestBody Integer userId,
                                                      @RequestHeader(name = "access_token") String token) {
        logger.debug("add assignee to issue : {}", dashboardId);
        UserInfo userInfo = RequestUtils.extractUserInfoFromAccessToken(token);

        boolean succeed = false;
        try {
            succeed = issueAssigneeService.assignUserToIssue( dashboardId,userId,userInfo.getJti());
        } catch (NotFoundException e) {
            logger.error("error in assigning user to issue",e);
        }
        return new ResponseEntity<Boolean>(succeed, succeed ? HttpStatus.CREATED : HttpStatus.NOT_FOUND);
    }
    @DeleteMapping(path = {"/{dashboardId:[\\d]+}/assignees/{assigneeId:[\\d]+}"})
    @ApiOperation(value = "Delete Assignee from issue")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 201, message = "Assignee removed successfully.", response = Boolean.class)
    public ResponseEntity<Boolean> deleteAssigneeFromIssue(@PathVariable(value = "dashboardId") Integer dashboardId,@PathVariable(value = "assigneeId") Integer assigneeId,
                                                           @RequestHeader(name = "access_token") String token) {
        logger.debug("remove assignee from issue : {}", dashboardId);
        UserInfo userInfo = RequestUtils.extractUserInfoFromAccessToken(token);


        boolean succeed = false;
        try {
            succeed = issueAssigneeService.removeUserFromIssue( dashboardId,assigneeId,userInfo.getJti());
        } catch (NotFoundException e) {
            logger.error("error in removing user from issue",e);
        }
        return new ResponseEntity<Boolean>(succeed, succeed ? HttpStatus.CREATED : HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = {"/{dashboardId:[\\d]+}/assignees/search"})
    @ApiOperation(value = "Search for users to assign")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 200, message = "Search for users to assign.", response = SimpleUserDto.class)
    public ResponseEntity<List<SimpleUserDto>> getAvailableUsersToAssign(@PathVariable(value = "dashboardId") Integer dashboardId,
                                                                         @RequestParam(value = "name",required = false) String name ,
                                                                         @RequestHeader(name = "access_token") String token) {
        logger.debug("Search for users to assign to issue : {}", dashboardId);
        UserInfo userInfo = RequestUtils.extractUserInfoFromAccessToken(token);
        AssigneeSearch assigneeSearch =new AssigneeSearch();
        assigneeSearch.setIssueId(dashboardId);
        assigneeSearch.setSiteId(Integer.valueOf(userInfo.getSiteId()));
        assigneeSearch.setName(name);

        return  ResponseEntity.ok(issueAssigneeService.searchAssignee(assigneeSearch));
    }

    @GetMapping(path = {"/history"})
    @ApiOperation(value = "Search for users to assign")
    @ApiImplicitParam(name = "access_token", required = true, dataTypeClass = String.class)
    @ApiResponse(code = 200, message = "Search for users to assign.", response = SimpleUserDto.class)
    public ResponseEntity<List<MfNewsAnalysisDto>> getSCNewsHistoryByEventNameAndMatchedManId(@RequestParam(value = "eventName",required = false) String eventName ,
                                                                         @RequestParam(value = "matchedManId",required = false) Double matchedManId ,
                                                                         @RequestHeader(name = "access_token") String token) {
        logger.debug("Search news history with event name : {} and matched Man Id : {}", eventName, matchedManId);
        return  ResponseEntity.ok(alertDashboardService.getNewsAnalysisByEventNameAndMatchedManId(eventName, matchedManId));
    }
}
