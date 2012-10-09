package kevinpage.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Main activity for initial View.
 * Sets up buttons and data.
 */
public class MyBar extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybar);

		if (!data.generatedDrinks) {
			String line;
			InputStream is2 = getResources().openRawResource(R.raw.drinks);
			BufferedReader DrinkReader = new BufferedReader(
					new InputStreamReader(is2));
			ArrayList<String> drinkIngredients = new ArrayList<String>();
			ArrayList<String> amounts = new ArrayList<String>();
			try {
				// Adds all of the drinks in the file to allDrinks
				while ((line = DrinkReader.readLine()) != null) {
					drinkIngredients.clear();
					amounts.clear();
					int r = Integer.parseInt(DrinkReader.readLine());
					while (true) {
						String ing = DrinkReader.readLine();
						if (ing.equals("0"))
							break;
						// TreeSet makes sure there are no duplicates
						data.totalIngredients.add(ing);
						data.missingIngs.add(ing);
						String a = DrinkReader.readLine();
						drinkIngredients.add(ing);
						amounts.add(a);
					}

					String instruct = DrinkReader.readLine();
					Drink d = new Drink(line, r, drinkIngredients, amounts,
							instruct);
					data.allDrinks.add(d);
				}
			} catch (IOException a) {
				// TODO Auto-generated catch block
				a.printStackTrace();
			}

			Collections.sort(data.allDrinks);
			data.generatedDrinks = true;
		}
		
		/**
		 * Button event for "Drinks I Can Make"
		 */
		final Button canMakeButton = (Button) findViewById(R.id.canMake);
		canMakeButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (data.canMakeDrinks.isEmpty()) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							MyBar.this);
					adb.setTitle("Sorry");
					adb.setMessage("You can't make any drinks yet!\nUpdate your inventory first");
					adb.setPositiveButton("Ok", null);
					adb.show();
				} else {
					Intent myIntent = new Intent(v.getContext(),
							CanMakeDrinks.class);
					MyBar.this.startActivity(myIntent);
				}
			}
		});

		/**
		 * Button event for "Ingredients".
		 * Sets up tabs for 'Add Items' and 'My Inventory'
		 */
		final Button ingredientButton = (Button) findViewById(R.id.ingredients);
		ingredientButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), TabInventory.class);
				MyBar.this.startActivity(myIntent);
			}
		});
		
		/**
		 * Button event for "Browse Drinks"
		 */
		final Button browseButton = (Button) findViewById(R.id.browse);
		browseButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), AllDrinks.class);
				MyBar.this.startActivity(myIntent);
			}
		});

		/**
		 * Button event for "Random Drink"
		 */
		final Button randomAllButton = (Button) findViewById(R.id.random);
		randomAllButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int randomIndex = (int) (Math.random() * data.allDrinks.size());
				AlertDialog.Builder adb = new AlertDialog.Builder(MyBar.this);
				Drink drink = data.allDrinks.get(randomIndex);
				adb.setTitle(drink.getDisplayTitle());
				adb.setMessage(drink.getDisplayMessage());
				adb.setPositiveButton("Ok", null);
				adb.show();
			}
		});

		/**
		 * Button event for "Random Drink I Can Make"
		 */
		final Button randomCanMakeButton = (Button) findViewById(R.id.randomCanMake);
		randomCanMakeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (data.canMakeDrinks.isEmpty()) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							MyBar.this);
					adb.setTitle("Sorry");
					adb.setMessage("You can't make any drinks yet!\nUpdate your inventory first");
					adb.setPositiveButton("Ok", null);
					adb.show();
				} else {
					int randomIndex = (int) (Math.random() * data.canMakeDrinks
							.size());
					AlertDialog.Builder adb = new AlertDialog.Builder(
							MyBar.this);
					Drink drink = data.canMakeDrinks.get(randomIndex);
					adb.setTitle(drink.getDisplayTitle());
					adb.setMessage(drink.getDisplayMessage());
					adb.setPositiveButton("Ok", null);
					adb.show();
				}
			}
		});

	}
}