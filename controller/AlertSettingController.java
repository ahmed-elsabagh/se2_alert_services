package com.se2.alert.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.se2.alert.constant.Constants;
import com.se2.alert.dto.*;
import com.se2.alert.entity.AlertSetting;
import com.se2.alert.entity.Distributors;
import com.se2.alert.entity.ThreatLevelValue;
import com.se2.alert.entity.User;
import com.se2.alert.exception.NotValidSettingException;
import com.se2.alert.repository.UserRepository;
import com.se2.alert.scope.ScopeFactory;
import com.se2.alert.service.DistributorsService;
import com.se2.alert.service.IAlertSettingItemsService;
import com.se2.alert.service.IAlertSettingService;
import com.se2.alert.service.ThreatLevelValueService;
import com.se2.alert.utility.RequestUtils;
import com.se2.om.cacheLayer.CacheService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.se2.alert.constant.Constants.CACHED_DASHBOARD_NOTSEEN;
import static com.se2.alert.constant.Constants.CACHED_SETTINGS_DISTRIBUTORS;

@RestController
@RequestMapping(path = "/setting")
@Api(value="Alert Setting Management", description="Operations controlling setting in Alert Setting Management")
public class AlertSettingController {

	private final static Logger logger = LoggerFactory.getLogger(AlertSettingController.class);

	@Autowired
	private IAlertSettingService alertSettingService;

	@Autowired
	private IAlertSettingItemsService alertSettingItemsService;

	@Autowired
	private UserRepository userRepository;

	@Autowired 
	private ScopeFactory scopeFactory;


	@Autowired
	private ThreatLevelValueService threatLevelValueService;

	@Autowired
	private DistributorsService distributorsService;

	private CacheService cacheService;

	@Autowired
	public void setCacheService(@Qualifier("redisServiceImpl") CacheService cacheService) {
		this.cacheService = cacheService;
	}

	@GetMapping(path = "haveSetting/{userId:[\\d]+}/{itemIds}/{scopeId:[\\d]}")
	public @ResponseBody Map<Integer, AlertSettingStatusDto> areHaveSetting(
			@PathVariable(value = "userId", required = true) int userId,
			@PathVariable(value = "itemIds", required = true) LinkedList<Integer> itemIds,
			@PathVariable(value = "scopeId", required = true) int scopeId) {
		return alertSettingItemsService.areHaveSettings(userId, itemIds, scopeId);
	}

	/**
	 *
	 * @param userId
	 * @param itemId
	 * @param scopeId
	 * @return itemName
	 */
	@GetMapping(path = "itemName/{userId:[\\d]+}/{itemId:[\\d]+}/{scopeId:[\\d]}")
	public @ResponseBody String itemName(
			@PathVariable(value = "userId", required = true) int userId,
			@PathVariable(value = "itemId", required = true) int itemId,
			@PathVariable(value = "scopeId", required = true) int scopeId) {
		String itemName=scopeFactory.getSettingName(itemId, scopeId);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ItemNameSite itemSite=new ItemNameSite(itemName);
		String json = gson.toJson(itemSite);

		return json;
	}
	/**
	 *
	 * @param userId
	 * @param itemId
	 * @param scopeId
	 * @return AlertSettingDto
	 */
	@ApiOperation(value = "Get an AlertSettingDto by userId & itemId and scopeId") 
	@GetMapping(value = "/new/{userId:[\\d]+}/{itemId:[\\d]+}/{scopeId:[\\d]}")
	public @ResponseBody AlertSettingDto getNewSetting(@PathVariable(value = "userId", required = true) int userId,
			@PathVariable(value = "itemId", required = true) Integer itemId,
			@PathVariable(value = "scopeId", required = true) int scopeId) {
		return alertSettingService.getNewSetting(userId, itemId, scopeId);
	}

	/**
	 *
	 * @param settingId
	 * @return AlertSettingDto
	 */
	@ApiOperation(value = "Get an AlertSettingDto by settingId")
	@GetMapping(value = "/edit/{settingId:[\\d]+}")
	public @ResponseBody AlertSettingDto editSetting(
			@PathVariable(value = "settingId", required = true) Integer settingId) throws NotValidSettingException {
		logger.debug("editSetting called with settingId: {}" , settingId);
		return alertSettingService.getExistingSetting(settingId);
	}

