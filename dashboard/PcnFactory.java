package com.se2.alert.dashboard;

import com.se2.alert.dto.AlertDashboardDetailDto;
import com.se2.alert.dto.AlertDashboardDto;
import com.se2.alert.dto.change.Change;
import com.se2.alert.dto.change.PCNChange;
import com.se2.alert.entity.*;
import com.se2.alert.service.impl.StaticLookups;
import com.se2.alert.utility.DateUtility;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.se2.alert.utility.Utility.convertToStrCommaSeparated;
import static com.se2.alert.utility.Utility.validate;

public class PcnFactory extends DashboardAbstractFactory {

	@Override
	public AlertDashboardCommon createDashBoard(Change change, AlertSetting alertSetting,
												AlertEmailHistory emailHistory, AlertFeatureCategory alertFeatureCategory, AlertMatchType alertMatchType,List<AlertDashboardCommon> alertDashboardCommonList) {
		AlertDashboardPcn alertDashboardPcn = new AlertDashboardPcn();

		super.addCommonChangesToCommonDto(alertDashboardPcn, change, alertSetting, emailHistory, alertFeatureCategory,
				alertMatchType);

		PCNChange pcnChange = (PCNChange) change;
		alertDashboardPcn.setSourceUrl(validate(pcnChange.getPcnUrl()));
		alertDashboardPcn.setDescription(validate(pcnChange.getDescriptionOfChange()));
		alertDashboardPcn.setEffictiveDate(pcnChange.getEffectiveDate());
		alertDashboardPcn.setNotificationDate(pcnChange.getNotificationDate());
		alertDashboardPcn.setPcnNumber(pcnChange.getPcnNumber());
		alertDashboardPcn.setFeatureTitle(convertToStrCommaSeparated(pcnChange.getTypeOfChanges()));
		alertDashboardPcn.setPcnId(pcnChange.getPcnId());
		alertDashboardPcn.setLastTimeBuyDate(pcnChange.getLastTimeBuyDate());
		alertDashboardPcn.setLastShipDate(pcnChange.getLastShipDate());
		return alertDashboardPcn;
	}

	@Override
	public AlertDashboardDto getDashboardDto(AlertDashboardCommon alertDashboardCommon, long totalCount) {
		AlertDashboardDto alertDashboardDto = super.getDashboardDto(alertDashboardCommon, totalCount);
		alertDashboardDto.setSubCategoryName(getSubCategoryName(alertDashboardCommon.getFeatureTitle()));
		return alertDashboardDto;
	}

	@Override
	public AlertDashboardDetailDto getDetailDashBoardDto(AlertDashboardCommon alertDashboardCommon) {

		AlertDashboardDetailDto alertDashboardDetailDto = super.getDetailDashBoardDto(alertDashboardCommon);

		AlertDashboardPcn alertDashboardPcn = (AlertDashboardPcn) alertDashboardCommon;

		String featuresCommaSeparated = alertDashboardCommon.getFeatureTitle();
		StringBuilder features = new StringBuilder();
		if (featuresCommaSeparated != null && !featuresCommaSeparated.isEmpty()) {
			String[] featuresArr = featuresCommaSeparated.split(",");
			for (String feature : featuresArr)
				features.append("PCN: " + feature + "\n");
		}
		alertDashboardDetailDto.getAlertDashboardDto().setFeatureName(features.toString());

		StringBuilder sourceUrl = handelSourceUrl(alertDashboardPcn);

		alertDashboardDetailDto.getAlertDashboardDto().setSourceUrl(sourceUrl.toString());

		alertDashboardDetailDto.getAlertDashboardDto().setPcnNumber(alertDashboardPcn.getPcnNumber());

		if (alertDashboardPcn.getLastTimeBuyDate() != null) {

			alertDashboardDetailDto.getAlertDashboardDto().setLtbDate(
					DateUtility.getStrFromDate(alertDashboardPcn.getLastTimeBuyDate(), DateUtility.MMM_d_yyyy_DATE_FORMAT));
		}

		if (alertDashboardPcn.getLastShipDate() != null) {

			alertDashboardDetailDto.getAlertDashboardDto().setLastShipDate(
					DateUtility.getStrFromDate(alertDashboardPcn.getLastShipDate(), DateUtility.MMM_d_yyyy_DATE_FORMAT));
		}

		if (alertDashboardPcn.getEffictiveDate() != null)
			alertDashboardDetailDto.setEffictiveDate(DateUtility.getStrFromDate(alertDashboardPcn.getEffictiveDate(),
					DateUtility.MMM_d_yyyy_DATE_FORMAT));

		if (alertDashboardPcn.getNotificationDate() != null)
			alertDashboardDetailDto.setIssuedDate(DateUtility.getStrFromDate(alertDashboardPcn.getNotificationDate(),
					DateUtility.MMM_d_yyyy_DATE_FORMAT));

		return alertDashboardDetailDto;
	}

	private StringBuilder handelSourceUrl(AlertDashboardPcn alertDashboardPcn) {
		String pcnUrl = alertDashboardPcn.getSourceUrl();
		StringBuilder sourceUrl = new StringBuilder();
		if (alertDashboardPcn.getPcnNumber() == null || alertDashboardPcn.getPcnNumber().isEmpty()) {
			if (pcnUrl != null && pcnUrl.startsWith("http"))
				sourceUrl.append(pcnUrl.substring(pcnUrl.lastIndexOf("/") + 1) + "@" + pcnUrl);
			else
				sourceUrl.append(pcnUrl);
		} else {
			if (pcnUrl != null && pcnUrl.startsWith("http"))
				sourceUrl.append("PCN# " + alertDashboardPcn.getPcnNumber() + "@" + pcnUrl);
			else
				sourceUrl.append("PCN# " + alertDashboardPcn.getPcnNumber() + "(" + pcnUrl + ")");
		}
		return sourceUrl;
	}

	@Override
	public String createDescription(AlertDashboardCommon alertDashboardCommon) {
		return alertDashboardCommon.getDescription();
	}

	@Override
	protected AlertDashboardDetailDto getDetailDashBoardDtoForDownload(AlertDashboardCommon alertDashboardCommon) {
		AlertDashboardDetailDto alertDashboardDetailDto = getDetailDashBoardDto(alertDashboardCommon);

		alertDashboardDetailDto.getAlertDashboardDto().setFeatureName("PCN");
		alertDashboardDetailDto.getAlertDashboardDto().setSubCategoryName(alertDashboardCommon.getFeatureTitle());

		return alertDashboardDetailDto;
	}

	private static String getSubCategoryName(String featureTitle) {
		Set<String> subCategoryNamesSet = new HashSet<>();
		if (featureTitle != null && !featureTitle.isEmpty()) {
			String[] subFeatures = featureTitle.split(",");
			if (subFeatures != null && subFeatures.length > 0)
				subCategoryNamesSet.add(StaticLookups.getPCNSubCategoryBySubFeatureName(subFeatures[0].trim()));
		}

		StringBuilder subCategoryNames = new StringBuilder();
		String comma = "";
		for (String subCategoryName : subCategoryNamesSet) {
			subCategoryNames.append(comma + subCategoryName);
			comma = ",";
		}
		return subCategoryNames.toString();
	}
}
