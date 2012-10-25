package kevinpage.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlDatabase2 {
	
	private DatabaseOpenHelper mDatabaseHelper;
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "drinks.db";
	
	/**
	 * Constructor
	 * 
	 * @param context
	 *            The Context within which to work, used to create the DB
	 */
	public SqlDatabase2(Context context) {
		mDatabaseHelper = new DatabaseOpenHelper(context);
	}
	
	
	//TODO Build out QUERIES here
	
	
	
	public static class DatabaseOpenHelper extends SQLiteOpenHelper{

		private final Context mHelperContext;
		private SQLiteDatabase mDatabase;
		
		/** SQL to create first table of drinks */
		private static final String TABLE_CREATE1 = "CREATE TABLE " + FeedReaderContract.FeedEntry1.TABLE1
				+ " (" + FeedReaderContract.FeedEntry1.KEY_ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ FeedReaderContract.FeedEntry1.KEY_DRINK + " TEXT NOT NULL, " 
				+ FeedReaderContract.FeedEntry1.KEY_RATING + " INTEGER NOT NULL, " 
				+ FeedReaderContract.FeedEntry1.KEY_INSTRUCTIONS + " TEXT NOT NULL" 
				+ ");";
		/** SQL to create second table of ingredients */
		private static final String TABLE_CREATE2 = "CREATE TABLE " + FeedReaderContract.FeedEntry2.TABLE2
				+ " (" + FeedReaderContract.FeedEntry2.KEY_ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ FeedReaderContract.FeedEntry2.KEY_sINGREDIENT + " TEXT NOT NULL" 
				+ ");";		
		/** SQL to create third table of drink-ingredients */
		private static final String TABLE_CREATE3 = "CREATE TABLE " + FeedReaderContract.FeedEntry3.TABLE3
				+ " (" + FeedReaderContract.FeedEntry3.KEY_ID3 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ FeedReaderContract.FeedEntry3.KEY_subID1 + " INTEGER NOT NULL,"
				+ FeedReaderContract.FeedEntry3.KEY_subID2 + " INTEGER NOT NULL,"
				+ FeedReaderContract.FeedEntry3.KEY_AMOUNT + " TEXT NOT NULL,"
				+ "FOREIGN KEY(" + FeedReaderContract.FeedEntry3.KEY_subID1 + ") REFERENCES "
				+ FeedReaderContract.FeedEntry1.TABLE1 + "(" + FeedReaderContract.FeedEntry1.KEY_ID1 + "),"
				+ "FOREIGN KEY(" + FeedReaderContract.FeedEntry3.KEY_subID2 + ") REFERENCES "
				+ FeedReaderContract.FeedEntry2.TABLE2 + "(" + FeedReaderContract.FeedEntry2.KEY_ID2 + "),"
				+ ");";
		
		private static final String SQL_DELETE_TABLE1 = 
				"DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry1.TABLE1;
		private static final String SQL_DELETE_TABLE2 =
				"DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry2.TABLE2;
		private static final String SQL_DELETE_TABLE3 =
				"DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry3.TABLE3;
		
		
		DatabaseOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mHelperContext = context;
		}		
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_CREATE1);
			db.execSQL(TABLE_CREATE2);
			db.execSQL(TABLE_CREATE3);
			loadTableData();
		}
		
		/**
		 * Starts a thread to load the database table with words
		 */
		private void loadTableData() {
			new Thread(new Runnable() {
				public void run() {
					try {
						loadTables();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}).start();
		}
		
		//TODO edit to correctly scan in raw ingredients & other data
		private void loadTables() throws IOException {
			//Log.d(TAG, "Loading ingredients...");
			//Log.d(TAG2, "Loading drinks...");
			final Resources resources = mHelperContext.getResources();
			InputStream inputStream = resources.openRawResource(R.raw.drinks);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));

			try {
				String line;
				ArrayList<String> drinkIngredients = new ArrayList<String>();
				ArrayList<String> amounts = new ArrayList<String>();
				while ((line = reader.readLine()) != null) {
					int r = Integer.parseInt(reader.readLine());
					while (true) {
						String ing = reader.readLine();
						if (ing.equals("0"))
							break;
						//addIngredient(ing);
						String amount = reader.readLine();

						amounts.add(amount);
						drinkIngredients.add(ing);
					}
					String instruct = reader.readLine();
					//addDrink(line, r, drinkIngredients, amounts, instruct);
				}
			} finally {
				reader.close();
			}
			//Log.d(TAG, "DONE loading ingredients.");
			//Log.d(TAG2, "DONE loading drinks.");
		}
		
		
		//TODO add INSERT commands to database here
		
		

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQL_DELETE_TABLE1);
			db.execSQL(SQL_DELETE_TABLE2);
			db.execSQL(SQL_DELETE_TABLE3);
			onCreate(db);
			
		}

	}
}
