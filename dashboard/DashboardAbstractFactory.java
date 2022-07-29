package com.se2.alert.dashboard;

import java.util.List;

import com.google.gson.Gson;
import com.se2.alert.dto.*;
import com.se2.alert.dto.change.Change;
import com.se2.alert.entity.*;
import com.se2.alert.scope.ScopeFactory;
import com.se2.alert.utility.DateUtility;
import com.se2.alert.utility.GeneratingURLsUtility;

public abstract class DashboardAbstractFactory {
	public abstract AlertDashboardCommon createDashBoard(Change change, AlertSetting alertSetting,
															AlertEmailHistory emailHistory, AlertFeatureCategory alertFeatureCategory, AlertMatchType alertMatchType,List<AlertDashboardCommon> alertDashboardCommonList);

	public abstract String createDescription(AlertDashboardCommon alertDashboardCommon);

	protected abstract AlertDashboardDetailDto getDetailDashBoardDtoForDownload(
			AlertDashboardCommon alertDashboardCommon);

	private ScopeFactory scopeFactory = new ScopeFactory();

	protected void addCommonChangesToCommonDto(AlertDashboardCommon alertDashboardCommon, Change change,
			AlertSetting alertSetting, AlertEmailHistory emailHistory, AlertFeatureCategory alertFeatureCategory,
			AlertMatchType alertMatchType) {
		IssuePriority issuePriority=new IssuePriority();
		alertDashboardCommon.setAlertSetting(alertSetting);
		alertDashboardCommon.setAlertEmailHistory(emailHistory);

		alertDashboardCommon.setFeatureTitle(alertFeatureCategory.getFeatureCategoryNameSingular());
		alertDashboardCommon.setAlertFeatureCategory(alertFeatureCategory);

		alertDashboardCommon.setMatchType(alertMatchType);

		alertDashboardCommon.setUploadedManId(change.getUploadedManId());
		alertDashboardCommon.setMatchedManId(change.getMatchedManId());
		alertDashboardCommon.setUploadedComId(change.getUploadedComId());
		alertDashboardCommon.setMatchedComId(change.getMatchedComId());

		alertDashboardCommon.setUploadedPart(change.getUploadedMPN());
		alertDashboardCommon.setMatchedPart(change.getMatchedMPN());
		alertDashboardCommon.setUploadedManName(change.getUploadedManName());
		alertDashboardCommon.setMatchedManName(change.getMatchedManName());
		alertDashboardCommon.setItemsNames(change.getItemsNames());
		alertDashboardCommon.setUploadedCPN(change.getUploadedCPN());
		issuePriority.setIssuePriorityId(1);
		alertDashboardCommon.setIssuePriorities(issuePriority);

	}

	public AlertDashboardDto getDashboardDto(AlertDashboardCommon alertDashboardCommon, long totalCount) {
		AlertDashboardDto alertDashboardDto = new AlertDashboardDto();
        alertDashboardDto.setId(alertDashboardCommon.getId());
        alertDashboardDto.setTotalCount(totalCount);
        alertDashboardDto.setFeatureName(alertDashboardCommon.getFeatureTitle());

		if (alertDashboardCommon.getAlertFeatureCategory() != null)
			alertDashboardDto.setFeatureId(alertDashboardCommon.getAlertFeatureCategory().getFeatureCategoryId());

		alertDashboardDto.setDescription(createDescription(alertDashboardCommon));

		alertDashboardDto.setMatchedPart(alertDashboardCommon.getMatchedPart());
		alertDashboardDto.setMatchedMan(alertDashboardCommon.getMatchedManName());

		if (alertDashboardCommon.getMatchType() != null)
			alertDashboardDto.setMatchStatus(alertDashboardCommon.getMatchType().getMatchShortcut());

		if (alertDashboardCommon.getSourceUrl() != null )
			alertDashboardDto.setSourceUrl(alertDashboardCommon.getSourceUrl());

		alertDashboardDto.setSentDate(DateUtility.getStrFromDate(
				alertDashboardCommon.getAlertEmailHistory().getInsertDate(), DateUtility.MMM_d_yyyy_DATE_FORMAT));

		alertDashboardDto.setSettingOnName(alertDashboardCommon.getAlertSetting().getAlertSettingNameWithScope());

		alertDashboardDto.setSettingName(alertDashboardCommon.getAlertSetting().getAlertSettingName());

		alertDashboardDto.setSettingScope(alertDashboardCommon.getAlertSetting().getAlertScope().getName());

		alertDashboardDto.setFlagged(alertDashboardCommon.getIsFlag());
		alertDashboardDto.setArchived(alertDashboardCommon.getIsArchive());

		if (alertDashboardCommon.getAlertFeatureCategory().getFeatureCategoryId()==9){
		alertDashboardDto.setSubCategoryName(alertDashboardCommon.getAlertFeatureCategory().getAlertFeatureSubCategories().get(0).getSubCategoryName());}
		alertDashboardDto.setIssueStatus(IssueStatusToDTO(alertDashboardCommon.getIssueStatus()));
		alertDashboardDto.setIssuePriority(ToDTO(alertDashboardCommon.getIssuePriorities()));

		if(alertDashboardCommon.getAssignees()!=null&& !alertDashboardCommon.getAssignees().isEmpty()){
			for(User assignee: alertDashboardCommon.getAssignees()){
				alertDashboardDto.addAssignee(assignee);
			}

		}

		return alertDashboardDto;
	}
	