	/**
	 *
	 * @param AlertSettingDto
	 * @return AlertSettingStatusDto
	 */
	@ApiOperation(value = "save an AlertSettingDto in DB ")
	@PostMapping(value = "/save")
	@ApiImplicitParam(name ="access_token",required = true,dataTypeClass = String.class)
	@ApiResponse(code= 200 ,message = "AlertSetting is saved",response = AlertSettingDto.class)
	public @ResponseBody AlertSettingStatusDto saveSetting(
			@RequestBody(required = true) AlertSettingDto alertSettingDto,
			@RequestHeader(name ="access_token") String token) {

		logger.info("saveSetting called, and will parse inputs now");

		UserInfo userInfo = RequestUtils.extractUserInfoFromAccessToken(token);

		String aclIdString = Optional.ofNullable(userInfo.getAclId()).filter(s -> !s.isEmpty()).orElse("-1");
		Integer aclId = Integer.parseInt(aclIdString);

		alertSettingDto.setLoggedUser(userInfo.getJti());
		alertSettingDto.setAclId(aclId);

		logger.info("saveSetting called with alertSettingDto: {}" , alertSettingDto);
		return alertSettingService.saveAlertSetting(alertSettingDto);

	}

	@GetMapping(path = "/{settingId:[\\d]+}")
	public @ResponseBody
	AlertSetting getSettingById(
			@PathVariable(value = "settingId", required = true) Integer settingId) {
		logger.debug("getSettingById called with settingId: {}" , settingId);
		return alertSettingService.findSettingById(settingId);
	}

	@GetMapping(path = { "/all", "/all/{pageNumber:[1-9][\\d]*}",
	"/all/{pageNumber:[1-9][\\d]*}/{pageSize:[1-9][\\d]*}" })
	public @ResponseBody Iterable<AlertSetting> getAllSettings(
			@PathVariable(value = "pageNumber", required = false) Optional<Integer> pageNumber,
			@PathVariable(value = "pageSize", required = false) Optional<Integer> pageSize) {

		if (pageNumber.isPresent() && pageSize.isPresent())
			return alertSettingService.getAllSettings(pageNumber.get(), pageSize.get());

		if (pageNumber.isPresent())
			return alertSettingService.getAllSettings(pageNumber.get());

		return alertSettingService.getAllSettings();
	}

	@GetMapping(path = { "/forUser/{userId:[\\d]+}", "/forUser/{userId:[\\d]+}/{pageNumber:[1-9][\\d]*}",
	"/forUser/{userId:[\\d]+}/{pageNumber:[1-9][\\d]*}/{pageSize:[1-9][\\d]*}" })
	public @ResponseBody Iterable<AlertSetting> getAllSettingsPerUser(
			@PathVariable(value = "userId", required = true) int userId,
			@PathVariable(value = "pageNumber", required = false) Optional<Integer> pageNumber,
			@PathVariable(value = "pageSize", required = false) Optional<Integer> pageSize) {

		if (pageNumber.isPresent() && pageSize.isPresent())
			return alertSettingService.getAllSettingsPerUser(userId, pageNumber.get(), pageSize.get());

		if (pageNumber.isPresent())
			return alertSettingService.getAllSettingsPerUser(userId, pageNumber.get());

		return alertSettingService.getAllSettingsPerUser(userId);
	}


