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
 * This class sets up database for a table of ingredients as well as
 * a table for drinks.
 * @author Zach
 *
 * TODO Change to fit more columns. This shouldn't just be for inventory. Tables
 * for other information.
 */
public class SqlDatabase {
	private static final String TAG = "AllIngredientsSqlTable";	//used later by Log
	private static final String TAG2 = "AllDrinkSqlTable";		//""
	
	/** The columns included in our all-ingredients table*/
	public static final String KEY_ID = "_id";
	public static final String KEY_INGREDIENT = "ingredient";
	public static final String KEY_HAS = "hasIngredient";
	
	/** The columns included in our all-drinks table*/
	public static final String KEY_ID2 = "_id";
	public static final String KEY_DRINK = "drink";
	public static final String KEY_RATING = "rating";
	public static final String KEY_INGREDIENTS = "ingredients";
	public static final String KEY_AMOUNTS = "amount";
	public static final String KEY_INSTRUCTIONS = "instructions";
	
	private static final String DATABASE_NAME = "allDrinksIngredients"; 
	
	private static final String TABLE = "ingredients"; 
	private static final String TABLE2 = "drinks";
	private static final int DATABASE_VERSION = 1;
	
	private final DatabaseOpenHelper mDatabaseHelper;
	
	private static final HashMap<String, String> mColumnMap = buildColumnMap();
	
	/**
     * Constructor
     * @param context The Context within which to work, used to create the DB
     */
	public SqlDatabase(Context context){
		mDatabaseHelper = new DatabaseOpenHelper(context);
	}
	
	/**
	 * Taken from Android's Searchable Dictionary example:
     * Builds a map for all columns that may be requested, which will be given to the 
     * SQLiteQueryBuilder. This is a good way to define aliases for column names, but must include 
     * all columns, even if the value is the key. This allows the ContentProvider to request
     * columns w/o the need to know real column names and create the alias itself.
     */
	private static HashMap<String, String> buildColumnMap(){
		HashMap<String, String> map = new HashMap<String, String>();
		/** Mappings for ingredients table*/
		map.put(KEY_INGREDIENT, KEY_INGREDIENT);
		map.put(KEY_HAS, KEY_HAS);
		map.put(BaseColumns._ID, "rowid AS " +
                BaseColumns._ID);
		/** Mappings for drinks table*/
		map.put(KEY_DRINK, KEY_DRINK);	
		map.put(KEY_RATING, KEY_RATING);
		map.put(KEY_INGREDIENTS, KEY_INGREDIENTS);
		map.put(KEY_AMOUNTS, KEY_AMOUNTS);
		map.put(KEY_INSTRUCTIONS, KEY_INSTRUCTIONS);
		map.put(BaseColumns._ID, "rowid AS " + 
				BaseColumns._ID);
		return map;
	}
	
	/**
     * Returns a Cursor positioned at the word specified by rowId
     * TODO Build out other queries
     * @param rowId id of ingredient to retrieve
     * @param columns The columns to include, if null then all are included
     * @return Cursor positioned to matching ingredient, or null if not found.
     */
	public Cursor getIngredients(String rowId, String[] columns){
		String selection = null; //to get all ingredients
		String[] selectionArgs = new String[] {rowId};
		
		return query(selection, selectionArgs, columns);
		
		/* This builds a query that looks like: (SQL equivalent)
         *     SELECT <columns> FROM <table> WHERE rowid = <rowId>
         */
	}
	
	/**
	 * Returns a Cursor positioned at the drink specified by rowId
	 * @param rowId id of drink to retrieve
     * @param columns The columns to include, if null then all are included
     * @return Cursor positioned to matching drink, or null if not found.
	 */
	public Cursor getDrink(String rowId, String[] columns){
		String selection = "rowid = ?";
		String[] selectionArgs = new String[] {rowId};
		
		return query(selection, selectionArgs, columns);
	}
	
	/**
     * Performs a database query.
     * @param selection The selection clause
     * @param selectionArgs Selection arguments for "?" components in the selection
     * @param columns The columns to return
     * @return A Cursor over all rows matching the query
     */
	private Cursor query(String selection, String[] selectionArgs, String[] columns){
		/* The SQLiteBuilder provides a map for all possible columns requested to
         * actual columns in the database, creating a simple column alias mechanism
         * by which the ContentProvider does not need to know the real column names
         */ 
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		/** TODO Change parameter.. is this right?*/
		builder.setTables(TABLE + ", " + TABLE2 );
		builder.setProjectionMap(mColumnMap);
		
		Cursor cursor = builder.query(mDatabaseHelper.getReadableDatabase(), columns, 
				selection, selectionArgs, null, null, null);
		
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
	private static class DatabaseOpenHelper extends SQLiteOpenHelper{

		private final Context mHelperContext;
		private SQLiteDatabase mDatabase;
		/** SQL to create first table of ingredients*/
		private static final String TABLE_CREATE = 
				"CREATE TABLE " + TABLE + " (" +
				KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				KEY_INGREDIENT + " TEXT NOT NULL" +
				");";
		/** SQL to create second table of drinks*/
		private static final String TABLE_CREATE2 =
				"CREATE TABLE " + TABLE2 + " (" +
				KEY_ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				KEY_DRINK + " TEXT NOT NULL" +
				KEY_RATING + " INTEGER NOT NULL" +
				KEY_INGREDIENTS + " TEXT NOT NULL" +
				KEY_AMOUNTS + " TEXT NOT NULL" +
				KEY_INSTRUCTIONS + " TEXT NOT NULL" +
				");";
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

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
					addDrink(line, r, drinkIngredients, amounts,
							instruct);
                }
            } finally {
                reader.close();
            }
            Log.d(TAG, "DONE loading ingredients.");
            Log.d(TAG2, "DONE loading drinks.");
        }
        
        /**
         * Add an ingredient to the table.
         * @return rowId or -1 if failed
         */
        public long addIngredient(String ingredient) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_INGREDIENT, ingredient);

            return mDatabase.insert(TABLE, null, initialValues);
        }
        
        /**
         * Add a drink to the table.
         * @return rowId or -1 if failed
         */
        public long addDrink(String name, int rating, ArrayList<String> ingredients, 
        		ArrayList<String> amounts, String instructions){
        	
        	ContentValues initialValues = new ContentValues();
        	initialValues.put(KEY_DRINK, name);
        	initialValues.put(KEY_RATING, rating);
        	for(int i = 0; i < ingredients.size(); i ++){
        		initialValues.put(KEY_INGREDIENTS, ingredients.get(i));
        	}
        	for(int j = 0; j < amounts.size(); j ++){
        		initialValues.put(KEY_AMOUNTS, amounts.get(j));
        	}
        	initialValues.put(KEY_INSTRUCTIONS, instructions);
        	return 1;
        }

        /**TODO improve this method to handle ingredient saving from old to new 
        * version
        */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
            onCreate(db);
		}

	}
}

















