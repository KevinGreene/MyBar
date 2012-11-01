package kevinpage.com;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This activity occurs when client is under the 'My Inventory'
 * tab under 'Ingredients'
 */
public class Have extends Activity {
	
	private SqlDatabase sqlDb;
	static ListView lvH;
	
	/**
	 * Updates the ListView elements
	 * @param lv The ListView display
	 * @param array The array of data to display
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
		String[] array;
		Cursor ingreds = sqlDb.getHasIngredients("1");
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

		
		/*data.al2 = (String[]) data.ownedIngredients
				.toArray(new String[data.ownedIngredients.size()]);*/
		lvH = (ListView) findViewById(R.id.ingredient_list);
		
		fillData(lvH, array);//
		//fillData(lvH, data.al2);
		lvH.setTextFilterEnabled(true);
		
		/**
		 * Handles event when user clicks ingredient to remove.
		 */
		lvH.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(
						getApplicationContext(),
						"Removed " + (((TextView) view).getText())
								+ " from Inventory", Toast.LENGTH_SHORT).show();
				
				/*Cursor ingred = sqlDb.getHasIngredient((((TextView) view)
						.getText()).toString());*//** TODO perhaps add check later */
				sqlDb.updateHasValue(0, (((TextView) view).getText()).toString());
				
				/*data.ownedIngredients.remove((((TextView) view).getText()));
				data.missingIngs.add((((TextView) view).getText()).toString());*/
				
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
				fillData(DontHave.lvD, missingArray);
				
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
				fillData(lvH, haveArray);
				
				/*data.al = (String[]) data.missingIngs
						.toArray(new String[data.missingIngs.size()]);
				data.al2 = (String[]) data.ownedIngredients
						.toArray(new String[data.ownedIngredients.size()]);
				fillData(lvH, data.al2);
				fillData(DontHave.lvD, data.al);*/
			}
		});

		/**
		 * Handles event when user clicks to go back to main menu.
		 * Reconfigure the drinks they are able to make.
		 */
		final Button mainButton = (Button) findViewById(R.id.main_menu);
		mainButton.setOnClickListener(new View.OnClickListener() {
			@Override
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