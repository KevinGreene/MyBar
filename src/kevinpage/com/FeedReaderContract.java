package kevinpage.com;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class FeedReaderContract{
	public static abstract class FeedEntry implements BaseColumns {
		/** The columns included in our all-drinks table */
		public static final String KEY_ID1 = "drink_id";
		public static final String KEY_DRINK = "name";
		public static final String KEY_RATING = "rating";
		public static final String KEY_INSTRUCTIONS = "instructions";

		/** The columns included in our all-ingredients table */
		public static final String KEY_ID2 = "ingred_id";
		public static final String KEY_sINGREDIENT = "name";
		//public static final String KEY_HAS = "has";

		/** The columns included in our drink-ingredients table */
		public static final String KEY_ID3 = "_id";
		public static final String KEY_subID1 = "drink_id";
		public static final String KEY_subID2 = "ingred_id";
		public static final String KEY_AMOUNT = "amount";

		public static final String TABLE1 = "drinks";
		public static final String TABLE2 = "ingredients";
		public static final String TABLE3 = "drinkIngredients";
	}
	
	
	
	private  FeedReaderContract() {}
	
	/** SQL to create second table of drinks */
	private static final String TABLE_CREATE2 = "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE1
			+ " (" + FeedReaderContract.FeedEntry.KEY_ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ FeedReaderContract.FeedEntry.KEY_DRINK + " TEXT NOT NULL, " 
			+ FeedReaderContract.FeedEntry.KEY_RATING + " INTEGER NOT NULL, " 
			+ FeedReaderContract.FeedEntry.KEY_INSTRUCTIONS + " TEXT NOT NULL" 
			+ ");";
	/** SQL to create first table of ingredients */
	private static final String TABLE_CREATE = "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE2
			+ " (" + FeedReaderContract.FeedEntry.KEY_ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ FeedReaderContract.FeedEntry.KEY_sINGREDIENT + " TEXT NOT NULL" 
			+ ");";		
	/** SQL to create third table of drink-ingredients */
	private static final String TABLE_CREATE3 = "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE3
			+ " (" + FeedReaderContract.FeedEntry.KEY_ID3 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ FeedReaderContract.FeedEntry.KEY_subID1 + " INTEGER NOT NULL,"
			+ FeedReaderContract.FeedEntry.KEY_subID2 + " INTEGER NOT NULL,"
			+ FeedReaderContract.FeedEntry.KEY_AMOUNT + " TEXT NOT NULL,"
			+ ");";
	
	private static final String SQL_DELETE_TABLE1 = 
			"DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE1;
	private static final String SQL_DELETE_TABLE2 =
			"DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE2;
	private static final String SQL_DELETE_TABLE3 =
			"DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE3;
	
	public class FeedReaderDbHelper extends SQLiteOpenHelper {
		
		private static final int DATABASE_VERSION = 1;
		private static final String DATABASE_NAME = "drinks.db";

		public FeedReaderDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_CREATE);
			db.execSQL(TABLE_CREATE2);
			db.execSQL(TABLE_CREATE3);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQL_DELETE_TABLE1);
			db.execSQL(SQL_DELETE_TABLE2);
			db.execSQL(SQL_DELETE_TABLE3);
			onCreate(db);
		}		
	}
}
