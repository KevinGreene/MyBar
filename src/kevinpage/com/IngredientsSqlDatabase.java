package kevinpage.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.text.TextUtils;
import android.util.Log;

/**
 * This class sets up database for a table of ingredients
 * @author Zach
 *
 */
public class IngredientsSqlDatabase {
	private static final String TAG = "IngredientsSqlDatabase";	//used later by Log
	
	/** The columns included in our drinks table*/
	public static final String KEY_ID = "_id";
	public static final String KEY_INGREDIENT = "ingredient";
	
	private static final String DATABASE_NAME = "ingredients";
	/** TODO Make more tables for drinks, etc.*/
	private static final String TABLE = "ingredients"; 
	private static final int DATABASE_VERSION = 1;
	
	private final IngredientsOpenHelper mIngredientsOpenHelper;
	
	private static final HashMap<String, String> mColumnMap = buildColumnMap();
	
	/**
     * Constructor
     * @param context The Context within which to work, used to create the DB
     */
	public IngredientsSqlDatabase(Context context){
		mIngredientsOpenHelper = new IngredientsOpenHelper(context);
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
		map.put(KEY_INGREDIENT, KEY_INGREDIENT);
		map.put(BaseColumns._ID, "rowid AS " +
                BaseColumns._ID);
		return map;
	}
	
	/**
     * Returns a Cursor positioned at the word specified by rowId
     *
     * @param rowId id of word to retrieve
     * @param columns The columns to include, if null then all are included
     * @return Cursor positioned to matching word, or null if not found.
     */
	public Cursor getIngredient(String rowId, String[] columns){
		String selection = "rowid = ?";
		String[] selectionArgs = new String[] {rowId};
		
		return query(selection, selectionArgs, columns);
		
		/* This builds a query that looks like: (SQL equivalent)
         *     SELECT <columns> FROM <table> WHERE rowid = <rowId>
         */
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
		/** TODO Change parameter to String[] when implement more tables*/
		builder.setTables(TABLE);
		builder.setProjectionMap(mColumnMap);
		
		Cursor cursor = builder.query(mIngredientsOpenHelper.getReadableDatabase(), columns, 
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
	private static class IngredientsOpenHelper extends SQLiteOpenHelper{

		private final Context mHelperContext;
		private SQLiteDatabase mDatabase;
		
		private static final String TABLE_CREATE = 
				"CREATE TABLE " + TABLE + " (" +
				KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				KEY_INGREDIENT + " TEXT NOT NULL" +
				");";
		
		IngredientsOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

		@Override
		public void onCreate(SQLiteDatabase db) {
			mDatabase = db;
            mDatabase.execSQL(TABLE_CREATE);
            loadTableIngredients();
		}
		
		/**
         * Starts a thread to load the database table with words
         */
        private void loadTableIngredients() {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadIngredients();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
        
        private void loadIngredients() throws IOException {
            Log.d(TAG, "Loading words...");
            final Resources resources = mHelperContext.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.ingredients);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] strings = TextUtils.split(line, "-");
                    if (strings.length < 2) continue;
                    long id = addIngredient(strings[0].trim(), strings[1].trim());
                    if (id < 0) {
                        Log.e(TAG, "unable to add word: " + strings[0].trim());
                    }
                }
            } finally {
                reader.close();
            }
            Log.d(TAG, "DONE loading ingredients.");
        }
        
        /**
         * Add an ingredient to the table.
         * @return rowId or -1 if failed
         */
        public long addIngredient(String ingredient, String definition) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_INGREDIENT, ingredient);

            return mDatabase.insert(TABLE, null, initialValues);
        }

        /**TODO improve this method to handle ingredient saving from old to new 
        * version
        */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE);
            onCreate(db);
		}

	}
}

















