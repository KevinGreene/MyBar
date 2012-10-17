package kevinpage.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
 /**
  * This class creates an activity for the 'Browse Drinks'
  * option in the application.
  */
public class AllDrinks extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drinks);
		
		// Calls data file for all drinks
		String[] drinkName = new String[data.allDrinks.size()];

		for (int i = 0; i < data.allDrinks.size(); i++) {
			drinkName[i] = ((data.allDrinks.get(i)).getName());
		}

		//The list view to display all the names of all drinks
		ListView lv = (ListView) findViewById(R.id.makeable_drinks);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.check, drinkName));
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
				Drink drink = data.allDrinks.get(position);
				adb.setTitle(drink.getDisplayTitle());
				adb.setMessage(drink.getDisplayMessage());
				adb.setPositiveButton("Ok", null);
				adb.show();
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