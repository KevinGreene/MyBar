package kevinpage.com;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import kevinpage.com.MyBarDatabase;

 /**
  * This class creates an activity for the 'Browse Drinks'
  * option in the application.
  */
public class AllDrinks extends Activity {
	
	private MyBarDatabase sqlDb;
	
	/**
	 * Fills a String array based on a cursor
	 * @param cursor The cursor to parse through
	 * @return A String array based on data in cursor
	 */
/*	private ArrayList<String> fillArray(Cursor cursor){
		ArrayList<String> temp;
		if(cursor == null){
			temp = new ArrayList<String>();
		}
		else{
			temp = new ArrayList<String>(cursor.getCount());
			for(int i = 0; i<cursor.getCount() && !(cursor.isLast()); i++){
				if(cursor.isNull(0)){
					break;
				}
				temp.add(cursor.getString(0));
				cursor.moveToNext();
			}
		}
		return temp;
	}*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drinks);
		
		sqlDb = new MyBarDatabase(this);
		
		List<String> drinkNames = sqlDb.getAllDrinks();
		//ArrayList<String> drinkNames = new ArrayList<String>();
		
		//drinkNames = fillArray(cursor);

		//The list view to display all the names of all drinks
		ListView lv = (ListView) findViewById(R.id.makeable_drinks);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.check, drinkNames));
		lv.setTextFilterEnabled(true);

		/**
		 * Handles user click on drink 
		 */
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						AllDrinks.this); //Opens alert dialog box displaying drink selected

				
				String drinkName = (((TextView)view).getText()).toString();
				
				Cursor cDrink = sqlDb.getDrinkInfo(drinkName);
				adb.setTitle(cDrink.getString(0) + " - Rating: " + cDrink.getString(1));
				
				int drink_id = cDrink.getInt(2);
				
				String instructions = cDrink.getString(3);
				
				String message = "Ingredients: \n";
				
				Cursor drinkIngreds = sqlDb.getDrinkIngredientsById(String.valueOf(drink_id));

				for(int i = 0; i < drinkIngreds.getCount() && !(drinkIngreds.isAfterLast()); i++){
					//Cursor ingredName = sqlDb.getIngredById(String.valueOf(drinkIngreds.getInt(0)));
					String ingredName = drinkIngreds.getString(0);
					String rating = drinkIngreds.getString(1);
					drinkIngreds.moveToNext();
					message += ingredName + " - " + rating + "\n";
				}
				message += "\n";
				message += "Instructions: \n";
				message += instructions;
				
				adb.setMessage(message);
				adb.setPositiveButton("Ok", null);
				adb.show();
				

			}
		});
		
		/**
		 * Listener for long clicks on items
		 */
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
					int arg2, long arg3) {
				
				final String drinkName = (((TextView)arg1).getText()).toString();
				
				AlertDialog.Builder adb = new AlertDialog.Builder(AllDrinks.this);
				adb.setMessage("Are you sure you want to delete this drink?").setTitle("Delete Drink");
				adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               sqlDb.deleteDrink(drinkName);
			               Intent i = new Intent(arg1.getContext(), TabInventory2.class);
			               finish();
			               startActivity(i);
			           }
			       });
				adb.setNegativeButton("Cancel", null);
				
				adb.show();
				
				return false;
			}
			
		});

		/**
		 * Handles 'Main Menu' button to go back from this view to
		 * the main menu
		 */
		final Button mainButton = (Button) findViewById(R.id.main_menu);
		mainButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//'Glue' to next activity (the main menu)
				Intent myIntent = new Intent(v.getContext(), MyBar.class);
				AllDrinks.this.startActivity(myIntent);
			}
		});
		
	}
}