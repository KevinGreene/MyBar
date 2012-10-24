package kevinpage.com;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Provides access to SqlDatabase for ingredients and drinks
 * 
 * @author Zach
 * 
 */
public class SqlProvider extends ContentProvider {

	String TAG = "SqlProvider";

	public static String AUTHORITY = "kevinpage.com.SqlProvider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/sqldb");

	// MIME types for looking up a single drink or all ingredients
	public static final String DRINK_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/vdn.kevinpage.com.drink";
	public static final String INGREDIENT_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/vdn.kevinpage.com.ingredients";

	private SqlDatabase mDatabase;

	// Uri matcher stuff
	private static final int GET_DRINK = 0;
	private static final int GET_INGREDIENTS = 1;
	private static final UriMatcher sUriMatcher = buildUriMatcher();

	/**
	 * Builds up UriMatcher for queries
	 */
	private static UriMatcher buildUriMatcher() {
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		// to get a drink or ingredients
		matcher.addURI(AUTHORITY, "sqldb", GET_DRINK);
		matcher.addURI(AUTHORITY, "sqldb/#", GET_INGREDIENTS);// # means we can
																// get multiple

		return matcher;
	}

	@Override
	public boolean onCreate() {
		mDatabase = new SqlDatabase(getContext());
		return true;
	}

	/**
	 * Handles all the queries for data that may be requested in the
	 * application. When requesting a specific drink, the uri alone is required.
	 * When requesting all ingredients the selectionArgs argument must carry the
	 * search query as the first element. All other arguments ignored.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// Use the UriMatcher to see what kind of query we have and format the
		// db query accordingly
		switch (sUriMatcher.match(uri)) {
		case GET_DRINK:
			return getDrink(uri);
		case GET_INGREDIENTS:
			if (selectionArgs == null) {
				throw new IllegalArgumentException(
						"selectionArgs must be provided for the Uri + Uri");
			}
			return getIngredients(selectionArgs[0]);
		default:
			throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
	}

	private Cursor getIngredients(String query) {
		query = query.toLowerCase();
		String[] columns = new String[] { BaseColumns._ID,
				SqlDatabase.KEY_INGREDIENT };

		return mDatabase.getIngredients(query, columns);
	}

	private Cursor getDrink(Uri uri) {
		String rowId = uri.getLastPathSegment();
		String[] columns = new String[] { BaseColumns._ID,
				SqlDatabase.KEY_DRINK };

		return mDatabase.getDrink(rowId, columns);
	}
	
	@Override
	public String getType(Uri uri) {
		switch(sUriMatcher.match(uri)){
			case GET_DRINK:
				return DRINK_MIME_TYPE;
			case GET_INGREDIENTS:
				return INGREDIENT_MIME_TYPE;
			default:
                throw new IllegalArgumentException("Unknown URL " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

}
