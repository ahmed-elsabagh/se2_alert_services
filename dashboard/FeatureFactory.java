package com.se2.alert.dashboard;

import java.util.List;

import com.se2.alert.constant.AlertFeature;
import com.se2.alert.dto.AlertDashboardDetailDto;
import com.se2.alert.dto.AlertDashboardDto;
import com.se2.alert.dto.change.Change;
import com.se2.alert.entity.*;

public class FeatureFactory {

	private static DashboardAbstractFactory getFactory(AlertFeature alertFeature) {
		switch (alertFeature) {
		case PCN:
			return new PcnFactory();
		case DataSheet:
			return new DataSheetFactory();
		case Lifecycle:
			return new LifecycleFactory();
		case ManufacturerAcquisitionChanges:
			return new ManufacturerFactory();
		case REACH:
		case ROHS:
			return new EnvironmentalFactory();
		case GIDEP:
			return new GidepFactory();
		case SUPPLYCHAIN:
		return new SupplyChainFactory();
			case MARKETAVAILABILITY:
			return new MarketAvailabilityFactory();
		default:
			return null;
		}
	}

	public static AlertDashboardCommon createDashBoard(Change change, AlertSetting alertSetting, AlertEmailHistory emailHistory,
													   AlertFeatureCategory alertFeatureCategory, AlertMatchType alertMatchType,List<AlertDashboardCommon> alertDashboardCommonList) {
		DashboardAbstractFactory factory = getFactory(AlertFeature.valueOf(change.getFeatureId()));
		if (factory != null) {
			return factory.createDashBoard(change, alertSetting, emailHistory, alertFeatureCategory, alertMatchType, alertDashboardCommonList);
		}
		return null;
	}

	public static AlertDashboardDto getDashboardDto(AlertDashboardCommon alertDashboardCommon, long totalCount) {
		DashboardAbstractFactory factory = getFactory(
				AlertFeature.valueOf(alertDashboardCommon.getAlertFeatureCategory().getFeatureCategoryId()));
		if (factory != null) {
			return factory.getDashboardDto(alertDashboardCommon, totalCount);
		}
		return null;
	}
	
	public static AlertDashboardDetailDto getDetailDashBoardDto(AlertDashboardCommon alertDashboardCommon) {
		DashboardAbstractFactory factory = getFactory(
				AlertFeature.valueOf(alertDashboardCommon.getAlertFeatureCategory().getFeatureCategoryId()));
		if (factory != null) {
			return factory.getDetailDashBoardDto(alertDashboardCommon);
		}
		return null;
	}
	public static AlertDashboardDetailDto getDetailDashBoardDtoForDownload(AlertDashboardCommon alertDashboardCommon) {
	DashboardAbstractFactory factory = getFactory(
			AlertFeature.valueOf(alertDashboardCommon.getAlertFeatureCategory().getFeatureCategoryId()));
	if (factory != null) {
		return factory.getDetailDashBoardDtoForDownload(alertDashboardCommon);
	}
	return null;
}
}
