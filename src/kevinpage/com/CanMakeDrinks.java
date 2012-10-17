package kevinpage.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This class handles the 'Drinks I Can Make' activity
 * in the application.
 */
public class CanMakeDrinks extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drinks);

		// Calls data file for possible drinks based on ingredients
		String[] drinkNames = new String[data.canMakeDrinks.size()];

		for (int i = 0; i < drinkNames.length; i++) {
			drinkNames[i] = ((data.canMakeDrinks.get(i)).getName());
		}

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
				Drink drink = data.canMakeDrinks.get(position);
				adb.setTitle(drink.getDisplayTitle());
				adb.setMessage(drink.getDisplayMessage());
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
