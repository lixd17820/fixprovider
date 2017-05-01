package com.android.provider.fixcode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FixcodeDatabaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH = "";
	private static final String DB_NAME = "fixcode.db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private int version;
	private String TAG = "FixcodeDatabaseHelper";
	// private static final String SDCARDDB="/sdcard/jwtdb/";

	private static FixcodeDatabaseHelper mDBConnection;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	private FixcodeDatabaseHelper(Context context, int version) {
		super(context, DB_NAME, null, version);
		this.myContext = context;
		this.version = version;
		DB_PATH = "/data/data/"
				+ context.getApplicationContext().getPackageName()
				+ "/databases/";
		// The Android's default system path of your application database is
		// "/data/data/mypackagename/databases/"
		// 测试存储卡的数据文件是否可用
		File f = new File(DB_PATH);
		if (!f.exists())
			f.mkdirs();
	}

	/**
	 * getting Instance
	 * 
	 * @param context
	 * @return DBAdapter
	 */
	public static synchronized FixcodeDatabaseHelper getDBAdapterInstance(
			Context context, int version) {
		if (mDBConnection == null) {
			mDBConnection = new FixcodeDatabaseHelper(context, version);
		}
		return mDBConnection;
	}

	/**
	 * Creates an empty database on the system and rewrites it with your own
	 * database.
	 **/
	public void createDataBase() throws IOException {
		boolean isNewBase = checkDataBase();
		if (!isNewBase) {
			try {
				copyDataBase();
				Log.e(TAG, "version is updated");
				String myPath = DB_PATH + Fixcode.DATABASE_NAME;
				SQLiteDatabase db = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READWRITE);
				Log.e(TAG, "version " + version);
				if (db != null && db.getVersion() < version) {
					db.setVersion(version);
					Log.e(TAG, "db.setVersion(" + version + ")");
					db.close();
				}
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		boolean result = false;
		int oldversion = 0;
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + Fixcode.DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		if (checkDB != null) {
			oldversion = checkDB.getVersion();
			Log.e(TAG, "old database's version is " + oldversion);
			checkDB.close();
			if (oldversion >= version) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		AssetManager am = myContext.getAssets();
		byte[] b = new byte[1024];
		try {
			OutputStream os = new FileOutputStream(new File(DB_PATH + DB_NAME));
			String[] files = am.list("");
			Arrays.sort(files);
			for (int j = 0; j < files.length; j++) {
				if (files[j].startsWith(DB_NAME)) {
					InputStream in = am.open(files[j]);
					int len = -1;
					while ((len = in.read(b)) > -1) {
						os.write(b, 0, len);
					}
					in.close();
				}
			}
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the database
	 * 
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	/**
	 * Close the database if exist
	 */
	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	/**
	 * Call on creating data base for example for creating tables at run time
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	/**
	 * can used for drop tables then call onCreate(db) function to create tables
	 * again - upgrade
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log
				.i("onUpgrade", "db old " + oldVersion + " newVersion "
						+ newVersion);

	}

	// ----------------------- CRUD Functions ------------------------------

	/**
	 * This function used to select the records from DB.
	 * 
	 * @param tableName
	 * @param tableColumns
	 * @param whereClase
	 * @param whereArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return A Cursor object, which is positioned before the first entry.
	 */
	public Cursor selectRecordsFromDB(String tableName, String[] tableColumns,
			String whereClase, String whereArgs[], String groupBy,
			String having, String orderBy) {
		return myDataBase.query(tableName, tableColumns, whereClase, whereArgs,
				groupBy, having, orderBy);
	}

	/**
	 * This function used to update the Record in DB.
	 * 
	 * @param tableName
	 * @param initialValues
	 * @param whereClause
	 * @param whereArgs
	 * @return 0 in case of failure otherwise return no of row(s) are updated
	 */
	public int updateRecordsInDB(String tableName, ContentValues initialValues,
			String whereClause, String whereArgs[]) {
		return myDataBase.update(tableName, initialValues, whereClause,
				whereArgs);
	}

	/**
	 * This function used to delete the Record in DB.
	 * 
	 * @param tableName
	 * @param whereClause
	 * @param whereArgs
	 * @return 0 in case of failure otherwise return no of row(s) are deleted.
	 */
	public int deleteRecordInDB(String tableName, String whereClause,
			String[] whereArgs) {
		return myDataBase.delete(tableName, whereClause, whereArgs);
	}

	// --------------------- Select Raw Query Functions ---------------------

	/**
	 * apply raw Query
	 * 
	 * @param query
	 * @param selectionArgs
	 * @return Cursor
	 */
	public Cursor selectRecordsFromDB(String query, String[] selectionArgs) {
		return myDataBase.rawQuery(query, selectionArgs);
	}

}