	@DeleteMapping(path = "delete/{settingId}/{userId:[\\d]+}")
	@ApiOperation(value = "Delete alert setting according to maintain permission")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Deleted Successfully"),
			@ApiResponse(code = 400, message = "Invalid Setting Data, SettingId doesn't exist or may be have been deleted")})
	@ApiImplicitParam(name ="access_token",required = true,dataTypeClass = String.class)
	public @ResponseBody ResponseEntity<ResponseDTO<String>> deleteSettingByIdAndUserIdAccordingToMaintainPermission(
			@ApiParam(value = "setting id and user id to get the setting and check the permission of the user", required = true) @PathVariable List<Integer> settingId,
			@PathVariable(value = "userId", required = true) Integer userId,
			@RequestHeader(name ="access_token") String token) throws NotValidSettingException{

		UserInfo userInfo = RequestUtils.extractUserInfoFromAccessToken(token);

		String statusMsg = alertSettingService.deleteSettingByIdAndUserIdAccordingToMaintainPermission(settingId,
				userId,Integer.valueOf(userInfo.getJti()));
		if (statusMsg.equals(Constants.DELETED_SUCCESSFULLY))
			return new ResponseEntity<ResponseDTO<String>>(new ResponseDTO<>(statusMsg), HttpStatus.OK);

		return new ResponseEntity<ResponseDTO<String>>(new ResponseDTO<>(statusMsg, Constants.NOT_VALID_SETTING), HttpStatus.BAD_REQUEST);
	}

	/**
	 * @param siteId
	 * @param pageNumber
	 * @param pageSize
	 * @return List<AlertSetting>
	 */
	@ApiOperation(value = "Return list of AlertSetting per Site")
	@GetMapping(path = { "/forSite/{siteId:[\\d]+}", "/forSite/{siteId:[\\d]+}/{pageNumber:[1-9][\\d]*}",
	"/forSite/{siteId:[\\d]+}/{pageNumber:[1-9][\\d]*}/{pageSize:[1-9][\\d]*}" })
	public @ResponseBody Iterable<AlertSettingDto> getAllSettingsPerSite(@PathVariable(value = "siteId", required = true) int siteId,
			@PathVariable(value = "pageNumber", required = false) Optional<Integer> pageNumber,
			@PathVariable(value = "pageSize", required = false) Optional<Integer> pageSize) {

		if (pageNumber.isPresent() && pageSize.isPresent())
			return alertSettingService.getAllSettingsPerSiteId(siteId, pageNumber.get(), pageSize.get());

		if (pageNumber.isPresent())
			return alertSettingService.getAllSettingsPerSiteId(siteId, pageNumber.get());

		return alertSettingService.getAllSettingsPerSiteId(siteId);
	}

	/**
	 *
	 * @param settingId
	 * @param pageNumber
	 * @param pageSize
	 * @return List<AlertSettingDto>
	 */
	@ApiOperation(value = "Return List of AlertSettingDto for Specific Item per Site using settingId on that item")
	@GetMapping(path = { "/forItem/{settingId:[\\d]+}", "/forItem/{settingId:[\\d]+}/{pageNumber:[1-9][\\d]*}",
	"/forItem/{settingId:[\\d]+}/{pageNumber:[1-9][\\d]*}/{pageSize:[1-9][\\d]*}" })
	public @ResponseBody Iterable<AlertSettingDto> getAllSettingsForItemPerSitePerSettingId(@PathVariable(value = "settingId", required = true) int settingId,
			@PathVariable(value = "pageNumber", required = false) Optional<Integer> pageNumber,
			@PathVariable(value = "pageSize", required = false) Optional<Integer> pageSize) {

		if (pageNumber.isPresent() && pageSize.isPresent())
			return alertSettingService.getAllSettingsForItemPerSitePerSettingId(settingId, pageNumber.get(), pageSize.get());

		if (pageNumber.isPresent())
			return alertSettingService.getAllSettingsForItemPerSitePerSettingId(settingId, pageNumber.get());

		return alertSettingService.getAllSettingsForItemPerSitePerSettingId(settingId);
	}

	/**
	 *
	 * @param itemId
	 * @param userId
	 * @param pageNumber
	 * @param pageSize
	 * @return List<AlertSettingDto>
	 */
	@ApiOperation("Returns list of AlertSettingDto for item per site ")
	@GetMapping(path = { "/forItemPerSite/{itemId:[\\d]+}/{userId:[\\d]+}", "/forItemPerSite/{itemId:[\\d]+}/{userId:[\\d]+}/{pageNumber:[1-9][\\d]*}",
	"/forItemPerSite/{itemId:[\\d]+}/{userId:[\\d]+}/{pageNumber:[1-9][\\d]*}/{pageSize:[1-9][\\d]*}" })
	public @ResponseBody Iterable<AlertSettingDto> getAllSettingsForItemPerSitePerItemId(@PathVariable(value = "itemId", required = true) int itemId,
			@PathVariable(value = "userId", required = true) int userId,
			@PathVariable(value = "pageNumber", required = false) Optional<Integer> pageNumber,
			@PathVariable(value = "pageSize", required = false) Optional<Integer> pageSize) {

		if (pageNumber.isPresent() && pageSize.isPresent())
			return alertSettingService.getAllSettingsForItemPerSitePerItemId(itemId,userId, pageNumber.get(), pageSize.get());

		if (pageNumber.isPresent())
			return alertSettingService.getAllSettingsForItemPerSitePerItemId(itemId,userId, pageNumber.get());

		return alertSettingService.getAllSettingsForItemPerSitePerItemId(itemId,userId);
	}



	@GetMapping(path = "checkPermission/{settingId}/{userId:[\\d]+}")
	@ApiOperation(value = "check permission of the user,if he's authorized to perform delete and edit operations or not")
	public @ResponseBody ResponseEntity<Boolean> checkPermission(
			@ApiParam(value = "setting id and user id to get the setting and check the permission of the user", required = true) @PathVariable(value = "settingId", required = true) Integer settingId,
			@PathVariable(value = "userId", required = true) Integer userId) {
		logger.debug("checkPermission called with settingId:  {} ", settingId);
		boolean isAuthorized = alertSettingService.checkPermission(settingId,userId);
		return new ResponseEntity<Boolean>(isAuthorized, HttpStatus.OK);
	}

	@GetMapping(path = "IsUserCompanyOrSiteAdmin/{userId:[\\d]+}")
	@ApiOperation(value = "checks whether this user is company or site admin ")
	public @ResponseBody ResponseEntity<Boolean> isUserCompanyOrSiteAdmin(
			@ApiParam(value = "user id checks whether this user is company or site admin", required = true)
			@PathVariable(value = "userId", required = true) Integer userId) {
		logger.debug("isUserCompanyOrSiteAdmin called with userId:  {} ", userId);
		Optional<User> user = userRepository.findById(userId);
		boolean isAuthorized = false;

		if(user.isPresent()){
			 isAuthorized = alertSettingService.isDelegateUser(user.get().getSe2UserType());
		}
		return new ResponseEntity<Boolean>(isAuthorized, HttpStatus.OK);
	}

	@ApiOperation(value = "check site contains acl or not")
	@ApiImplicitParam(name = "userId", required = true, dataTypeClass = Integer.class)
	@ApiResponse(code = 200, message = "checkSiteACL is Done", response = ResponseEntity.class)
	@GetMapping(path = "IsSiteACL/{userId:[\\d]+}")
	public @ResponseBody ResponseEntity<Boolean> checkSiteACL(
			@PathVariable(value = "userId", required = true) Integer userId) {
		Optional<User> user = userRepository.findById(userId);
		boolean isSiteHasACL = false;
		if(user.isPresent()){
			logger.debug("checkSiteACL called with siteId: {}", user.get().getSiteId());
			isSiteHasACL=alertSettingService.checkSiteACL(user.get().getSiteId());
		}
		return new ResponseEntity<Boolean>(isSiteHasACL, HttpStatus.OK);

	}

	@GetMapping("/threatLevels")
	@ApiOperation(value = "Get all Threat Level Values from DB")
	@ApiImplicitParam(name = "access_token", required = true,paramType = "header",dataTypeClass = String.class)
	@ApiResponse(code= 200 ,message = "Get All threat level values",response = ThreatLevelValue.class)
	public @ResponseBody ResponseEntity<Iterable<ThreatLevelValue>> getAllThreatLeveLValues(){
		return ResponseEntity.ok(threatLevelValueService.findAll());
	}

	@GetMapping("/distributors")
	@ApiOperation(value = "Get all Distributors list from DB")
	@ApiImplicitParam(name = "access_token", required = true,paramType = "header",dataTypeClass = String.class)
	@ApiResponse(code= 200 ,message = "Get All threat level values",response = ThreatLevelValue.class)
	public @ResponseBody ResponseEntity<Iterable<Distributors>> getAllDistributors(){
		Iterable<Distributors> distributors = (Iterable<Distributors>)cacheService.fetchFromCache(CACHED_SETTINGS_DISTRIBUTORS, CACHED_SETTINGS_DISTRIBUTORS);
		if (distributors != null) {
			logger.info("getting distributors from cache..");
			return ResponseEntity.ok(distributors);
		} else {
			logger.info("getting distributors from DB..");
			distributors = distributorsService.getAllDistributors();
			cacheService.putInCache(CACHED_SETTINGS_DISTRIBUTORS , CACHED_SETTINGS_DISTRIBUTORS , distributors , 4320);
			return ResponseEntity.ok(distributors);
		}
	}

}