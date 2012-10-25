package kevinpage.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

//import com.example.android.searchabledict.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * This class sets up database for a table of ingredients as well as a table for
 * drinks.
 * 
 * @author Zach
 * 
 * TODO THIS IS NOW BEING MOVED OVER TO SqlDatabase2... work in progress
 */
public class SqlDatabase {
	private static final String TAG = "ingredientsTable"; // used later by Log
	private static final String TAG2 = "drinksTable"; // ""
	private static final String TAG3 = "drinkingredientsTable"; // ""

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

	private static final String DATABASE_NAME = "drinks";

	private static final String TABLE1 = "drinks";
	private static final String TABLE2 = "ingredients";
	private static final String TABLE3 = "drinkIngredients";
	private static final int DATABASE_VERSION = 1;

	private final DatabaseOpenHelper mDatabaseHelper;

	private static final HashMap<String, String> mDrinkColumnMap = buildDrinkColumnMap();
	private static final HashMap<String, String> mIngredientsColumnMap = buildIngredientsColumnMap();
	private static final HashMap<String, String> mDrinkIngredientsColumnMap = buildDrinkIngredientsColumnMap();

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The Context within which to work, used to create the DB
	 */
	public SqlDatabase(Context context) {
		mDatabaseHelper = new DatabaseOpenHelper(context);
	}

	/**
	 * Taken from Android's Searchable Dictionary example: Builds a map for all
	 * columns that may be requested, which will be given to the
	 * SQLiteQueryBuilder. This is a good way to define aliases for column
	 * names, but must include all columns, even if the value is the key. This
	 * allows the ContentProvider to request columns w/o the need to know real
	 * column names and create the alias itself.
	 */
	private static HashMap<String, String> buildDrinkColumnMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		/** Mappings for drinks table */
		map.put(KEY_DRINK, KEY_DRINK);
		map.put(KEY_RATING, KEY_RATING);
		map.put(KEY_INSTRUCTIONS, KEY_INSTRUCTIONS);

