package kevinpage.com;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity occurs when you are in 'Add Items' tab
 * selecting new ingredients under 'Ingredients'
 */
public class DontHave extends Activity {

	static ListView lvD;
	private SqlDatabase sqlDb;
	
	/**
	 * Updates the ListView element.
	 * @param lv The ListView object
	 * @param array The array of drinks
	 */
	private void fillData(ListView lv, String[] array) {
		ArrayAdapter<String> help = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.check, array);
		lv.setAdapter(help);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingredients);
		
		sqlDb = new SqlDatabase(this);
		
		/*InitDbTask initDb = new InitDbTask(this);
		initDb.execute(this, null, null);
		sqlDb = initDb.getDatabase();*/
		Cursor ingreds = sqlDb.getHasIngredients("0");
		String[] array;
		if(ingreds == null){
			array = new String[0];
		}
		else{
			array = new String[ingreds.getCount()];
			for(int i = 0; i<ingreds.getCount() && !(ingreds.isLast()); i++){
				array[i] = ingreds.getString(0);
				ingreds.moveToNext();
			}
		}
		
		

		/*for (String ingredient : data.totalIngredients) {
			if (!data.ownedIngredients.contains(ingredient)) {
				data.missingIngs.add(ingredient);
			}
		}*/
		lvD = (ListView) findViewById(R.id.ingredient_list);
		/*data.al = (String[]) data.missingIngs
				.toArray(new String[data.missingIngs.size()]);*/
		fillData(lvD, array);
		//fillData(lvD, data.al);
		lvD.setTextFilterEnabled(true);
		
		/**
		 * Handles event when user clicks ingredient to add to inventory.
		 * Adds it to My Inventory and removes it from this View.
		 */
		lvD.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				Toast.makeText(
						getApplicationContext(),
						"Added " + (((TextView) view).getText())
								+ " to Inventory", Toast.LENGTH_SHORT).show();

				//TODO Need to add if == null case for Cursor?
				Cursor ingred = sqlDb.getHasIngredient((((TextView) view)
						.getText()).toString());
				if(ingred == null){
					;
				}
				else{
					/**If the value is 0, or they don't have it, change it to 1*/
					if(ingred.getInt(0) == 0){
						sqlDb.updateHasValue(1, (((TextView) view)
							.getText()).toString());
					}
				}
				
				

				/*if (!data.ownedIngredients.contains((((TextView) view)
						.getText()).toString())) {
					data.ownedIngredients.add((((TextView) view).getText())
							.toString());
				}*/
				/*data.missingIngs.remove((((TextView) view).getText())
						.toString());*//**Shouldn't need this since only dealing w/ 1 table*/
				/** Fill in missing ingredients */
				Cursor missingCursor = sqlDb.getHasIngredients("0");
				String[] missingArray;
				if(missingCursor == null){
					missingArray = new String[0];
				}
				else{
					missingArray = new String[missingCursor.getCount()];
					for(int i = 0; i<missingCursor.getCount() && !(missingCursor.isLast()); i++){
						missingArray[i] = missingCursor.getString(0);
						missingCursor.moveToNext();
					}
				}				
				fillData(lvD, missingArray);
				
				
				/*data.al = (String[]) data.missingIngs
						.toArray(new String[data.missingIngs.size()]);*/
				
				/** Fill in ingredients they now have */
				Cursor haveCursor = sqlDb.getHasIngredients("1");
				String[] haveArray;
				if(haveCursor == null){
					haveArray = new String[0];
				}
				else{
					haveArray = new String[haveCursor.getCount()];
					for(int i = 0; i<haveCursor.getCount() && !(haveCursor.isLast()); i++){
						haveArray[i] = haveCursor.getString(0);
						haveCursor.moveToNext();
					}
				}				
				fillData(Have.lvH, haveArray);
				
				/*data.al2 = (String[]) data.ownedIngredients
						.toArray(new String[data.ownedIngredients.size()]);
				fillData(lvD, data.al);
				fillData(Have.lvH, data.al2);*/
				///////////////////////
			}
		});

		/**
		 * Handles exit when user goes back to main menu. 
		 * Clears out previous canMakeDrinks variable then 
		 * updates it to match what was chosen in this activity.
		 */
		final Button mainButton = (Button) findViewById(R.id.main_menu);
		mainButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				data.canMakeDrinks.clear();
				for (Drink drink : data.allDrinks) {
					if (drink.canMake()) {
						if (!data.canMakeDrinks.contains(drink))
							data.canMakeDrinks.add(drink);
					}
				}
				finish();
			}
		});

	}
}
