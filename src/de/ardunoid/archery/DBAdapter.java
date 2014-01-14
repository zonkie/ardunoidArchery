package de.ardunoid.archery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	int id = 1;
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_TIME = "time";
	public static final String KEY_VALUE = "POINTS";
	public static final String KEY_SUM = "SUM";
	public static final String KEY_COUNT = "COUNT";
	public static final String KEY_DISTANCE = "DISTANCE";
	public static final String KEY_TARGETTYPE = "TARGETTYPE";

	
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "ardunoidarchery";
	private static final String DATABASE_TABLE = "hits";
	private static final int DATABASE_VERSION = 102; //Android manifest version == 1.0.2 
	

	private static final String DATABASE_CREATE = "CREATE TABLE "
		+ DATABASE_TABLE + "(" 
		+ KEY_ROWID + " INTEGER PRIMARY KEY ASC,"
		+ KEY_VALUE + " TEXT NOT NULL,"
		+ KEY_DATE + " TEXT NOT NULL, "
		+ KEY_TIME + " TEXT NOT NULL ,"
		+ KEY_DISTANCE + " TEXT NOT NULL, "
		+ KEY_TARGETTYPE + " TEXT NOT NULL "
		+")";
	
	public static final String DATABASE_UPDATE_1_TO_102 = "UPDATE TABLE "
		+ DATABASE_TABLE + " ADD COLUMN "
		+ KEY_DISTANCE + " TEXT NOT NULL DEFAULT '0', "
		+ KEY_TARGETTYPE + " TEXT NOT NULL DEFAULT '0' "
		;

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion <= 1){
				Log.d("test","Updating DB from version 1 to version 2");
				db.execSQL(DATABASE_UPDATE_1_TO_102);
				oldVersion=102;
			}
/*						if(oldVersion==102 || oldVersion==103){
				Log.d("test","Updating DB to version 4");
				db.execSQL(DATABASE_UPDATE_TO_FOUR_pt1);
				db.execSQL(DATABASE_UPDATE_TO_FOUR_pt2);
				oldVersion = 104;
			}
			if(oldVersion==104){
				Log.d("test","Updating DB to version 5");
				db.execSQL(DATABASE_UPDATE_TO_FIVE);
			}*/
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert an Entry into the database---
	public long insertHit(String Date, String Time, Integer Points) {
		long retval;
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_DATE, "" + Date + "");
			initialValues.put(KEY_TIME, "" + Time + "");
			initialValues.put(KEY_VALUE, "" + Points + "");
			retval = db.insert(DATABASE_TABLE, null, initialValues);
		} catch (Exception e) {
			retval = 0;
		}
		return retval;
	}

	public int getEntryCount() {
		Cursor cursor = db.rawQuery("SELECT COUNT(" + KEY_ROWID + ") FROM " + DATABASE_TABLE, null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		}
		return cursor.getInt(0);

	}

	public int getMaxId() {

		Cursor cursor = db.rawQuery("SELECT MAX(" + KEY_ROWID + ") FROM "
				+ DATABASE_TABLE, null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		}
		return cursor.getInt(0);
	}
	
	
	
	public int getArrowsByDate(String date) {
		Cursor cursor = db.rawQuery("SELECT COUNT(" + KEY_ROWID + ") FROM "
				+ DATABASE_TABLE + " WHERE " + KEY_DATE + " = '" + date + "'", null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		}
		return cursor.getInt(0);
	}
	public int getPointsByDate(String date) {
		Cursor cursor = db.rawQuery("SELECT SUM(" + KEY_VALUE + ") FROM " + DATABASE_TABLE + " WHERE " + KEY_DATE + " = '" + date + "'", null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		}
		return cursor.getInt(0);
	}
	
	public Cursor getHitsByDate(String date) {
		String query = "SELECT " + KEY_ROWID + ", " + KEY_DATE + " AS " + KEY_DATE + ", " + KEY_VALUE + " AS " + KEY_VALUE + " FROM " + DATABASE_TABLE+ " WHERE " + KEY_DATE + " = '" + date + "'";
		Log.d("ardunoid", query);
		return db.rawQuery(query , null);
	}
	
	public String getDateByHitId(Integer id) {
		String query = "SELECT " + KEY_DATE + " FROM " + DATABASE_TABLE + " WHERE " + KEY_ROWID + " = " + id;
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			return cursor.getString(0);
		}
		return cursor.getString(0);
	}
	
	

	
	
	public Cursor getStatsGroupedBy(Integer groupType) {
		String query;
		switch(groupType) {
			case 1:
				query = "SELECT " + KEY_ROWID + ", " + KEY_DATE + ", sum(" + KEY_VALUE + ") AS " + KEY_SUM + ", count(" + KEY_VALUE + ") AS " + KEY_COUNT + " FROM " + DATABASE_TABLE + " GROUP BY " + KEY_DATE;
				break;
			default: //grouped by date 
				query = "SELECT " + KEY_ROWID + ", " + KEY_DATE + ", sum(" + KEY_VALUE + ") AS " + KEY_SUM + ", count(" + KEY_VALUE + ") AS " + KEY_COUNT + " FROM " + DATABASE_TABLE + " GROUP BY " + KEY_DATE;
				break;
		}
		return db.rawQuery(query , null);
	}
	
	public Cursor getStatsOverall() {
		String query = "SELECT " + KEY_DATE + " AS " + KEY_DATE + ", " + KEY_VALUE + "as " + KEY_VALUE + ", sum(" + KEY_VALUE + ") AS " + KEY_SUM + ", count(" + KEY_VALUE + ") AS " + KEY_COUNT + " FROM " + DATABASE_TABLE + " GROUP BY " + KEY_DATE + ", " + KEY_VALUE;
		return db.rawQuery(query , null);
	}
	
	
	public Cursor getList() {
		return db.rawQuery("SELECT " + KEY_ROWID + "," + KEY_VALUE + " AS " + KEY_VALUE + " FROM " + DATABASE_TABLE, null);
	}
	

	
	
	
	
	
	public Cursor fetchOne(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_DATE, KEY_VALUE }, KEY_ROWID + "=" + rowId,
				null, null, null, null, null);
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
		return mCursor;
	}

	public void deleteEntry(Integer id){
		String query = "DELETE FROM " + DATABASE_TABLE + " WHERE " + KEY_ROWID + " = " + id;
		String table = DATABASE_TABLE;
		String whereClause = KEY_ROWID + "= ?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		Log.d("ardunoid", query);
	    db.delete(table, whereClause, whereArgs);
	}
	
}