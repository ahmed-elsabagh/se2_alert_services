package com.se2.alert.dashboard;

import com.se2.alert.dto.AlertDashboardDetailDto;
import com.se2.alert.dto.AlertDashboardDto;
import com.se2.alert.dto.AlertSupplyChainDto;
import com.se2.alert.dto.change.Change;
import com.se2.alert.dto.change.SupplyChainChange;
import com.se2.alert.entity.*;

import static com.se2.alert.utility.Utility.validate;

import java.util.List;

public class SupplyChainFactory extends DashboardAbstractFactory {

	@Override
	public AlertDashboardCommon createDashBoard(Change change, AlertSetting alertSetting,
			AlertEmailHistory emailHistory, AlertFeatureCategory alertFeatureCategory, AlertMatchType alertMatchType,List<AlertDashboardCommon> alertDashboardCommonList) {

		AlertDashboardSupplyChain alertDashboardSupplyChain = new AlertDashboardSupplyChain();

		super.addCommonChangesToCommonDto(alertDashboardSupplyChain, change, alertSetting, emailHistory,
				alertFeatureCategory, alertMatchType);

		SupplyChainChange supplyChainChange = (SupplyChainChange) change;
		alertDashboardSupplyChain.setEventType(validate(supplyChainChange.getEvent_Type()));
		alertDashboardSupplyChain.setEventName(validate(supplyChainChange.getEvent_Name()));
		alertDashboardSupplyChain.setThreat_Level_ID(supplyChainChange.getThreat_Level_Id());
		alertDashboardSupplyChain.setArticle_Title(
				validate(supplyChainChange.getEvent_Name() + " - " + supplyChainChange.getUpdate_Statment()));
		alertDashboardSupplyChain.setImpact_Analysis(validate(supplyChainChange.getImpact_Analysis()));
		alertDashboardSupplyChain.setImpact_Degree(validate(supplyChainChange.getImpact_Degree()));
		alertDashboardSupplyChain
				.setAlert_Notification_Message(validate(supplyChainChange.getAlert_Notification_Message()));
		alertDashboardSupplyChain.setSourceUrl(validate(supplyChainChange.getNews_Source()));
		alertDashboardSupplyChain.setDescription(validate(supplyChainChange.getDescription()));
		alertDashboardSupplyChain.setSupplyChain_Scope(validate(supplyChainChange.getSupplyChain_Scope()));
		if (supplyChainChange.getSupplier_Change_Date() != null) {
			alertDashboardSupplyChain.setSupplyChain_News_Date(supplyChainChange.getSupplier_Change_Date());
		}
		alertDashboardSupplyChain.setImpact_Status(validate(supplyChainChange.getImpact_Status()));
		alertDashboardSupplyChain.setUpdateStatment(validate(supplyChainChange.getUpdate_Statment()));
		if(supplyChainChange.getSupplyChain_Scope().equalsIgnoreCase("Supplier")) {
		alertDashboardSupplyChain.setMatchedManName(validate(supplyChainChange.getSeSupplier()));
		alertDashboardSupplyChain.setUploadedManName(validate(supplyChainChange.getSeSupplier()));
		alertDashboardSupplyChain.setUploadedManId(supplyChainChange.getMatchedManId()); 
		alertDashboardSupplyChain.setMatchedManId(supplyChainChange.getMatchedManId());
		} 
		return alertDashboardSupplyChain;
	}

	@Override
	public String createDescription(AlertDashboardCommon alertDashboardCommon) {
		return alertDashboardCommon.getDescription();
	}

	@Override
	public AlertDashboardDetailDto getDetailDashBoardDtoForDownload(AlertDashboardCommon alertDashboardCommon) {
		AlertDashboardDetailDto alertDashboardDetailDto = getDetailDashBoardDto(alertDashboardCommon);
		alertDashboardDetailDto.getAlertDashboardDto().setFeatureName("SUPPLYCHAIN");
		AlertSupplyChainDto alertSupplyChain=mapAlertSupplyChainToDto(alertDashboardCommon);
		alertDashboardDetailDto.setAlertSupplyChain(alertSupplyChain);
		handelSourceUrl(alertDashboardDetailDto);
        return alertDashboardDetailDto;
	}

	protected AlertDashboardDetailDto handelSourceUrl(AlertDashboardDetailDto alertDashboardDetailDto) {
		if (alertDashboardDetailDto.getAlertDashboardDto().getSourceUrl().startsWith("http"))
		{alertDashboardDetailDto.getAlertDashboardDto().setSourceUrl("Source@"
				+alertDashboardDetailDto.getAlertDashboardDto().getSourceUrl());}
				return alertDashboardDetailDto;
	}

	@Override
	public AlertDashboardDto getDashboardDto(AlertDashboardCommon alertDashboardCommon, long totalCount) {

		AlertDashboardDto alertDashboardDto = super.getDashboardDto(alertDashboardCommon, totalCount);
		AlertSupplyChainDto alertSupplyChain = mapAlertSupplyChainToDto(alertDashboardCommon);
		alertDashboardDto.setFeatureName(alertSupplyChain.getEventName()+alertSupplyChain.getImpactAnalysis());
		handelSourceUrl(alertDashboardDto);
		alertDashboardDto.setAlertSupplyChain(alertSupplyChain);

		return alertDashboardDto;
	}

	public AlertSupplyChainDto mapAlertSupplyChainToDto(AlertDashboardCommon alertDashboardCommon) {
		AlertSupplyChainDto alertSupplyChain = new AlertSupplyChainDto();
		AlertDashboardSupplyChain alertDashboardSupplyChain = (AlertDashboardSupplyChain) alertDashboardCommon;

		alertSupplyChain.setImpactDegree(validate(alertDashboardSupplyChain.getImpact_Degree()));
		alertSupplyChain.setAlertNotificationMessage(validate(alertDashboardSupplyChain.getAlert_Notification_Message()));
		alertSupplyChain.setArticleTitle(validate(alertDashboardSupplyChain.getArticle_Title()));
		alertSupplyChain.setEventName(validate(alertDashboardSupplyChain.getEventName()));
		alertSupplyChain.setEventType(validate(alertDashboardSupplyChain.getEventType()));
		alertSupplyChain.setImpactAnalysis(validate(alertDashboardSupplyChain.getImpact_Analysis()));
		alertSupplyChain.setThreat_Level_Id(alertDashboardSupplyChain.getThreat_Level_ID());
		alertSupplyChain.setImpactStatus(validate(alertDashboardSupplyChain.getImpact_Status()));
		if (alertDashboardSupplyChain.getSupplyChain_News_Date() != null) {
			alertSupplyChain.setNewsDate(alertDashboardSupplyChain.getSupplyChain_News_Date());
		}
		alertSupplyChain.setSupplyChainScope(validate(alertDashboardSupplyChain.getSupplyChain_Scope()));
		alertSupplyChain.setDescription(validate(alertDashboardSupplyChain.getDescription()));
		alertSupplyChain.setUpdateStatment(validate(alertDashboardSupplyChain.getUpdateStatment()));
		alertSupplyChain.setSeSupplier(alertDashboardSupplyChain.getMatchedManName());
		return alertSupplyChain;
	}

	public AlertDashboardDto handelSourceUrl(AlertDashboardDto alertDashboardDto) {
		if (alertDashboardDto.getSourceUrl().startsWith("http"))
		  { alertDashboardDto.setSourceUrl("Source@"
				+alertDashboardDto.getSourceUrl());}
				return alertDashboardDto;
	} 
	
}