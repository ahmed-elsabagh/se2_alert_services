package com.se2.alert.dashboard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.se2.alert.dto.AlertDashboardDetailDto;
import com.se2.alert.dto.AlertDashboardDto;
import com.se2.alert.dto.AlertDashboardInventoryTimelineDto;
import com.se2.alert.dto.AlertDashboardMADetailsDto;
import com.se2.alert.dto.change.Change;
import com.se2.alert.dto.change.InventoryChange;
import com.se2.alert.entity.AlertDashboardCommon;
import com.se2.alert.entity.AlertDashboardInventory;
import com.se2.alert.entity.AlertDashboardMADetails;
import com.se2.alert.entity.AlertEmailHistory;
import com.se2.alert.entity.AlertFeatureCategory;
import com.se2.alert.entity.AlertMatchType;
import com.se2.alert.entity.AlertSetting;

public class MarketAvailabilityFactory extends DashboardAbstractFactory{
        
	@Override
    public AlertDashboardCommon createDashBoard(Change change, AlertSetting alertSetting, AlertEmailHistory emailHistory, AlertFeatureCategory alertFeatureCategory, AlertMatchType alertMatchType,List<AlertDashboardCommon> alertDashboardCommonList) {
	List<AlertDashboardMADetails> alertDashboardMADetailsList=new ArrayList<AlertDashboardMADetails>();
	AlertDashboardInventory alertDashboardInventorysameObject=null;
	
	try {
		
		if (alertDashboardCommonList!=null &&!alertDashboardCommonList.isEmpty()) {
	       for (int i=0; i <alertDashboardCommonList.size(); i++) { 
	   alertDashboardInventorysameObject=(AlertDashboardInventory)alertDashboardCommonList.get(i); 
	   if ( alertDashboardCommonList.get(i).getAlertSetting().getAlertSettingId()==alertDashboardInventorysameObject.getSameObject())
	    {
		InventoryChange inventoryChange = (InventoryChange) change;
		AlertDashboardMADetails alertDashboardMADetails=new AlertDashboardMADetails();
		alertDashboardMADetails.setDistId(inventoryChange.getDist_Id());
		alertDashboardMADetails.setOldQuantity(inventoryChange.getOld_Quantity());
		alertDashboardMADetails.setNewQuantity(inventoryChange.getNew_Quantity());
        alertDashboardMADetails.setSettFetTypeValue(inventoryChange.getValue());
		alertDashboardMADetails.setFeatureTypeId(inventoryChange.getTypeId());
        alertDashboardMADetails.setDashboard((AlertDashboardInventory)alertDashboardCommonList.get(i));
		alertDashboardInventorysameObject.getDashboard().add(alertDashboardMADetails);
	} 
	        return alertDashboardInventorysameObject;

	       }   
	    
	    }
		else {
			
		AlertDashboardInventory alertDashboardInventory=new AlertDashboardInventory(); 
		super.addCommonChangesToCommonDto(alertDashboardInventory, change, alertSetting, emailHistory,
			alertFeatureCategory, alertMatchType);
	
	InventoryChange inventoryChange = (InventoryChange) change;
	alertDashboardInventory.setInventoryDate(inventoryChange.getInventoryDate());
	AlertDashboardMADetails alertDashboardMADetails=new AlertDashboardMADetails();
	alertDashboardMADetails.setDistId(inventoryChange.getDist_Id().intValue());
	alertDashboardMADetails.setOldQuantity(inventoryChange.getOld_Quantity());
	alertDashboardMADetails.setNewQuantity(inventoryChange.getNew_Quantity());
    alertDashboardMADetails.setSettFetTypeValue(inventoryChange.getValue());
	alertDashboardMADetails.setFeatureTypeId(inventoryChange.getTypeId());
    alertDashboardMADetails.setDashboard(alertDashboardInventory);
	alertDashboardMADetailsList.add(alertDashboardMADetails);
	alertDashboardInventory.setDashboard(alertDashboardMADetailsList);
	alertDashboardInventory.setSameObject(alertSetting.getAlertSettingId());
	    return alertDashboardInventory;

	} 
		}
	 
	catch(Exception e) {
		e.printStackTrace();
	}
	 
	 return null;
}




@Override
public String createDescription(AlertDashboardCommon alertDashboardCommon) {
    return alertDashboardCommon.getDescription();
}

@Override
protected AlertDashboardDetailDto getDetailDashBoardDtoForDownload(AlertDashboardCommon alertDashboardCommon) {
	AlertDashboardDetailDto alertDashboardDetailDto = getDetailDashBoardDto(alertDashboardCommon);
	List<AlertDashboardMADetailsDto> marketAvailabilityDtos = mapAlertInventoryToDto(alertDashboardCommon);
	alertDashboardDetailDto.setAlertInventoryDetails(marketAvailabilityDtos);
	alertDashboardDetailDto.getAlertDashboardDto().setSubCategoryName(alertDashboardCommon.getAlertFeatureCategory().getAlertFeatureSubCategories().get(0).getSubCategoryName());
	return alertDashboardDetailDto;
}

@Override
public AlertDashboardDetailDto getDetailDashBoardDto(AlertDashboardCommon alertDashboardCommon) {
	AlertDashboardDetailDto alertDashboardDetailDto = super.getDetailDashBoardDto(alertDashboardCommon);
	List<AlertDashboardMADetailsDto> marketAvailabilityDtos = mapAlertInventoryToDto(alertDashboardCommon);
	alertDashboardDetailDto.setAlertInventoryDetails(marketAvailabilityDtos);
	alertDashboardDetailDto.getAlertDashboardDto().setSubCategoryName(alertDashboardCommon.getAlertFeatureCategory().getAlertFeatureSubCategories().get(0).getSubCategoryName());
	return alertDashboardDetailDto;
}