	protected IssueSieStatusListDto IssueStatusToDTO(IssueStatus issueStatus){
		IssueSieStatusListDto issueSieStatusListDto=new IssueSieStatusListDto();
		issueSieStatusListDto.setIssueStatusId(issueStatus.getStatusId());
		issueSieStatusListDto.setIssueStatusName(issueStatus.getStatusName());
		return issueSieStatusListDto;
	}

	protected IssueSitePriorityListDto ToDTO(IssuePriority issuePriorities){
		IssueSitePriorityListDto issueSitePriorityListDto=new IssueSitePriorityListDto();
		issueSitePriorityListDto.setPriorityId(issuePriorities.getIssuePriorityId());
		issueSitePriorityListDto.setPriorityName(issuePriorities.getPriorityName());
		return issueSitePriorityListDto;
	}

	protected AlertDashboardDetailDto getDetailDashBoardDto(AlertDashboardCommon alertDashboardCommon) {
		AlertDashboardDetailDto alertDashboardDetailDto = new AlertDashboardDetailDto();
		alertDashboardDetailDto.setAlertDashboardDto(getDashboardDto(alertDashboardCommon, 0l));

		if (alertDashboardCommon.getMatchedPart() != null) {
			String partUrl = alertDashboardCommon.getMatchedPart() + "@"
					+ GeneratingURLsUtility.generatePartDetailURL(alertDashboardCommon.getMatchedPart(),
							alertDashboardCommon.getMatchedManName(), alertDashboardCommon.getMatchedComId());
			alertDashboardDetailDto.getAlertDashboardDto().setMatchedPart(partUrl);
		}

		if (alertDashboardCommon.getMatchedManName() != null) {
			String manUrl = alertDashboardCommon.getMatchedManName() + "@"
					+ GeneratingURLsUtility.generateManufacturerURL(alertDashboardCommon.getMatchedManName(),
							alertDashboardCommon.getMatchedManId());
			alertDashboardDetailDto.getAlertDashboardDto().setMatchedMan(manUrl);
		}

		if (alertDashboardCommon.getUploadedPart() != null) {
			String partUrl = alertDashboardCommon.getUploadedPart() + "@"
					+ GeneratingURLsUtility.generatePartDetailURL(alertDashboardCommon.getUploadedPart(),
							alertDashboardCommon.getUploadedManName(), alertDashboardCommon.getUploadedComId());
			alertDashboardDetailDto.setUploadedPart(partUrl);
		}

		if (alertDashboardCommon.getUploadedManName() != null) {
			String manUrl = alertDashboardCommon.getUploadedManName() + "@"
					+ GeneratingURLsUtility.generateManufacturerURL(alertDashboardCommon.getUploadedManName(),
							alertDashboardCommon.getUploadedManId());
			alertDashboardDetailDto.setUploadedMan(manUrl);
		}

		handelAlertDashBoardDetailDtoAlertSettingData(alertDashboardCommon, alertDashboardDetailDto);

		alertDashboardDetailDto.setUploadedCPN(alertDashboardCommon.getUploadedCPN());

		alertDashboardDetailDto.setUploadedManId(alertDashboardCommon.getUploadedManId());
		alertDashboardDetailDto.setMatchedManId(alertDashboardCommon.getMatchedManId());

		alertDashboardDetailDto.setUploadedComId(alertDashboardCommon.getUploadedComId());
		alertDashboardDetailDto.setMatchedComId(alertDashboardCommon.getMatchedComId());

		return alertDashboardDetailDto;
	}

	private void handelAlertDashBoardDetailDtoAlertSettingData(AlertDashboardCommon alertDashboardCommon,
			AlertDashboardDetailDto alertDashboardDetailDto) {
		if (alertDashboardCommon.getAlertSetting() != null) {
			alertDashboardDetailDto.getAlertDashboardDto()
			.setSettingOnName(scopeFactory.getSettingNameWithLink(alertDashboardCommon.getAlertSetting()));

			alertDashboardDetailDto.setSettingDeleted(alertDashboardCommon.getAlertSetting().getIsDeleted());

			alertDashboardDetailDto.setSettingId(alertDashboardCommon.getAlertSetting().getAlertSettingId());
			if (alertDashboardCommon.getItemsNames() != null && !alertDashboardCommon.getItemsNames().isEmpty()) {
				AlertSettingItems alertSettingItems = new Gson().fromJson(alertDashboardCommon.getItemsNames(),
						AlertSettingItems.class);
				alertDashboardDetailDto.getAlertDashboardDto().setAlertSettingItems(alertSettingItems);

			} else {
				alertDashboardDetailDto.getAlertDashboardDto().setAlertSettingItems(
						scopeFactory.getAlertSettingItems(alertDashboardCommon.getAlertSetting()));
			}

		}
	}

}
