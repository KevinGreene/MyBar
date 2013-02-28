package kevinpage.com;

import android.provider.BaseColumns;

public class FeedReaderContract{
	
	/** Prevent this class from being instantiated*/
	private  FeedReaderContract() {}	
	
	/** The columns included in our all-drinks table */
	public static abstract class FeedEntry1 implements BaseColumns {		
		public static final String TABLE1 = "drinks";
		public static final String KEY_ID1 = "drink_id";
		public static final String KEY_DRINK = "name";
		public static final String KEY_RATING = "rating";
		public static final String KEY_INSTRUCTIONS = "instructions";
	}
	
	/** The columns included in our all-ingredients table */
	public static abstract class FeedEntry2 implements BaseColumns {		
		public static final String TABLE2 = "ingredients";
		public static final String KEY_ID2 = "ingred_id";
		public static final String KEY_sINGREDIENT = "name";
		public static final String KEY_HAS = "has";
	}
	
	/** The columns included in our drink-ingredients table */
	public static abstract class FeedEntry3 implements BaseColumns{		
		public static final String TABLE3 = "drinkIngredients";
		public static final String KEY_ID3 = "_id";
		public static final String KEY_subID1 = "drink_id";
		public static final String KEY_ingredNAME = "name";
		public static final String KEY_AMOUNT = "amount";		
	}	
}
