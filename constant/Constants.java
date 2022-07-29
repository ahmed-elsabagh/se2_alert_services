package com.se2.alert.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Constants {

	private Constants() {
	}

	public final static String NOT_VALID_SETTING = "Setting id doesn't exist or may be have been deleted";
	public final static String NOT_ENOUGH_PERMISSIONS = "Not enough permissions to delete alert(s)";
	public final static String DELETED_SUCCESSFULLY = "Deleted successfully";
	public final static String NOT_FOUND_USER = "The user id doesn't exist or may be have been deleted";
	public final static String SETTING_NOT_FOUND = "Setting is not found";

	public final static String OFFLINE_THREAD_NAME = "offlineThread";
	public final static String ONLINE_THREAD_NAME = "onlineThread";
	public final static String STREAMING_THREAD_NAME = "StreamingThread";
	public final static String TRANSLATEDKEYS = "translatedKeys";
	public final static String TEMPLATE_PATH = "/templates";
	public final static String EXCEL_SUMMARY_TEMPLATE_FILE = "email-summary-template.ftl";
	public final static String EXCEL_DETAILS_TEMPLATE_FILE = "email-details-template.ftl";
	public final static String EMAIL_ASSIGNEE_TEMPLATE_FILE = "email-assignee-template.ftl";

	public final static String EMAIL_CHANGE_ALERT_NOTIFICATION_ISSUE_SUBJECT = "Change Alert Notification Issue Details";

	public final static String ERROR_OFFLINE_FINISHED_EMAIL_SUBJECT = "Alert offline process finished with excption(s)";
	public final static String SUCCEED_OFFLINE_FINISHED_EMAIL_SUBJECT = "Alert offline process finished successfully";
	public final static String SUCCEED_OFFLINE_FINISHED_EMAIL_BODY = "Alert offline process finished successfully you can start online process.";
	public final static String FILL_ITEMS_NAME_EMAIL_SUBJECT ="Fill Dashboard With ItemsNames";

	public final static String ERROR_ONLINE_FINISHED_EMAIL_SUBJECT = "Alert online process finished with excption(s)";
	public final static String SUCCEED_ONLINE_FINISHED_EMAIL_SUBJECT = "Alert online process finished successfully";
	public final static String SUCCEED_ONLINE_FINISHED_EMAIL_BODY = "Alert online process finished successfully you can start next switch.";

	public final static String ERROR_SENDING_FINISHED_EMAIL_SUBJECT = "Alert sending failed email process finished with excption(s)";
	public final static String SUCCEED_SENDING_FINISHED_EMAIL_SUBJECT = "Alert sending failed email process finished successfully";
	public final static String SUCCEED_SENDING_FINISHED_EMAIL_BODY = "Alert sending failed email process finished successfully, please check alert_run table.";
	public static final int BEST_CUTINCOMP = 46;
	public static final int BEST_COMPWITHOUTMAN = 43;
	public static final int BEST_HASH = 40;
	public static final int BEST_FAMILY = 41;

	public final static String TIMELINE_EXPORT = "timeline_export";
	public final static String EXCEL_EXTENSION = ".xlsx";
	public final static String ALERT = "Alert";

	public final static String CACHED_DASHBOARD_FILTERS = "CACHED_DASHBOARD_FILTERS";
	public final static String CACHED_TIMELINE_FILTERS = "CACHED_TIMELINE_FILTERS";
	public final static String CACHED_ARCHIVE_FILTERS = "CACHED_ARCHIVE_FILTERS";
	public final static String SUCCESS= "SUCCESS";

	public final static String CACHED_DASHBOARD_DATA = "CACHED_DASHBOARD_DATA";
	public final static String CACHED_DASHBOARD_TIMELINE_DATA = "CACHED_DASHBOARD_TIMELINE_DATA";
	public final static String CACHED_DASHBOARD_ARCHIVED_DATA = "CACHED_DASHBOARD_ARCHIVED_DATA";
	public final static String CACHED_DASHBOARD_TIMELINE_THREAD ="CACHED_DASHBOARD_TIMELINE_THREAD";
	public final static String CACHED_DASHBOARD_ARCHIVE_THREAD ="CACHED_DASHBOARD_TIMELINE_THREAD";
	public final static String CACHED_DASHBOARD_NOTSEEN = "CACHED_DASHBOARD_NOTSEEN";
	public final static String CACHED_DASHBOARD_FOR_SITE = "CACHED_DASHBOARD_FOR_SITE";
	public final static String CACHED_SETTINGS_DISTRIBUTORS = "CACHED_DASHBOARD_FOR_SITE";


	public final static int EXPORT_PAGE_SIZE = 10000;
	//JPQL Queries
	public static final String FIND_ALERT_SETTINGS_BY_SITE_AND_ITEM = "SELECT alrt_sttng FROM AlertSetting alrt_sttng" +
			" INNER JOIN AlertSettingItem sttng_item ON sttng_item.alertSetting.alertSettingId = alrt_sttng.alertSettingId" +
			" WHERE alrt_sttng.site.siteId = :siteId" +
			" AND sttng_item.itemId = :itemId AND alrt_sttng.isDeleted = false" +
			" ORDER BY alrt_sttng.modificationDate DESC" ;
	
	public static final List<String> LC_URGENT_STATUSES = new ArrayList<>(Arrays.asList("Obsolete","LTB","NRND"));
}
