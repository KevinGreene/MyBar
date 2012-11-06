package kevinpage.com;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This class handles the 'Drinks I Can Make' activity
 * in the application.
 */
public class CanMakeDrinks extends Activity {
	
	private MyBarDatabase sqlDb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drinks);
		
		sqlDb = new MyBarDatabase(this);
		
		ArrayList<String> drinkNames = new ArrayList<String>();
		Cursor drinks = sqlDb.getPossibleDrinks();
		for(int i = 0; i < drinks.getCount() && !(drinks.isAfterLast()); i++){
			drinkNames.add(drinks.getString(0));
		}
		
		/*// Calls data file for possible drinks based on ingredients
		String[] drinkNames = new String[data.canMakeDrinks.size()];

		for (int i = 0; i < drinkNames.length; i++) {
			drinkNames[i] = ((data.canMakeDrinks.get(i)).getName());
		}*/

		//The list view to display all the names of possible drinks
		ListView lv = (ListView) findViewById(R.id.makeable_drinks);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.check, drinkNames));
		lv.setTextFilterEnabled(true);

		/**
		 * Handles button event when user clicks on specific drink
		 * in this view
		 */
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Pops up with selected drink
				AlertDialog.Builder adb = new AlertDialog.Builder(
						CanMakeDrinks.this);
				//Drink drink = data.canMakeDrinks.get(position);
				
				String drinkName = (((TextView)view).getText()).toString();
				
				Cursor cDrink = sqlDb.getDrinkInfo(drinkName);
				adb.setTitle(cDrink.getString(0) + " - Rating: " + cDrink.getString(1));
				
				int drink_id = cDrink.getInt(2);
				
				String instructions = cDrink.getString(3);
				
				String message = "Ingredients: \n";
				
				Cursor drinkIngreds = sqlDb.getDrinkIngredientsById(String.valueOf(drink_id));

				for(int i = 0; i < drinkIngreds.getCount() && !(drinkIngreds.isLast()); i++){
					//Cursor ingredName = sqlDb.getIngredById(String.valueOf(drinkIngreds.getInt(0)));
					String ingredName = drinkIngreds.getString(0);
					drinkIngreds.moveToNext();
					message += ingredName + " - " + drinkIngreds.getString(1) + "\n";
				}
				message += "\n";
				message += "Instructions: \n";
				message += instructions;
				

				adb.setMessage(message);
				adb.setPositiveButton("Ok", null);
				adb.show();
			}
		});

		// data.chosenDrink = data.canMakeDrinks.get(position);
		// Intent myIntent = new Intent(view.getContext(), DrinkDisplay.class);
		// CanMakeDrinks.this.startActivity(myIntent);
		// }
		// });

		/**
		 * Handles click to go back to main menu
		 */
		final Button mainButton = (Button) findViewById(R.id.main_menu);
		mainButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

	}
}
