package com.android.provider.fixcode;

import android.net.Uri;
import android.provider.BaseColumns;

public class Fixcode {

	public static final String DATABASE_NAME = "fixcode.db";
	public static final int VERSION = 1;

	public static final String AUTHORITY = "com.android.provider.fixcode";

	public static final int ITEM = 1;
	public static final int ITEM_ID = 2;
	public static final int DPT_ID = 3;
	public static final int DPT = 4;

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.android.fixcode";

	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.android.fixcode";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/item");

	public class FrmCode implements BaseColumns {
		/**
		 * 代码表的名称
		 */
		public static final String CODE_TABLE_NAME = "FRM_CODE";

		/**
		 * 系统列别字段
		 */
		public static final String XTLB = "xtlb";
		/**
		 * 代码列表
		 */
		public static final String DMLB = "dmlb";
		/**
		 * 代码表的名称
		 */
		public static final String DMZ = "dmz";
		/**
		 * 代码表的名称
		 */
		public static final String DMSM1 = "dmsm1";
		/**
		 * 代码表的名称
		 */
		public static final String DMSM2 = "dmsm2";
		/**
		 * 代码表的名称
		 */
		public static final String DMSM3 = "dmsm3";
		/**
		 * 代码表的名称
		 */
		public static final String DMSM4 = "dmsm4";
		/**
		 * 代码表的名称
		 */
		public static final String DMSX = "dmsx";
		/**
		 * 整型
		 */
		public static final String SXH = "sxh";
		/**
		 * 代码表的名称
		 */
		public static final String YWDX = "ywdx";
		/**
		 * 代码表的名称
		 */
		public static final String ZT = "zt";

	}

	// FRM_DPT_CODE
	public class frmDptCode {
		public static final String TABLE_NAME = "FRM_DPT_CODE";
		public static final String XTLB = "xtlb";
		public static final String DMLB = "dmlb";
		public static final String DMZ = "dmz";
		public static final String DMSM1 = "dmsm1";
		public static final String DMSM2 = "dmsm2";
		public static final String DMLBSM = "dmlbsm";
		public static final String ZT = "zt";
	}
}
