package com.se2.alert.constant;

public class Queries {

	public static final String ALERT_PCN_VIEW_BY_GREATER_EQUAL_BATCH_ID = "select * from alert_pcn_view where PATCH_ID >= ?";
	public static final String ALERT_PCN_VIEW_BY_GROUP_BATCH_ID = "select * from alert_pcn_view where PATCH_ID in (?)";
	public static final String ALERT_PCN_VIEW_BATCH_ID_BY_DATE = "select min(PATCH_ID) from alert_pcn_view where DATE(MODIFIED_DATE) >= DATE(?) and DATE(MODIFIED_DATE) < DATE(?)";

	public static final String ALERT_GIDEP_VIEW_BY_GREATER_EQUAL_BATCH_ID = "select * from alert_gidep_view where PATCH_ID >= ?";
	public static final String ALERT_GIDEP_VIEW_BY_GROUP_BATCH_ID = "select * from alert_gidep_view where PATCH_ID in (?)";
	public static final String ALERT_GIDEP_VIEW_BATCH_ID_BY_DATE = "select min(PATCH_ID) from alert_gidep_view where DATE(MODIFIED_DATE) >= DATE(?) and DATE(MODIFIED_DATE) < DATE(?)";

	public static final String ALERT_ACQUISITION_VIEW_BY_GREATER_EQUAL_BATCH_ID = "select * from alert_acquisition_view where PATCH_ID >= ?";
	public static final String ALERT_ACQUISITION_VIEW_BY_GROUP_BATCH_ID = "select * from alert_acquisition_view where PATCH_ID in (?)";
	public static final String ALERT_ACQUISITION_VIEW_BATCH_ID_BY_DATE = "select min(PATCH_ID) from alert_acquisition_view where DATE(MODIFIED_DATE) >= DATE(?) and DATE(MODIFIED_DATE) < DATE(?)";

	public static final String TMP_DML_BY_GREATER_EQUAL_BATCH_ID = "select * from temp_dml where PATCH_ID >= ?";
	public static final String TMP_DML_BY_GROUP_BATCH_ID = "select * from temp_dml where PATCH_ID in (?)";
	public static final String TMP_DML_BATCH_ID_BY_DATE = "select min(PATCH_ID) from temp_dml where DATE(MODIFIED_DATE) >= DATE(?) and DATE(MODIFIED_DATE) < DATE(?)";

	public static final String TBL_PDF_STATIC_DATA_DML_BY_GREATER_EQUAL_BATCH_ID = "select * from tbl_pdfstatic_datachng_dml where PATCH_ID >= ?";
	public static final String TBL_PDF_STATIC_DATA_DML_BY_GROUP_BATCH_ID = "select * from tbl_pdfstatic_datachng_dml where PATCH_ID in (?)";
	public static final String TBL_PDF_STATIC_DATA_DML_BATCH_ID_BY_DATE = "select min(PATCH_ID) from tbl_pdfstatic_datachng_dml where DATE(MODIFIED_DATE) >= DATE(?) and DATE(MODIFIED_DATE) < DATE(?)";
}
