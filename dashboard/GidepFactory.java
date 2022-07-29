package com.se2.alert.dashboard;

import com.se2.alert.dto.AlertDashboardDetailDto;
import com.se2.alert.dto.AlertDashboardDto;
import com.se2.alert.dto.change.Change;
import com.se2.alert.dto.change.GidepChange;
import com.se2.alert.entity.*;
import com.se2.alert.service.impl.StaticLookups;

import static com.se2.alert.utility.Utility.validate;

import java.util.List;

public class GidepFactory extends DashboardAbstractFactory {

	private static final String GIDEP_NOTE = "Please Contact GIDEP for Document #[%s]";

	@Override
	public AlertDashboardCommon createDashBoard(Change change, AlertSetting alertSetting,
												AlertEmailHistory emailHistory, AlertFeatureCategory alertFeatureCategory, AlertMatchType alertMatchType,List<AlertDashboardCommon> alertDashboardCommonList) {
		AlertDashboardGidep alertDashboardGidep = new AlertDashboardGidep();

		super.addCommonChangesToCommonDto(alertDashboardGidep, change, alertSetting, emailHistory, alertFeatureCategory,
				alertMatchType);

		GidepChange gidepChange = (GidepChange) change;
		alertDashboardGidep.setDoucumentNumber(validate(gidepChange.getGidepDocumentNum()));
		alertDashboardGidep.setCageCode(validate(gidepChange.getCageCode()));
		alertDashboardGidep.setFeatureTitle(getGidepSubFeatures(gidepChange));
		alertDashboardGidep.setSourceUrl(String.format(GIDEP_NOTE,  gidepChange.getGidepDocumentNum()));
		return alertDashboardGidep;
	}

	@Override
	public AlertDashboardDto getDashboardDto(AlertDashboardCommon alertDashboardCommon, long totalCount) {
		AlertDashboardDto alertDashboardDto = super.getDashboardDto(alertDashboardCommon, totalCount);
		alertDashboardDto.setFeatureName(String.format("GIDEP [%s]",alertDashboardCommon.getFeatureTitle()));
		return alertDashboardDto;
	}

	@Override
	public String createDescription(AlertDashboardCommon alertDashboardCommon) {
		AlertDashboardGidep alertDashboardGidep = (AlertDashboardGidep) alertDashboardCommon;

		StringBuffer description = new StringBuffer();
		description.append(String.format("GIDEP Document #:[%s] ",alertDashboardGidep.getDoucumentNumber()));
		description.append("\n");
		description.append("Cage Code: " + alertDashboardGidep.getCageCode());

		return description.toString();
	}

	@Override
	public AlertDashboardDetailDto getDetailDashBoardDtoForDownload(AlertDashboardCommon alertDashboardCommon) {
		AlertDashboardDetailDto alertDashboardDetailDto = getDetailDashBoardDto(alertDashboardCommon);
		alertDashboardDetailDto.getAlertDashboardDto().setFeatureName("GIDEP");
		alertDashboardDetailDto.getAlertDashboardDto().setSubCategoryName(alertDashboardCommon.getFeatureTitle());
		return alertDashboardDetailDto;
	}

	public static String getGidepSubFeatures(GidepChange gidepChange) {
		return StaticLookups.getGidepFeatureTypeByTypeId(gidepChange.getTypeId()) + " , "
				+ StaticLookups.getGidepDocTypeByTypeDocId(gidepChange.getDocTypeId());
	}
}