	@Override
public AlertDashboardDto getDashboardDto(AlertDashboardCommon alertDashboardCommon, long totalCount) {

	AlertDashboardDto alertDashboardDto = super.getDashboardDto(alertDashboardCommon, totalCount);
	List<AlertDashboardInventoryTimelineDto> inventoryTimelineDtos = mapAlertInventoryTimelineToDto(alertDashboardCommon);
	alertDashboardDto.setAlertInventory(inventoryTimelineDtos);

	return alertDashboardDto;
}

private List<AlertDashboardMADetailsDto> mapAlertInventoryToDto(AlertDashboardCommon alertDashboardCommon) {
	List<AlertDashboardMADetailsDto> alertDashboardMADetailsDtos = new ArrayList<AlertDashboardMADetailsDto>();
	AlertDashboardInventory alertDashboardInventory = (AlertDashboardInventory) alertDashboardCommon;
	for (AlertDashboardMADetails dashboard : alertDashboardInventory.getDashboard()) {
		alertDashboardMADetailsDtos.add(AlertDashboardMADetailsDto.builder()
				.id(dashboard.getId())
				.settingId(alertDashboardInventory.getAlertSetting().getAlertSettingId())
				.settFetTypeValue(dashboard.getSettFetTypeValue())
				.featureTypeId(dashboard.getFeatureTypeId())
				.oldQuantity(dashboard.getOldQuantity())
				.newQuantity(dashboard.getNewQuantity())
				.distId(dashboard.getDistId())
				.changeDate(alertDashboardInventory.getInventoryDate())
				.build());
	}
	return alertDashboardMADetailsDtos;
}
private List<AlertDashboardInventoryTimelineDto> mapAlertInventoryTimelineToDto(AlertDashboardCommon alertDashboardCommon) {
	List<AlertDashboardInventoryTimelineDto> alertDashboardInventoryTimelineDtos = new ArrayList<AlertDashboardInventoryTimelineDto>();
	AlertDashboardInventory alertDashboardInventory = (AlertDashboardInventory) alertDashboardCommon;
	for (AlertDashboardMADetails dashboard : alertDashboardInventory.getDashboard()) {
		alertDashboardInventoryTimelineDtos.add(AlertDashboardInventoryTimelineDto.builder()
				.id(dashboard.getId())
				.featureTypeId(dashboard.getFeatureTypeId())
				.settFetTypeValue(dashboard.getSettFetTypeValue())
				.settingId(alertDashboardCommon.getAlertSetting().getAlertSettingId())
				.build()
		);
	}
	Set<Integer> featureTypesSet = new HashSet<>();

	alertDashboardInventoryTimelineDtos = alertDashboardInventoryTimelineDtos.stream()
			.filter(dto -> featureTypesSet.add(dto.getFeatureTypeId())).collect(Collectors.toList());
	return alertDashboardInventoryTimelineDtos;
}
}