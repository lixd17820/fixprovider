package com.android.provider.fixcode;

import java.io.IOException;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class FixcodeProvider extends ContentProvider {

	public static final String TAG = "FixcodeProvider";

	public FixcodeDatabaseHelper dbAdapter;

	public static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Fixcode.AUTHORITY, "item", Fixcode.ITEM);
		uriMatcher.addURI(Fixcode.AUTHORITY, "item/#/#", Fixcode.ITEM_ID);
		uriMatcher.addURI(Fixcode.AUTHORITY, "dptItem/#/#", Fixcode.DPT_ID);
		uriMatcher.addURI(Fixcode.AUTHORITY, "dpt", Fixcode.DPT);
	}

	@Override
	public boolean onCreate() {
		int currentCode = 0;
		try {
			PackageManager pm = getContext().getPackageManager();
			PackageInfo pi = pm.getPackageInfo("com.android.provider.fixcode",
					0);
			currentCode = pi.versionCode;
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		dbAdapter = FixcodeDatabaseHelper.getDBAdapterInstance(getContext(),
				currentCode);
		try {
			dbAdapter.createDataBase();
		} catch (IOException e) {
			Log.i("*** select ", e.getMessage());
		}
		dbAdapter.openDataBase();
		// Log.e(TAG,"on fixcode provider create data");
		// databasehelper =
		// FixcodeDatabaseHelper.getDBAdapterInstance(getContext());
		// try {
		// databasehelper.createDataBase();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		int code = uriMatcher.match(uri);
		switch (code) {
		case Fixcode.ITEM:
			return Fixcode.CONTENT_TYPE;
		case Fixcode.ITEM_ID:
			return Fixcode.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// SQLiteDatabase db = databasehelper.getWritableDatabase();
		// long rowId;
		// if (uriMatcher.match(uri) != Fixcode.ITEM) {
		// throw new IllegalArgumentException("Unknown URI " + uri);
		// }
		// rowId = db.insert(Fixcode.FrmCode.CODE_TABLE_NAME,
		// Fixcode.FrmCode._ID,
		// values);
		// if (rowId > 0) {
		// Uri noteUri = ContentUris
		// .withAppendedId(Fixcode.CONTENT_URI, rowId);
		// getContext().getContentResolver().notifyChange(noteUri, null);
		// return noteUri;
		// }
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor c = null;
		switch (uriMatcher.match(uri)) {
		case Fixcode.ITEM:
			c = dbAdapter
					.selectRecordsFromDB(Fixcode.FrmCode.CODE_TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		case Fixcode.DPT:
			c = dbAdapter
					.selectRecordsFromDB(Fixcode.frmDptCode.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		case Fixcode.ITEM_ID: {
			String xtlb = uri.getPathSegments().get(1);
			String dmlb = uri.getPathSegments().get(2);
			String w = Fixcode.FrmCode.XTLB
					+ "='"
					+ xtlb
					+ "' AND "
					+ Fixcode.FrmCode.DMLB
					+ "='"
					+ dmlb
					+ "'"
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			c = dbAdapter.selectRecordsFromDB(Fixcode.FrmCode.CODE_TABLE_NAME,
					projection, w, selectionArgs, null, null, sortOrder);
		}
			break;
		case Fixcode.DPT_ID: {
			String xtlb = uri.getPathSegments().get(1);
			String dmlb = uri.getPathSegments().get(2);
			String w = Fixcode.frmDptCode.XTLB
					+ "='"
					+ xtlb
					+ "' AND "
					+ Fixcode.frmDptCode.DMLB
					+ "='"
					+ dmlb
					+ "'"
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			c = dbAdapter.selectRecordsFromDB(Fixcode.frmDptCode.TABLE_NAME,
					projection, w, selectionArgs, null, null, sortOrder);
		}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		// c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
