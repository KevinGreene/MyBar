package kevinpage.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyBarDatabase {

	private static final String TAG = "SqlDatabase";
	private final DatabaseOpenHelper mDatabaseHelper; // used for queries later
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "drinks.db";

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The Context within which to work, used to create the DB
	 */
	public MyBarDatabase(Context context) {
		mDatabaseHelper = new DatabaseOpenHelper(context);
		mDatabaseHelper.getReadableDatabase();
		//mDatabaseHelper.close();
	}

	// TODO Build out QUERIES here

	/**
	 * Returns a Cursor over the drink-ingredients as specified by the drink_id
	 * @param selectionArgs The drink id
	 * @return a Cursor over the ingredients connected to the drink w/ the amount
	 */
	public Cursor getDrinkIngredientsById(String selectionArgs){
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		String[] projection = new String[] {FeedReaderContract.FeedEntry3.KEY_ingredNAME, FeedReaderContract.FeedEntry3.KEY_AMOUNT};
		String selection = FeedReaderContract.FeedEntry3.KEY_subID1 + " =?";
		String[] ingredSelections = new String[] {selectionArgs};
		
		Cursor cursor = db.query(FeedReaderContract.FeedEntry3.TABLE3, projection, selection, ingredSelections, null, null, null);
		
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		db.close();
		return cursor;
	}
	
	/**
	 * Returns an ingredient based on its name
	 * @param selectionArgs The name of the ingredient
	 * @return A Cursor over the ingredient as specified
	 */
	public Cursor getIngredByName(String selectionArgs){
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		String[] projection = new String[] {FeedReaderContract.FeedEntry2.KEY_sINGREDIENT, FeedReaderContract.FeedEntry2.KEY_HAS};
		String selection = FeedReaderContract.FeedEntry2.KEY_sINGREDIENT + " =?";
		String[] singleSelection = new String[] {selectionArgs};
		
		Cursor cursor = db.query(FeedReaderContract.FeedEntry2.TABLE2, projection, selection, singleSelection, null, null, null);
		
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		db.close();
		return cursor;
	}
	
	/**
	 * Returns a Cursor over the specified drink and its rating
	 * @param selectionArgs The name of the drink
	 * @return Cursor over drink and rating column
	 */
	public Cursor getDrinkInfo(String selectionArgs){
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		String[] projection = new String[] {FeedReaderContract.FeedEntry1.KEY_DRINK, FeedReaderContract.FeedEntry1.KEY_RATING, FeedReaderContract.FeedEntry1.KEY_ID1, FeedReaderContract.FeedEntry1.KEY_INSTRUCTIONS};
		String selection = FeedReaderContract.FeedEntry1.KEY_DRINK + " =?";
		String[] singleSelection = new String[] {selectionArgs};
		
		Cursor cursor = db.query(FeedReaderContract.FeedEntry1.TABLE1, projection, selection, singleSelection, null, null, null);
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		db.close();
		return cursor;
	}
	
	/**
	 * Returns a Cursor over all of the ingredients
	 * @return a Cursor over all of the ingredients in alphabetical order
	 */
	public Cursor getAllDrinks(){
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		String[] projection = new String[] { FeedReaderContract.FeedEntry1.KEY_DRINK, FeedReaderContract.FeedEntry1.KEY_ID1 };
		String ordering = " COLLATE NOCASE";
		
		Cursor cursor = db.query(FeedReaderContract.FeedEntry1.TABLE1, projection, null, null, null, null, FeedReaderContract.FeedEntry1.KEY_DRINK + ordering);
		
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		db.close();
		return cursor;
	}
/*	
	*//**
	 * Returns a Cursor positioned at all ingredients from table
	 * 
	 * @param columns
	 *            The columns to include
	 * @return Cursor positioned over all ingredients
	 *//*
	public Cursor getAllIngredients() {
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		// Projection that specifies which columns we will use for query
		String[] projection = new String[] { FeedReaderContract.FeedEntry2.KEY_sINGREDIENT };

		Cursor cursor = db.query(FeedReaderContract.FeedEntry2.TABLE2,
				projection, null, null, null, null, null);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		db.close();
		return cursor;
	}*/

	/**
	 * Returns a Cursor over the rows of ingredients that the user has or
	 * doesn't have
	 * 
	 * @param selectionArgs
	 *            The arguments (probably 0 or 1) answering the selection clause
	 * @return A Cursor over the rows as specified by the selection arguments
	 */
	public Cursor getHasIngredients(String selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		String[] projection = new String[] { FeedReaderContract.FeedEntry2.KEY_sINGREDIENT };
		String selection = FeedReaderContract.FeedEntry2.KEY_HAS + " = ?";
		String[] singleSelection = new String[] { selectionArgs };
		String ordering = " COLLATE NOCASE";

		Cursor cursor = db.query(FeedReaderContract.FeedEntry2.TABLE2,
				projection, selection, singleSelection, null, null, FeedReaderContract.FeedEntry2.KEY_sINGREDIENT + ordering);
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		db.close();
		return cursor;
	}
/*	
	*//**
	 * Returns a Cursor over the has value of the name of the ingredient provided.
	 * @param selectionArgs The name of the ingredient
	 * @return a Cursor over the has column of the ingredient
	 *//*
	public Cursor getHasIngredient(String selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		String[] projection = new String[] { FeedReaderContract.FeedEntry2.KEY_HAS };
		String selection = FeedReaderContract.FeedEntry2.KEY_sINGREDIENT
				+ " = ?";
		String[] singleSeletion = new String[] { selectionArgs };

		Cursor cursor = db.query(FeedReaderContract.FeedEntry2.TABLE2,
				projection, selection, singleSeletion, null, null, null);
		
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		db.close();
		return cursor;
	}*/
	
	/**
	 * Gets a Cursor over all possible drinks the user can make
	 * @return A Cursor over all possible drink combinations
	 */
	public Cursor getPossibleDrinks() {
		Log.d("HEYEYYYYEYEY", "GOT INSIDE");
		
		//int count = 0;
		ArrayList<String> validDrinks = new ArrayList<String>();
		Cursor allDrinks = getAllDrinks();
		for(int i = 0; i < allDrinks.getCount() && !(allDrinks.isAfterLast()); i++ ){
			String drinkName = allDrinks.getString(0);
			Cursor drinkIgreds = getDrinkIngredientsById(allDrinks.getString(1));
			boolean soFarSoGood = true;
			for(int j = 0; j < drinkIgreds.getCount() && !(drinkIgreds.isAfterLast()); j++){
				if(getIngredByName(drinkIgreds.getString(0)).getInt(1) == 0){
					soFarSoGood = false;
					break;
				}
				drinkIgreds.moveToLast();
			}
			if(soFarSoGood){
				validDrinks.add(drinkName);
			}
			soFarSoGood = true;
			allDrinks.moveToNext();
		}
		
		if(validDrinks.isEmpty()){ return null; } //there are no possible drinks
		
		String inOp = "(";
		for(int k = 0; k < validDrinks.size(); k++){
			inOp += "?";
			if(k < validDrinks.size()-1){ inOp+=","; }
		}
		inOp += ")";
		
		String[] projection = new String[] {FeedReaderContract.FeedEntry1.KEY_DRINK};
		String selection = FeedReaderContract.FeedEntry1.KEY_DRINK + " IN" + inOp;
		String[] selections = new String[validDrinks.size()];
		for(int z = 0; z < validDrinks.size(); z++){
			selections[z] = validDrinks.get(z);
		}
		
		
		//String[] projection = new String[] {FeedReaderContract.FeedEntry3.KEY_ingredNAME, FeedReaderContract.FeedEntry3.KEY_AMOUNT};
		//String selection = FeedReaderContract.FeedEntry2.KEY_HAS + " =?";
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		Cursor cursor = db.query(FeedReaderContract.FeedEntry1.TABLE1, projection, selection, selections, null, null, null);
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		db.close();
		return cursor;
	}
	
	/**
	 * Method to update the 'has' column in the ingredients table
	 * 
	 * @param has
	 *            The value to update to
	 * @param ingredName
	 *            The name of the ingredient to update
	 * @return How many rows were affected
	 */
	public long updateHasValue(int has, String ingredName){
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		Log.d(TAG, "Updating ingredient 'has' field");
		ContentValues initialValues = new ContentValues();
		initialValues.put(FeedReaderContract.FeedEntry2.KEY_HAS, has);
		String where = FeedReaderContract.FeedEntry2.KEY_sINGREDIENT
				+ " = ?";
		String[] whereArgs = new String[] { ingredName };
		long rowsAffeected = db.update(FeedReaderContract.FeedEntry2.TABLE2,
					initialValues, where, whereArgs);
		db.close();
		return rowsAffeected;
	}

	// //////////////////////////////////////////////////////////////////////////////////

	/**
	 * This class is used to help create, get queries, and update or delete data
	 * from the database
	 * 
	 * @author Zach
	 * 
	 */
	public static class DatabaseOpenHelper extends SQLiteOpenHelper {

		private final Context mHelperContext;
		private SQLiteDatabase mDatabase;
		
		/** SQL to create first table of drinks */
		private static final String TABLE_CREATE1 = "CREATE TABLE "
				+ FeedReaderContract.FeedEntry1.TABLE1 + " ("
				+ FeedReaderContract.FeedEntry1.KEY_ID1	+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ FeedReaderContract.FeedEntry1.KEY_DRINK + " TEXT NOT NULL, "
				+ FeedReaderContract.FeedEntry1.KEY_RATING	+ " INTEGER NOT NULL, "
				+ FeedReaderContract.FeedEntry1.KEY_INSTRUCTIONS + " TEXT NOT NULL" + ");";
		/** SQL to create second table of ingredients */
		private static final String TABLE_CREATE2 = "CREATE TABLE "
				+ FeedReaderContract.FeedEntry2.TABLE2 + " ("
				+ FeedReaderContract.FeedEntry2.KEY_ID2	+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ FeedReaderContract.FeedEntry2.KEY_sINGREDIENT	+ " TEXT UNIQUE NOT NULL," 
				+ FeedReaderContract.FeedEntry2.KEY_HAS	+ " INTEGER NOT NULL" + ");";
		/** SQL to create third table of drink-ingredients */
		private static final String TABLE_CREATE3 = "CREATE TABLE "
				+ FeedReaderContract.FeedEntry3.TABLE3 + " ("
				+ FeedReaderContract.FeedEntry3.KEY_ID3	+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ FeedReaderContract.FeedEntry3.KEY_subID1	+ " INTEGER NOT NULL,"
				+ FeedReaderContract.FeedEntry3.KEY_AMOUNT + " TEXT NOT NULL,"
				+ FeedReaderContract.FeedEntry3.KEY_ingredNAME + " TEXT NOT NULL," //TODO this needs to be implemented
				+ "FOREIGN KEY(" + FeedReaderContract.FeedEntry3.KEY_subID1
				+ ") REFERENCES " + FeedReaderContract.FeedEntry1.TABLE1 + "("
				+ FeedReaderContract.FeedEntry1.KEY_ID1 + ")"+ ");";

		private static final String SQL_DELETE_TABLE1 = "DROP TABLE IF EXISTS "
				+ FeedReaderContract.FeedEntry1.TABLE1;
		private static final String SQL_DELETE_TABLE2 = "DROP TABLE IF EXISTS "
				+ FeedReaderContract.FeedEntry2.TABLE2;
		private static final String SQL_DELETE_TABLE3 = "DROP TABLE IF EXISTS "
				+ FeedReaderContract.FeedEntry3.TABLE3;

		DatabaseOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mHelperContext = context;
			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "Called onCreate..");
			mDatabase = db;
			db.execSQL(TABLE_CREATE1);
			db.execSQL(TABLE_CREATE2);
			db.execSQL(TABLE_CREATE3);
			db.execSQL("PRAGMA foreign_keys=ON;");// enable foreign keys
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
			// Log.d(TAG, "Loading ingredients...");
			// Log.d(TAG2, "Loading drinks...");
			final Resources resources = mHelperContext.getResources();
			InputStream inputStream = resources.openRawResource(R.raw.drinks);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));

			try {
				Log.d(TAG, "Loading database...");
				String line;
				ArrayList<String> drinkIngredients = new ArrayList<String>();
				ArrayList<String> amounts = new ArrayList<String>();
				while ((line = reader.readLine()) != null) {// get Drink name
					drinkIngredients.clear();
					amounts.clear();
					long drinkRowId = -1;
					int r = Integer.parseInt(reader.readLine());// get Drink
																// rating
					while (true) {
						String ing = reader.readLine(); // get ingred name
						if (ing.equals("0")) // stop if it was 0
							break;
						addIngredient(ing, 0); // add that
																// ingredient
																// name and get
																// rowId
						String amount = reader.readLine(); // get amount for
															// ingredient

						amounts.add(amount); // add to arraylist
						drinkIngredients.add(ing); // add to arraylist
					}
					String instruct = reader.readLine(); // instructions for
															// drink
					drinkRowId = addDrink(line, r, instruct); // add the drink
					/**
					 * If ingredients were read in for the drink, add it to the
					 * db with the correct row id's
					 */
					if (!(drinkIngredients.isEmpty())) {
						for (int i = 0; i < drinkIngredients.size(); i++) {
							addDrinkIngredient(amounts.get(i), drinkIngredients.get(i),
									drinkRowId);
						}
					}
				}
			} finally {
				reader.close();
				//mDatabase.close();
				Log.d(TAG, "Done loading database");
			}

		}

		/**
		 * Add an ingredient to the table.
		 * 
		 * @return rowId or -1 if failed
		 */
		public long addIngredient(String ingredient, int has) {
			Log.d(TAG, "Adding ingredient...");
			ContentValues initialValues = new ContentValues();
			initialValues.put(FeedReaderContract.FeedEntry2.KEY_sINGREDIENT,
					ingredient);
			initialValues.put(FeedReaderContract.FeedEntry2.KEY_HAS, has); // assume
																			// they
																			// don't
																			// have
																			// it

			return mDatabase.insert(FeedReaderContract.FeedEntry2.TABLE2, null,
					initialValues);
		}

		/**
		 * Add a drink to the table.
		 * 
		 * @return rowId or -1 if failed
		 */
		public long addDrink(String name, int rating, String instructions) {
			Log.d(TAG, "Adding drink...");
			ContentValues initialValues = new ContentValues();
			initialValues.put(FeedReaderContract.FeedEntry1.KEY_DRINK, name);
			initialValues.put(FeedReaderContract.FeedEntry1.KEY_RATING, rating);
			initialValues.put(FeedReaderContract.FeedEntry1.KEY_INSTRUCTIONS,
					instructions);
			/*
			 * for(int i = 0; i < ingredients.size(); i ++){
			 * initialValues.put(KEY_dINGREDIENT, ingredients.get(i)); }
			 */
			/*
			 * for (int j = 0; j < amounts.size(); j++) {
			 * initialValues.put(KEY_AMOUNT, amounts.get(j)); }
			 */

			return mDatabase.insert(FeedReaderContract.FeedEntry1.TABLE1, null,
					initialValues);
		}

		/**
		 * Add a drink-ingredient to its table
		 * 
		 * @param amount
		 *            The amount read in for the drink
		 * @param ingredId
		 *            The matching id from the ingredient list
		 * @param drinkId
		 *            The matching id from the drink it belongs to
		 * @return The rowId of where it was inserted, or -1 if failed
		 * TODO
		 */
		public long addDrinkIngredient(String amount, String name,
				long drinkId) {
			Log.d(TAG, "Adding DRINK ingredient...");
			ContentValues initialValues = new ContentValues();
			initialValues
					.put(FeedReaderContract.FeedEntry3.KEY_subID1, drinkId);
			initialValues.put(FeedReaderContract.FeedEntry3.KEY_ingredNAME,
					name);
			initialValues.put(FeedReaderContract.FeedEntry3.KEY_AMOUNT, amount);

			return mDatabase.insert(FeedReaderContract.FeedEntry3.TABLE3, null,
					initialValues);
		}

		/**
		 * Method to update the 'has' column in the ingredients table
		 * 
		 * @param has
		 *            The value to update to
		 * @param ingredName
		 *            The name of the ingredient to update
		 * @return How many rows were affected
		 *//*
		public long updateHas(int has, String ingredName) {
			Log.d(TAG, "Updating ingredient 'has' field");
			ContentValues initialValues = new ContentValues();
			initialValues.put(FeedReaderContract.FeedEntry2.KEY_HAS, has);
			String where = FeedReaderContract.FeedEntry2.KEY_sINGREDIENT
					+ " = ?";
			String[] whereArgs = new String[] { ingredName };

			return mDatabase.update(FeedReaderContract.FeedEntry2.TABLE2,
					initialValues, where, whereArgs);
		}*/

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "Called onUpgrade..");
			db.execSQL(SQL_DELETE_TABLE1);
			db.execSQL(SQL_DELETE_TABLE2);
			db.execSQL(SQL_DELETE_TABLE3);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
			onCreate(db);
		}
	}
}
