package kevinpage.com;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.util.Log;
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
	private void fillData(ListView lv, ArrayList<String> array) {
		ArrayAdapter<String> help = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.check, array);
		lv.setAdapter(help);
	}
	
	/**
	 * Fills a String array based on a cursor
	 * @param cursor The cursor to parse through
	 * @return A String array based on data in cursor
	 */
	private ArrayList<String> fillArray(Cursor cursor){
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
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingredients);
		
		
		sqlDb = new SqlDatabase(this);

		Cursor ingreds = sqlDb.getHasIngredients("0");
		ArrayList<String> array = fillArray(ingreds);
		
		lvD = (ListView) findViewById(R.id.ingredient_list);
		
		fillData(lvD, array);
		
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

				
				sqlDb.updateHasValue(1, (((TextView) view)
						.getText()).toString());
				

				/** Fill in missing ingredients */
				Cursor missingCursor = sqlDb.getHasIngredients("0");
				ArrayList<String> missingArray = fillArray(missingCursor);
				fillData(lvD, missingArray);
				
				/** Fill in ingredients they now have */
				Cursor haveCursor = sqlDb.getHasIngredients("1");
				ArrayList<String> haveArray = fillArray(haveCursor);	
				fillData(Have.lvH, haveArray);
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
				/*data.canMakeDrinks.clear();
				for (Drink drink : data.allDrinks) {
					if (drink.canMake()) {
						if (!data.canMakeDrinks.contains(drink))
							data.canMakeDrinks.add(drink);
					}
				}*/
				finish();
			}
		});

	}
}
