package com.se2.alert.dashboard;

import com.se2.alert.dto.AlertDashboardDetailDto;
import com.se2.alert.dto.change.Change;
import com.se2.alert.dto.change.DataSheetChange;
import com.se2.alert.entity.*;

import static com.se2.alert.utility.Utility.validate;

import java.util.List;

public class DataSheetFactory extends DashboardAbstractFactory {

	@Override
	public AlertDashboardCommon createDashBoard(Change change, AlertSetting alertSetting, AlertEmailHistory emailHistory,
												AlertFeatureCategory alertFeatureCategory, AlertMatchType alertMatchType,List<AlertDashboardCommon> alertDashboardCommonList) {
		AlertDashboardDatasheet alertDashboardDatasheet = new AlertDashboardDatasheet();

		super.addCommonChangesToCommonDto(alertDashboardDatasheet, change, alertSetting, emailHistory, alertFeatureCategory,
				alertMatchType);

		DataSheetChange dataSheetChange = (DataSheetChange) change;
		alertDashboardDatasheet.setNewSource(validate(dataSheetChange.getNewSource()));
		alertDashboardDatasheet.setOldSource(validate(dataSheetChange.getOldSource()));

		return alertDashboardDatasheet;
	}

	@Override
	public AlertDashboardDetailDto getDetailDashBoardDto(AlertDashboardCommon alertDashboardCommon) {
		AlertDashboardDetailDto alertDashboardDetailDto = super.getDetailDashBoardDto(alertDashboardCommon);

		AlertDashboardDatasheet alertDashboardDatasheet = (AlertDashboardDatasheet) alertDashboardCommon;

		StringBuffer source = new StringBuffer();

		source.append("New Datasheet@" + alertDashboardDatasheet.getNewSource());
		source.append("\n");
		source.append("Old Datasheet@" + alertDashboardDatasheet.getOldSource());

		alertDashboardDetailDto.getAlertDashboardDto().setSourceUrl(source.toString());

		return alertDashboardDetailDto;
	}

	@Override
	public String createDescription(AlertDashboardCommon alertDashboardCommon) {
		return alertDashboardCommon.getDescription();
	}

	@Override
	public AlertDashboardDetailDto getDetailDashBoardDtoForDownload(AlertDashboardCommon alertDashboardCommon) {
		AlertDashboardDetailDto alertDashboardDetailDto = super.getDetailDashBoardDto(alertDashboardCommon);
		AlertDashboardDatasheet alertDashboardDatasheet = (AlertDashboardDatasheet) alertDashboardCommon;

		StringBuffer source = new StringBuffer();
		alertDashboardDetailDto.getAlertDashboardDto().setOldValue(alertDashboardDatasheet.getOldSource());
		alertDashboardDetailDto.getAlertDashboardDto().setNewValue(alertDashboardDatasheet.getNewSource());
		source.append("New Datasheet@" + alertDashboardDatasheet.getNewSource());
		alertDashboardDetailDto.getAlertDashboardDto().setSourceUrl(source.toString());

		return alertDashboardDetailDto;
	}

}