		return map;
	}

	private static HashMap<String, String> buildIngredientsColumnMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
		/** Mappings for ingredients table */
		map.put(KEY_sINGREDIENT, KEY_sINGREDIENT);
		//map.put(KEY_HAS, KEY_HAS);
		map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);

		return map;
	}

	private static HashMap<String, String> buildDrinkIngredientsColumnMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		/** Mappings for drink ingredients table */
		map.put(KEY_subID1, KEY_subID1);
		map.put(KEY_subID2, KEY_subID2);
		map.put(KEY_AMOUNT, KEY_AMOUNT);
		map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);

		return map;
	}
	
	// TODO Build out other queries
	
	/**
	 * Returns a Cursor positioned over all ingredients specified by rowId 
	 * @param has
	 *            has value of ingredient to retrieve
	 * @param columns
	 *            The columns to include, if null then all are included
	 * @return Cursor positioned to matching ingredient, or null if not found.
	 */
	public Cursor getHasIngredients(String has, String[] columns) {
		String selection = "has = ?";
		String[] selectionArgs = new String[] { has };

		return queryDrinks(selection, selectionArgs, columns);

		/*
		 * This builds a query that looks like: (SQL equivalent) SELECT
		 * <columns> FROM <table> WHERE rowid = <rowId>
		 */
	}

	//TODO change this method
	/**
	 * Returns a Cursor positioned at the drink specified by rowId
	 * 
	 * @param rowId
	 *            id of drink to retrieve
	 * @param columns
	 *            The columns to include, if null then all are included
	 * @return Cursor positioned to matching drink, or null if not found.
	 */
	public Cursor getDrink(String rowId, String[] columns) {
		String selection = "rowid = ?";
		String[] selectionArgs = new String[] { rowId };

		return queryDrinks(selection, selectionArgs, columns);
	}
	
	/**
	 * Performs a database query on the table of drinks
	 * 
	 * @param selection
	 *            The selection clause
	 * @param selectionArgs
	 *            Selection arguments for "?" components in the selection
	 * @param columns
	 *            The columns to return
	 * @return A Cursor over all rows matching the query
	 */
	private Cursor queryDrinks(String selection, String[] selectionArgs,
			String[] columns) {
		/*
		 * The SQLiteBuilder provides a map for all possible columns requested
		 * to actual columns in the database, creating a simple column alias
		 * mechanism by which the ContentProvider does not need to know the real
		 * column names
		 */
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(TABLE1);
		builder.setProjectionMap(mDrinkColumnMap);

		Cursor cursor = builder.query(mDatabaseHelper.getReadableDatabase(),
				columns, selection, selectionArgs, null, null, null);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		return cursor;
	}
	
	/**
	 * Performs a database query on the table of ingredients
	 * @param selection The selection clause
	 * @param selectionArgs Selection arguments for "?" components in the selection
	 * @param columns The columns to return
	 * @return A Cursor over all rows matching the query
	 */
	private Cursor queryIngredients(String selection, String[] selectionArgs,
			String[] columns) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(TABLE2);
		builder.setProjectionMap(mIngredientsColumnMap);

		Cursor cursor = builder.query(mDatabaseHelper.getReadableDatabase(),
				columns, selection, selectionArgs, null, null, null);
		
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		return cursor;
	}

	/**
	 * Performs a database query on the table of drink ingredients
	 * @param selection The selection clause
	 * @param selectionArgs Selection arguments for "?" components in the selection
	 * @param columns The columns to return
	 * @return A Cursor over all rows matching the query
	 */
	private Cursor queryDrinkIngredients(String selection, String[] selectionArgs,
			String[] columns) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(TABLE3);
		builder.setProjectionMap(mDrinkIngredientsColumnMap);

		Cursor cursor = builder.query(mDatabaseHelper.getReadableDatabase(),
				columns, selection, selectionArgs, null, null, null);
		
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		return cursor;
	}

	/**
	 * This creates/opens the database.
	 */
	private static class DatabaseOpenHelper extends SQLiteOpenHelper {

		private final Context mHelperContext;
		private SQLiteDatabase mDatabase;
		/** SQL to create second table of drinks */
		private static final String TABLE_CREATE2 = "CREATE TABLE " + TABLE1
				+ " (" + KEY_ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_DRINK + " TEXT NOT NULL, " 
				+ KEY_RATING + " INTEGER NOT NULL, " 
				+ KEY_INSTRUCTIONS + " TEXT NOT NULL" 
				+ ");";
		/** SQL to create first table of ingredients */
		private static final String TABLE_CREATE = "CREATE TABLE " + TABLE2
				+ " (" + KEY_ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_sINGREDIENT + " TEXT NOT NULL" 
				+ ");";		
		/** SQL to create third table of drink-ingredients */
		private static final String TABLE_CREATE3 = "CREATE TABLE " + TABLE3
				+ " (" + KEY_ID3 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_subID1 + " INTEGER NOT NULL,"
				+ KEY_subID2 + " INTEGER NOT NULL,"
				+ KEY_AMOUNT + " TEXT NOT NULL,"
				+ ");";

		DatabaseOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mHelperContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			mDatabase = db;
			mDatabase.execSQL(TABLE_CREATE);
			mDatabase.execSQL(TABLE_CREATE2);
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

		private void loadTables() throws IOException {
			Log.d(TAG, "Loading ingredients...");
			Log.d(TAG2, "Loading drinks...");
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
						addIngredient(ing);
						String amount = reader.readLine();

						amounts.add(amount);
						drinkIngredients.add(ing);
					}
					String instruct = reader.readLine();
					addDrink(line, r, drinkIngredients, amounts, instruct);
				}
			} finally {
				reader.close();
			}
			Log.d(TAG, "DONE loading ingredients.");
			Log.d(TAG2, "DONE loading drinks.");
		}

		/**
		 * Add an ingredient to the table.
		 * 
		 * @return rowId or -1 if failed
		 */
		public long addIngredient(String ingredient) {
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_sINGREDIENT, ingredient);

			return mDatabase.insert(TABLE1, null, initialValues);
		}

		/**
		 * Add a drink to the table.
		 * 
		 * @return rowId or -1 if failed
		 */
		public long addDrink(String name, int rating,
				ArrayList<String> ingredients, ArrayList<String> amounts,
				String instructions) {

			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_DRINK, name);
			initialValues.put(KEY_RATING, rating);
			/*
			 * for(int i = 0; i < ingredients.size(); i ++){
			 * initialValues.put(KEY_dINGREDIENT, ingredients.get(i)); }
			 */
			for (int j = 0; j < amounts.size(); j++) {
				initialValues.put(KEY_AMOUNT, amounts.get(j));
			}
			initialValues.put(KEY_INSTRUCTIONS, instructions);
			return 1;
		}

		/**
		 * TODO improve this method to handle ingredient saving from old to new
		 * version
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE1);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
			onCreate(db);
		}

	}
}
