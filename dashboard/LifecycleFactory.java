package com.se2.alert.dashboard;

import com.se2.alert.dto.AlertDashboardDetailDto;
import com.se2.alert.dto.AlertDashboardDto;
import com.se2.alert.dto.change.Change;
import com.se2.alert.dto.change.LifeCycleChange;
import com.se2.alert.entity.*;
import com.se2.alert.utility.DateUtility;

import static com.se2.alert.utility.Utility.validate;

import java.util.List;

public class LifecycleFactory extends DashboardAbstractFactory {

	@Override
	public AlertDashboardCommon createDashBoard(Change change, AlertSetting alertSetting, AlertEmailHistory emailHistory,
												AlertFeatureCategory alertFeatureCategory, AlertMatchType alertMatchType,List<AlertDashboardCommon> alertDashboardCommonList) {
		AlertDashboardLifecycle alertDashboardLifecycle = new AlertDashboardLifecycle();

		super.addCommonChangesToCommonDto(alertDashboardLifecycle, change, alertSetting, emailHistory, alertFeatureCategory,
				alertMatchType);

		LifeCycleChange lifeCycleChange = (LifeCycleChange) change;
		alertDashboardLifecycle.setSourceUrl(validate(lifeCycleChange.getSource()));
		alertDashboardLifecycle.setSourceType(validate(lifeCycleChange.getSourceType()));
		alertDashboardLifecycle.setDate(lifeCycleChange.getLifeCycleDate());
		alertDashboardLifecycle.setNewValue(validate(lifeCycleChange.getNewValue()));
		alertDashboardLifecycle.setOldValue(validate(lifeCycleChange.getOldValue()));
		alertDashboardLifecycle.setReason(validate(lifeCycleChange.getReason()));
		alertDashboardLifecycle.setExceptionalComment(validate(lifeCycleChange.getExceptionalComment()));
		return alertDashboardLifecycle;
	}

	@Override
	public AlertDashboardDetailDto getDetailDashBoardDto(AlertDashboardCommon alertDashboardCommon) {

		AlertDashboardDetailDto alertDashboardDetailDto = super.getDetailDashBoardDto(alertDashboardCommon);

		AlertDashboardLifecycle alertDashboardLifecycle = (AlertDashboardLifecycle) alertDashboardCommon;

		validateAlertDashboardLifecycle(alertDashboardCommon, alertDashboardDetailDto, alertDashboardLifecycle);

		validateDashboardDirectFeed(alertDashboardCommon, alertDashboardDetailDto);

		validateExceptionalComment(alertDashboardDetailDto, alertDashboardLifecycle);

		alertDashboardDetailDto.getAlertDashboardDto().setNewValue(alertDashboardLifecycle.getNewValue());

		alertDashboardDetailDto.getAlertDashboardDto().setOldValue(alertDashboardLifecycle.getOldValue());

		if (alertDashboardLifecycle.getDate() != null) {

			alertDashboardDetailDto.getAlertDashboardDto().setLtbDate(
					DateUtility.getStrFromDate(alertDashboardLifecycle.getDate(), DateUtility.MMM_d_yyyy_DATE_FORMAT));
		}

		return alertDashboardDetailDto;
	}

	public void validateExceptionalComment(AlertDashboardDetailDto alertDashboardDetailDto,
			AlertDashboardLifecycle alertDashboardLifecycle) {
		if(alertDashboardLifecycle.getExceptionalComment() !=null && !alertDashboardLifecycle.getExceptionalComment().isEmpty())
			alertDashboardDetailDto.getAlertDashboardDto().setExceptionalComment(alertDashboardLifecycle.getExceptionalComment());
	}

	public void validateDashboardDirectFeed(AlertDashboardCommon alertDashboardCommon,
			AlertDashboardDetailDto alertDashboardDetailDto) {
		if (alertDashboardCommon.getSourceUrl() != null && alertDashboardCommon.getSourceUrl().equalsIgnoreCase("Direct Feed"))
			alertDashboardDetailDto.getAlertDashboardDto().setSourceUrl(alertDashboardDetailDto.getAlertDashboardDto().getSourceUrl());
	}

	public void validateAlertDashboardLifecycle(AlertDashboardCommon alertDashboardCommon,
			AlertDashboardDetailDto alertDashboardDetailDto, AlertDashboardLifecycle alertDashboardLifecycle) {
		if (alertDashboardLifecycle.getSourceType() != null && alertDashboardCommon.getSourceUrl() != null
				&& !alertDashboardCommon.getSourceUrl().equalsIgnoreCase("Direct Feed")) {
			StringBuffer source = new StringBuffer();

			validateSourceType(alertDashboardCommon, alertDashboardLifecycle, source);

			alertDashboardDetailDto.getAlertDashboardDto().setSourceUrl(source.toString());
		}
	}

	public void validateSourceType(AlertDashboardCommon alertDashboardCommon,
			AlertDashboardLifecycle alertDashboardLifecycle, StringBuffer source) {
		if (alertDashboardLifecycle.getSourceType().isEmpty())
			source.append("Source" + "@" + alertDashboardCommon.getSourceUrl());
		else
			source.append(alertDashboardLifecycle.getSourceType() + "@" + alertDashboardCommon.getSourceUrl());
	}

	@Override
	public AlertDashboardDto getDashboardDto(AlertDashboardCommon alertDashboardCommon, long totalCount) {
		AlertDashboardDto alertDashboardDto = super.getDashboardDto(alertDashboardCommon, totalCount);
		AlertDashboardLifecycle alertDashboardLifecycle = (AlertDashboardLifecycle) alertDashboardCommon;
		alertDashboardDto.setExceptionalComment(alertDashboardLifecycle.getExceptionalComment());
		return alertDashboardDto;
	}

	@Override
	public String createDescription(AlertDashboardCommon alertDashboardCommon) {
		AlertDashboardLifecycle alertDashboardLifecycle = (AlertDashboardLifecycle) alertDashboardCommon;
		StringBuffer description = new StringBuffer();
		if (alertDashboardLifecycle.getReason() != null && !alertDashboardLifecycle.getReason().isEmpty()) {
			description.append("Reason: " + alertDashboardLifecycle.getReason());
		}
		return description.toString();
	}

	@Override
	public AlertDashboardDetailDto getDetailDashBoardDtoForDownload(AlertDashboardCommon alertDashboardCommon) {
		return getDetailDashBoardDto(alertDashboardCommon);
	}
}
