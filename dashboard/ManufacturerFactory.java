package com.se2.alert.dashboard;

import com.se2.alert.dto.AlertDashboardDetailDto;
import com.se2.alert.dto.AlertDashboardDto;
import com.se2.alert.dto.change.Change;
import com.se2.alert.dto.change.ManufacturerAcquisitionChange;
import com.se2.alert.entity.*;

import static com.se2.alert.utility.Utility.validate;

import java.util.List;

public class ManufacturerFactory extends DashboardAbstractFactory {

	@Override
	public AlertDashboardCommon createDashBoard(Change change, AlertSetting alertSetting, AlertEmailHistory emailHistory,
												AlertFeatureCategory alertFeatureCategory, AlertMatchType alertMatchType,List<AlertDashboardCommon> alertDashboardCommonList) {
		AlertDashboardManufacture alertDashboardManufacture = new AlertDashboardManufacture();

		super.addCommonChangesToCommonDto(alertDashboardManufacture, change, alertSetting, emailHistory, alertFeatureCategory,
				alertMatchType);

		ManufacturerAcquisitionChange manufacturerAcquisitionChange = (ManufacturerAcquisitionChange) change;
		alertDashboardManufacture.setAcqType(validate(manufacturerAcquisitionChange.getAcquisitionType()));
		alertDashboardManufacture.setSellerName(validate(manufacturerAcquisitionChange.getSellerName()));
		alertDashboardManufacture.setBuyerName(validate(manufacturerAcquisitionChange.getBuyerName()));
		alertDashboardManufacture.setSourceUrl(validate(manufacturerAcquisitionChange.getSource()));
		alertDashboardManufacture.setMatchedManName(validate(manufacturerAcquisitionChange.getBuyerName()));
		alertDashboardManufacture.setMatchedManId(manufacturerAcquisitionChange.getBuyerId());
		alertDashboardManufacture.setDescription(manufacturerAcquisitionChange.getDescription());
		return alertDashboardManufacture;
	}

	@Override
	public AlertDashboardDetailDto getDetailDashBoardDto(AlertDashboardCommon alertDashboardCommon) {

		AlertDashboardDetailDto alertDashboardDetailDto = super.getDetailDashBoardDto(alertDashboardCommon);

		AlertDashboardManufacture alertDashboardManufacture = (AlertDashboardManufacture) alertDashboardCommon;

		String acqType = alertDashboardManufacture.getAcqType();

		acqType = acqType == null ? "" : acqType.substring(0, acqType.indexOf(" "));

		alertDashboardDetailDto.getAlertDashboardDto()
				.setFeatureName(acqType + " " + alertDashboardDetailDto.getAlertDashboardDto().getFeatureName());
		alertDashboardDetailDto.getAlertDashboardDto().setSellerName(alertDashboardManufacture.getSellerName());
		alertDashboardDetailDto.getAlertDashboardDto().setBuyerName(alertDashboardManufacture.getBuyerName());
		if (alertDashboardCommon.getSourceUrl() != null && !alertDashboardCommon.getSourceUrl().isEmpty())
			if (alertDashboardDetailDto.getAlertDashboardDto().getSourceUrl().startsWith("http"))
				alertDashboardDetailDto.getAlertDashboardDto()
						.setSourceUrl("Source@" + alertDashboardDetailDto.getAlertDashboardDto().getSourceUrl());
			else
				alertDashboardDetailDto.getAlertDashboardDto().setSourceUrl(alertDashboardDetailDto.getAlertDashboardDto().getSourceUrl());
		return alertDashboardDetailDto;
	}

	@Override
	public String createDescription(AlertDashboardCommon alertDashboardCommon) {
		AlertDashboardManufacture alertDashboardManufacture = (AlertDashboardManufacture) alertDashboardCommon;
		StringBuffer description = new StringBuffer();
		if (alertDashboardManufacture.getDescription() != null && !alertDashboardManufacture.getDescription().isEmpty()) {
			description.append("Details: " + alertDashboardManufacture.getDescription());
		}
		return description.toString();
	}

	public AlertDashboardDto getDashboardDto(AlertDashboardCommon alertDashboardCommon, long totalCount) {
		AlertDashboardDto alertDashboardDto = super.getDashboardDto(alertDashboardCommon, totalCount);

		AlertDashboardManufacture alertDashboardManufacture = (AlertDashboardManufacture) alertDashboardCommon;
		alertDashboardDto.setSellerName(alertDashboardManufacture.getSellerName());
		alertDashboardDto.setBuyerName(alertDashboardManufacture.getBuyerName());
		return alertDashboardDto;
	}

	@Override
	public AlertDashboardDetailDto getDetailDashBoardDtoForDownload(AlertDashboardCommon alertDashboardCommon) {
		AlertDashboardDetailDto alertDashboardDetailDto = getDetailDashBoardDto(alertDashboardCommon);
		alertDashboardDetailDto.getAlertDashboardDto().setSubCategoryName(alertDashboardDetailDto.getAlertDashboardDto().getFeatureName());
		alertDashboardDetailDto.getAlertDashboardDto().setFeatureName("Manufacturer Acquisition");
		return alertDashboardDetailDto;
	}
}
