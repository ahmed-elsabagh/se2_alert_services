package com.se2.alert.dashboard;

import com.se2.alert.dto.AlertDashboardDetailDto;
import com.se2.alert.dto.change.Change;
import com.se2.alert.dto.change.EnvironmentalChange;
import com.se2.alert.entity.*;

import static com.se2.alert.utility.Utility.validate;

import java.util.List;

public class EnvironmentalFactory extends DashboardAbstractFactory {

	@Override
	public AlertDashboardCommon createDashBoard(Change change, AlertSetting alertSetting, AlertEmailHistory emailHistory,
												AlertFeatureCategory alertFeatureCategory, AlertMatchType alertMatchType,List<AlertDashboardCommon> alertDashboardCommonList) {
		AlertDashboardEnvironmental alertDashboardEnvironmental = new AlertDashboardEnvironmental();

		super.addCommonChangesToCommonDto(alertDashboardEnvironmental, change, alertSetting, emailHistory, alertFeatureCategory,
				alertMatchType);

		EnvironmentalChange environmentalChange = (EnvironmentalChange) change;
		alertDashboardEnvironmental.setSourceUrl(validate(environmentalChange.getSource()));
		alertDashboardEnvironmental.setNewValue(validate(environmentalChange.getNewValue()));
		alertDashboardEnvironmental.setOldValue(validate(environmentalChange.getOldValue()));
		return alertDashboardEnvironmental;
	}

	@Override
	public AlertDashboardDetailDto getDetailDashBoardDto(AlertDashboardCommon alertDashboardCommon) {

		AlertDashboardDetailDto alertDashboardDetailDto = super.getDetailDashBoardDto(alertDashboardCommon);

		if (alertDashboardCommon.getSourceUrl() != null && !alertDashboardCommon.getSourceUrl().isEmpty()) {
			if (alertDashboardDetailDto.getAlertDashboardDto().getSourceUrl().startsWith("http")) {
				alertDashboardDetailDto.getAlertDashboardDto()
						.setSourceUrl("Source@" + alertDashboardDetailDto.getAlertDashboardDto().getSourceUrl());
			}
			else {
				alertDashboardDetailDto.getAlertDashboardDto().setSourceUrl(alertDashboardDetailDto.getAlertDashboardDto().getSourceUrl());
			}
		}
		AlertDashboardEnvironmental alertDashboardEnvironmental = (AlertDashboardEnvironmental) alertDashboardCommon;
		alertDashboardDetailDto.getAlertDashboardDto().setOldValue(alertDashboardEnvironmental.getOldValue());
		alertDashboardDetailDto.getAlertDashboardDto().setNewValue(alertDashboardEnvironmental.getNewValue());
		return alertDashboardDetailDto;
	}

	@Override
	public String createDescription(AlertDashboardCommon alertDashboardCommon) {

		return alertDashboardCommon.getDescription();
	}

	@Override
	public AlertDashboardDetailDto getDetailDashBoardDtoForDownload(AlertDashboardCommon alertDashboardCommon) {
		return getDetailDashBoardDto(alertDashboardCommon);
	}

}
