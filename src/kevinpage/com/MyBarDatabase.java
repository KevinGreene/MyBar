package kevinpage.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This is the class that deals entirely with the database.
 * TODO Refactor code to make more SRP; don't return Cursors
 * @author Zach
 *
 */
public class MyBarDatabase {
	
	private final DatabaseOpenHelper mDatabaseHelper; // used for queries later
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "mybar.db";
	private static String DB_PATH = "/data/data/kevinpage.com/databases/";

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The Context within which to work, used to create the DB
	 */
	public MyBarDatabase(Context context) {
		mDatabaseHelper = new DatabaseOpenHelper(context);
		//mDatabaseHelper.getReadableDatabase();
	}

	// TODO Build out QUERIES here

	/**
	 * Insert a drink into the database with the appropriate fields.
	 * @param name The name of the drink
	 * @param rating The rating of the drink
	 * @param instruct The instructions on how to make the drink
	 * @param ingreds The arraylist of ingredients for the drink
	 * @param amounts The matching arraylist of amounts of the ingredients
	 * @return True if successfully inserted
	 */
	public boolean insertDrink(String name, int rating, String instruct, ArrayList<String> ingreds, ArrayList<String> amounts){
		
		long drink_id = -1;
		
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();	
		
		Log.d("Database", "Got writable database");
		
		ContentValues ingredValues = new ContentValues();
		for(int i = 0; i < ingreds.size(); i++){
			ingredValues.put(FeedReaderContract.FeedEntry2.KEY_sINGREDIENT, ingreds.get(i));
			ingredValues.put(FeedReaderContract.FeedEntry2.KEY_HAS, 0);
			
			Log.d("Inserted Into Ingredients Table", ingreds.get(i));
			
			try{
				db.insertOrThrow(FeedReaderContract.FeedEntry2.TABLE2, null, ingredValues);
			}catch(SQLException ex){
				Log.d("Exception", "Could not insert ingredient");
				return false;
				
			}
			
			ingredValues.clear();
		}		
		
		ContentValues drinkValues = new ContentValues();
		drinkValues.put(FeedReaderContract.FeedEntry1.KEY_DRINK, name);
		drinkValues.put(FeedReaderContract.FeedEntry1.KEY_RATING, rating);
		drinkValues.put(FeedReaderContract.FeedEntry1.KEY_INSTRUCTIONS, instruct);
		try{
			drink_id = db.insertOrThrow(FeedReaderContract.FeedEntry1.TABLE1, null, drinkValues);
		}catch(SQLException ex){
			Log.d("Exception", "Could not insert drink");
			return false;
		}
		
		ContentValues drinkIngredValues = new ContentValues();
		for(int j = 0; j < ingreds.size(); j++){
			drinkIngredValues.put(FeedReaderContract.FeedEntry3.KEY_subID1, drink_id);
			drinkIngredValues.put(FeedReaderContract.FeedEntry3.KEY_ingredNAME, ingreds.get(j));
			drinkIngredValues.put(FeedReaderContract.FeedEntry3.KEY_AMOUNT, amounts.get(j));
			
			Log.d("Drink Ingredient: ", ingreds.get(j));//TODO
			Log.d("Drink Amount: ", amounts.get(j));//TODO
			Log.d("Drink ID: ", String.valueOf(drink_id));//TODO
			
			try{
				db.insertOrThrow(FeedReaderContract.FeedEntry3.TABLE3, null, drinkIngredValues);
			}catch(SQLException ex){
				Log.d("Exception", "Could not insert drink-ingredient");
				return false;
			}
		}
		
		Log.d("Got through", "ALL INFO ADDED");
		return true;
	}
	
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
		//db.close(); TODO
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
	 * Gets the drinks id by its name
	 * @param selectionArgs The name of the drink
	 * @return The integer id of the drink
	 */
	public int getDrinkIdByName(String selectionArgs){
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		String[] projection = new String[] {FeedReaderContract.FeedEntry1.KEY_ID1};
		String selection = FeedReaderContract.FeedEntry1.KEY_DRINK + " =?";
		String[] singleSelection = new String[] {selectionArgs};
		
		Cursor cursor = db.query(FeedReaderContract.FeedEntry1.TABLE1, projection, selection, singleSelection, null, null, null);
		int id = -1;
		if (cursor == null) {
			return id;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return id;
		}
		db.close();
		
		id = cursor.getInt(0);
		
		return id;
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
	 * Returns a List over all of the ingredients by name
	 * @return a List over all of the ingredients in alphabetical order by name
	 */
	public List<String> getAllDrinks(){
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		String[] projection = new String[] { FeedReaderContract.FeedEntry1.KEY_DRINK};//, FeedReaderContract.FeedEntry1.KEY_ID1 };
		String ordering = " COLLATE NOCASE";
		
		Cursor cursor = db.query(FeedReaderContract.FeedEntry1.TABLE1, projection, null, null, null, null, FeedReaderContract.FeedEntry1.KEY_DRINK + ordering);
		
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		//db.close(); TODO
		
		List<String> temp;
		temp = new ArrayList<String>(cursor.getCount());
		for(int i = 0; i<cursor.getCount() && !(cursor.isLast()); i++){
			if(cursor.isNull(0)){
				break;
			}
			temp.add(cursor.getString(0));
			cursor.moveToNext();
		}
		return temp;
	}

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
	
	/**
	 * Returns whether or not the user is able to make the drink or not
	 * @param selcectionArg the drink id
	 * @return True if it is makable, false otherwise
	 */
	public boolean canMakeDrink(int drink_id){
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		int numDrinkIngreds = getDrinkIngredientsById(String.valueOf(drink_id)).getCount();
		
		String[] selectionArgs = new String[]{"1", String.valueOf(drink_id)};
		
		Cursor cursor = db.rawQuery("SELECT " + FeedReaderContract.FeedEntry2.KEY_sINGREDIENT +
				" FROM " + FeedReaderContract.FeedEntry2.TABLE2 + " WHERE " +
				FeedReaderContract.FeedEntry2.KEY_HAS + " =?" + " INTERSECT SELECT " +
				FeedReaderContract.FeedEntry3.KEY_ingredNAME + " FROM " + FeedReaderContract.FeedEntry3.TABLE3 + 
				" WHERE " + FeedReaderContract.FeedEntry3.KEY_subID1 + " =?", selectionArgs);
		
		//db.close();
		
		if(cursor.getCount() == numDrinkIngreds){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Gets a Cursor over all possible drinks the user can make
	 * @return A Cursor over all possible drink combinations
	 */
	public Cursor getPossibleDrinks() {
		
		long startTime = System.nanoTime();//TODO
		ArrayList<String> validDrinks = new ArrayList<String>();
		List<String> allDrinks = getAllDrinks();
		for(int i = 0; i < allDrinks.size(); i++ ){
			String drinkName = allDrinks.get(i);
			int drinkId = getDrinkIdByName(drinkName);
			//Cursor drinkIgreds = getDrinkIngredientsById(String.valueOf(drinkId));
			boolean canMake = canMakeDrink(drinkId);
			
			if(canMake){
				validDrinks.add(drinkName);
			}
			
			/*boolean soFarSoGood = true;
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
			//allDrinks.moveToNext();
*/		}
		long endTime = System.nanoTime();//TODO
		Log.d("FIRST LOOP", String.valueOf((endTime - startTime)));
		
		if(validDrinks.isEmpty()){ return null; } //there are no possible drinks
		
		startTime = System.nanoTime();//TODO
		String inOp = "(";
		for(int k = 0; k < validDrinks.size(); k++){
			inOp += "?";
			if(k < validDrinks.size()-1){ inOp+=","; }
		}
		inOp += ")";
		endTime = System.nanoTime();//TODO
		Log.d("SECOND LOOP", String.valueOf(endTime - startTime));
		
		startTime = System.nanoTime();//TODO
		String[] projection = new String[] {FeedReaderContract.FeedEntry1.KEY_DRINK};
		String selection = FeedReaderContract.FeedEntry1.KEY_DRINK + " IN" + inOp;
		String[] selections = new String[validDrinks.size()];
		for(int z = 0; z < validDrinks.size(); z++){
			selections[z] = validDrinks.get(z);
		}
		endTime = System.nanoTime();//TODO
		Log.d("THIRD LOOP", String.valueOf(endTime - startTime));

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
	
	/**
	 * Deletes the corresponding drink from the all-drinks table ONLY
	 * @param drinkName The name of the drink to delete
	 * @return The number row of drink
	 */
	public long deleteDrink(String drinkName){
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		String selection = FeedReaderContract.FeedEntry1.KEY_DRINK + " = ?";
		String[] selectionArgs = {drinkName};
		
		return db.delete(FeedReaderContract.FeedEntry1.TABLE1, selection, selectionArgs);
	}

	////////////////////////////////////////////////////////////////////////////////////

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
		
		//TODO remove all commented code
		
		/** SQL to create first table of drinks */
/*		private static final String TABLE_CREATE1 = "CREATE TABLE "
				+ FeedReaderContract.FeedEntry1.TABLE1 + " ("
				+ FeedReaderContract.FeedEntry1.KEY_ID1	+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ FeedReaderContract.FeedEntry1.KEY_DRINK + " TEXT UNIQUE NOT NULL, "
				+ FeedReaderContract.FeedEntry1.KEY_RATING	+ " INTEGER NOT NULL, "
				+ FeedReaderContract.FeedEntry1.KEY_INSTRUCTIONS + " TEXT NOT NULL" + ");";
		*//** SQL to create second table of ingredients *//*
		private static final String TABLE_CREATE2 = "CREATE TABLE "
				+ FeedReaderContract.FeedEntry2.TABLE2 + " ("
				+ FeedReaderContract.FeedEntry2.KEY_ID2	+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ FeedReaderContract.FeedEntry2.KEY_sINGREDIENT	+ " TEXT UNIQUE NOT NULL," 
				+ FeedReaderContract.FeedEntry2.KEY_HAS	+ " INTEGER NOT NULL" + ");";
		*//** SQL to create third table of drink-ingredients *//*
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
				+ FeedReaderContract.FeedEntry3.TABLE3;*/

		DatabaseOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mHelperContext = context;
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			mDatabase = db;
			//db.execSQL(TABLE_CREATE1);
			//db.execSQL(TABLE_CREATE2);
			//db.execSQL(TABLE_CREATE3);
			//db.execSQL("PRAGMA foreign_keys=ON;");// enable foreign keys
			
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
			
			Log.d("DatabaseOpenHelper", "onCreate");
			//loadTableData();
		}

		/**
		 * Starts a thread to load the database table with words
		 */
/*		private void loadTableData() {
			new Thread(new Runnable() {
				public void run() {
					try {
						loadTables();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}).start();
		}*/

/*		private void loadTables() throws IOException {
			// Log.d(TAG, "Loading ingredients...");
			// Log.d(TAG2, "Loading drinks...");
			final Resources resources = mHelperContext.getResources();
			InputStream inputStream = resources.openRawResource(R.raw.drinks);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));

			try {
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
					*//**
					 * If ingredients were read in for the drink, add it to the
					 * db with the correct row id's
					 *//*
					if (!(drinkIngredients.isEmpty())) {
						for (int i = 0; i < drinkIngredients.size(); i++) {
							addDrinkIngredient(amounts.get(i), drinkIngredients.get(i),
									drinkRowId);
						}
					}
				}
			} finally {
				reader.close();
			}

		}*/

		/**
		 * Add an ingredient to the table.
		 * 
		 * @return rowId or -1 if failed
		 */
		public long addIngredient(String ingredient, int has) {
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
		 * 
		 */
		public long addDrinkIngredient(String amount, String name,
				long drinkId) {
			ContentValues initialValues = new ContentValues();
			initialValues
					.put(FeedReaderContract.FeedEntry3.KEY_subID1, drinkId);
			initialValues.put(FeedReaderContract.FeedEntry3.KEY_ingredNAME,
					name);
			initialValues.put(FeedReaderContract.FeedEntry3.KEY_AMOUNT, amount);

			return mDatabase.insert(FeedReaderContract.FeedEntry3.TABLE3, null,
					initialValues);
		}

		
		public void createDataBase() throws IOException {
			boolean dbExist = checkDataBase();
			if (dbExist) {
				Log.d("DatabaseOpenHelper", "DATBASE EXISTS");
			} else {
			
				this.getReadableDatabase();
				this.close();
				
				Log.v("database", "databae is being copied from assets to new db file");
				try {
					copyDataBase();
				} catch (IOException e) {
					throw new Error("Error copying database");
				}
			}
		}

		private boolean checkDataBase() {
			
			/*Log.d("DatabaseOpenHelper", "checkDatabase");
			File dbFile = new File(DB_PATH + DATABASE_NAME);
			return dbFile.exists();*/
			SQLiteDatabase checkDB = null;
			try {
				String myPath = DB_PATH + DATABASE_NAME;
				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READONLY);
			} catch (SQLiteException e) {
			}
			if (checkDB != null) {
				checkDB.close();
			}
			return checkDB != null ? true : false;
		}

		private void copyDataBase() throws IOException {
			// Open your local db as the input stream
			InputStream myInput = mHelperContext.getAssets().open(DATABASE_NAME);

			// Path to the just created empty db
			String outFileName = DB_PATH + DATABASE_NAME;

			// Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);

			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
			Log.d("DatabaseOpenHelper", "copydatabase");
		}

		public void openDataBase() throws SQLException {
			// Open the database
			String myPath = DB_PATH + DATABASE_NAME;
			
			/*mDatabase.execSQL(
					"CREATE TABLE IF NOT EXISTS \"android_metadata\" (\"locale\" TEXT DEFAULT 'en_US')"
					); mDatabase.execSQL("INSERT INTO \"android_metadata\" VALUES ('en_US')"
					);*/

			//Log.d("openDataBase", "meta_data");
			try{
				mDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);//SQLiteDatabase.NO_LOCALIZED_COLLATORS);//SQLiteDatabase.CREATE_IF_NECESSARY);
			}catch(SQLException e){
				Log.e("openDatabase", e.toString());
			}
			
			//return mDatabase != null;
		}
		
		@Override
	    public synchronized void close() 
	    {
	        if(mDatabase != null)
	            mDatabase.close();
	        super.close();
	    }
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//db.execSQL(SQL_DELETE_TABLE1);
			//db.execSQL(SQL_DELETE_TABLE2);
			//db.execSQL(SQL_DELETE_TABLE3);
			//db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
			onCreate(db);
		}
	}
}
